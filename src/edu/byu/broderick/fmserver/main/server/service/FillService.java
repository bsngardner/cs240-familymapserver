package edu.byu.broderick.fmserver.main.server.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.FillRequest;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.FillResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Server fill call service.  Erases all event and person data associated with a user and generates new family data.  Default 4 generations.
 *
 * @author Broderick Gardner
 */
public class FillService {

    private List<String> female_names = new ArrayList<>();
    private List<String> male_names = new ArrayList<>();
    private List<String> surnames = new ArrayList<>();
    private Map<String, List<LocationData>> locations = new HashMap<>();

    private Random rand = new Random();

    /**
     * Constructor.  Imports dictionary name and location data to be kept in memory for generating event data.
     */
    public FillService() {
        super();
        Gson gson = new GsonBuilder().create();

        try {
            NameDict fnames = gson.fromJson(new BufferedReader(new FileReader("data/json/fnames.json")), NameDict.class);
            NameDict mnames = gson.fromJson(new BufferedReader(new FileReader("data/json/mnames.json")), NameDict.class);
            NameDict snames = gson.fromJson(new BufferedReader(new FileReader("data/json/mnames.json")), NameDict.class);
            LocationDict locData = gson.fromJson(new BufferedReader(new FileReader("data/json/locations.json")), LocationDict.class);

            for (String s : fnames.data) {
                female_names.add(s);
            }

            for (String s : mnames.data) {
                male_names.add(s);
            }

            for (String s : snames.data) {
                surnames.add(s);
            }

            for (LocationData d : locData.data) {
                if (d.country == null) {
                    String[] split = d.city.split(",");
                    d.city = split[0];
                    d.country = split[1];
                }
                if (locations.containsKey(d.country)) {
                    locations.get(d.country).add(d);
                } else {
                    List<LocationData> list = new ArrayList<>();
                    list.add(d);
                    locations.put(d.country, list);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find dictionary JSON files! Bad");
            e.printStackTrace();
        }
    }


    /**
     * Dispatches this object to handle the fill service request, and returns result
     *
     * @param request
     * @return
     */
    public Result service(FillRequest request) {
        Result result;
        Database db = Database.getDB();

        System.out.println("COMMAND: fill");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            return result;
        }

        User user = db.userData.loadUser(request.getUsername());
        if (user == null) {
            result = new ErrorResult("Specified username does not exist");
            System.out.println("Command failed due to specified username does not exist");
            return result;
        }

        int gens;
        if ((gens = request.getGenerations()) < 1) {
            result = new ErrorResult("Invalid generations parameter");
            System.out.println("Command failed due to invalid generations parameter");
            return result;
        }

        db.eventData.deleteUserEvents(user);
        db.personData.deleteUserPersons(user);

        fillUser(user, gens);

        int numPersons = db.personData.loadUserPersons(user).size();
        int numEvents = db.eventData.loadUserEvents(user).size();
        result = new FillResult(numPersons, numEvents);

        System.out.println("Command successful");

        return result;
    }

    /**
     * Fills a number of generations for the given user, storing Person and Event data in the database.
     *
     * @param user
     * @param generations
     */
    public void fillUser(User user, int generations) {

        String username = user.getUserName();
        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        String gender = user.getGender();

        Person person = new Person(username, firstname, lastname, gender, null, null, null);
        Database.getDB().personData.storeNewPerson(person);

        user.setPersonID(person.getPersonID());
        Database.getDB().userData.updateUser(user);

        int birthYearBase = rand.nextInt(50) + 1950;
        fillEvents(person, birthYearBase);
        fillTree(username, person, generations, birthYearBase - 20);
    }

    /**
     * Recursive method for filling family tree with person and event data
     *
     * @param username
     * @param child
     * @param gens
     * @param birthYearBase
     */
    private void fillTree(String username, Person child, int gens, int birthYearBase) {
        if (gens == 0)
            return;

        Person father = createPerson(username, child);
        child.setFather(father.getPersonID());

        Person mother = createPerson(username, child);
        child.setMother(mother.getPersonID());

        fillEvents(father, birthYearBase);   //Order important! father spouse must be null for fillEvents or multiple marriage events are made
        father.setSpouse(mother.getPersonID());
        mother.setSpouse(father.getPersonID());
        fillEvents(mother, birthYearBase);

        Database.getDB().personData.updatePerson(child);
        Database.getDB().personData.updatePerson(father);
        Database.getDB().personData.updatePerson(mother);

        fillTree(username, father, gens - 1, birthYearBase - 20);
        fillTree(username, mother, gens - 1, birthYearBase - 20);
    }

    /**
     * Creates a father for the child if there is no father, mother if there is no mother
     *
     * @param username
     * @param child
     * @return
     */
    private Person createPerson(String username, Person child) {
        String gender;
        String firstname;
        String lastname;
        if (child.getFather() == null) {
            gender = "m";
            lastname = child.getLastname();
            firstname = male_names.get(rand.nextInt(male_names.size()));
        } else if (child.getMother() == null) {
            gender = "f";
            lastname = surnames.get(rand.nextInt(surnames.size()));
            firstname = female_names.get(rand.nextInt(female_names.size()));
        } else
            return null;

        Person person = new Person(username, firstname, lastname, gender, null, null, null);
        Database.getDB().personData.storeNewPerson(person);
        return person;
    }

    /**
     * Generates events randomly.  Naively places all events for a single person in 1 country.  No events take place
     * after a person has died and some events have a chance of not happening for a given person.
     *
     * @param person
     * @param birthYearBase
     */
    private void fillEvents(Person person, int birthYearBase) {
        String username = person.getDescendant();
        String personid = person.getPersonID();

        String country = getRandomCountry();
        List<LocationData> locs = new LinkedList<>(locations.get(country));

        int birthyear = birthYearBase + rand.nextInt(12) - 6;
        int deathyear = (int) (birthyear + rand.nextGaussian() * 15 + 85);
        if (deathyear > 2017 || deathyear < birthyear + 20)
            deathyear = -1;

        int marriageyear = (int) (birthyear + rand.nextGaussian() * 5 + 25);
        if (marriageyear > 2017 || marriageyear < birthyear + 15 || person.getSpouse() == null)
            marriageyear = -1;

        int graduateyear = (int) (birthyear + rand.nextGaussian() * 2 + 24);
        int modern_life = 1980 - birthyear;
        int college_chance = rand.nextInt((modern_life < 5) ? 5 : modern_life);
        if (graduateyear > 2017 || graduateyear < birthyear + 10 || graduateyear > deathyear || college_chance < 3)
            graduateyear = -1;

        int boughthouseyear = (int) (birthyear + rand.nextGaussian() * 10 + 45);
        if (boughthouseyear > birthyear + 65)
            boughthouseyear -= 30;
        if (boughthouseyear < birthyear + 20)
            boughthouseyear = -1;
        if (boughthouseyear > deathyear)
            boughthouseyear = -1;

        int majorsurgeryyear;
        if (rand.nextInt(5) < 2 && deathyear > 0)
            majorsurgeryyear = birthyear + rand.nextInt(deathyear - birthyear - 5) + 5;
        else
            majorsurgeryyear = -1;

        int lifecrisisyear = (rand.nextInt(2) < 1) ? (int) (birthyear + rand.nextGaussian() * 5 + 40) : -1;
        if (lifecrisisyear > deathyear || lifecrisisyear < birthyear + 25)
            lifecrisisyear = -1;

        int ridebikeyear = (rand.nextInt(5) < 3) ? (int) (birthyear + rand.nextGaussian() * 3 + 10) : -1;
        if (ridebikeyear < birthyear + 4 || ridebikeyear > birthyear + 20)
            ridebikeyear = -1;

        double latitude;
        double longitude;
        String city;

        int r = rand.nextInt(locs.size());
        LocationData loc = locs.get(r);
        locs.remove(loc);

        makeEvent(username, personid, loc, "birth", birthyear);

        if (deathyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "death", deathyear);
        }
        if (marriageyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "marriage", marriageyear);
            makeEvent(username, person.getSpouse(), loc, "marriage", marriageyear);
        }
        if (graduateyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "graduation", graduateyear);
        }
        if (boughthouseyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "boughtHouse", boughthouseyear);
        }
        if (majorsurgeryyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "majorSurgery", majorsurgeryyear);
        }
        if (lifecrisisyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "lifeCrisis", lifecrisisyear);
        }
        if (ridebikeyear > 0) {
            if (locs.size() <= 0)
                locs = new LinkedList<>(locations.get(country));
            r = rand.nextInt(locs.size());
            loc = locs.get(r);
            locs.remove(loc);
            makeEvent(username, personid, loc, "rideBike", ridebikeyear);
        }

    }

    /**
     * Actually creates the event and stores it in the database
     *
     * @param username
     * @param personid
     * @param loc
     * @param eventType
     * @param year
     */
    private void makeEvent(String username, String personid, LocationData loc, String eventType, int year) {
        Event event = new Event(username, personid, Double.parseDouble(loc.latitude), Double.parseDouble(loc.longitude), loc.country, loc.city, eventType, Integer.toString(year));
        Database.getDB().eventData.storeNewEvent(event);
    }

    /**
     * Returns a random country form the dictionary map
     *
     * @return
     */
    private String getRandomCountry() {
        Set<String> set = locations.keySet();
        int index = rand.nextInt(set.size());
        Iterator<String> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    /**
     * Static inner class only used inside the container class.  Container class for location data.
     */
    private static class LocationData {
        public String latitude;
        public String longitude;
        public String city;
        public String country;

        @Override
        public int hashCode() {
            return Objects.hash(latitude, longitude, city, country);
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }
    }

    /**
     * Static helper class only used to import data from JSON
     */
    private static class LocationDict {
        public List<LocationData> data;


    }

    /**
     * Static helper class only used to import data from JSON
     */
    private static class NameDict {
        public List<String> data;
    }
}
