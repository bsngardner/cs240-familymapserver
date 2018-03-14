package edu.byu.broderick.fmserver.main.database;

import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;

import java.util.*;

/**
 * Person Data Access Object
 *
 * @author Broderick Gardner
 */
public class PersonDAO {

    private static final String PERSON_TABLE = "main.persons";
    private static final List<String> PERSON_COLUMNS = Arrays.asList("personid", "userid", "firstname", "lastname", "gender", "father", "mother", "spouse");

    private Set<String> uids = null;

    private final Database db;

    /**
     * Constructor.
     *
     * @param db
     */
    public PersonDAO(Database db) {
        this.db = db;
    }

    /**
     * Stores Person in database and generates personid
     *
     * @param person
     */
    public void storeNewPerson(Person person) {

        if (uids == null) {
            uids = new TreeSet<>();
            List<List<Object>> records = this.db.select(PERSON_TABLE, Arrays.asList("personid"), null, null);
            for (List<Object> record : records) {
                uids.add((String) record.get(0));
            }
        }

        String uid;
        do {
            uid = this.db.generateID();
        } while (uids.contains(uid));
        person.setPersonid(uid);

        storePerson(person);
    }


    /**
     * Stores a new person with a given personid
     */
    public void storePerson(Person person) {
        this.db.insert(PERSON_TABLE, PERSON_COLUMNS, person.getEntryList());
    }

    /**
     * Loads all person entries associated with given User
     *
     * @param user
     * @return
     */
    public List<Person> loadUserPersons(User user) {
        List<Person> persons = new ArrayList<>();
        List<List<Object>> records = this.db.select(PERSON_TABLE, PERSON_COLUMNS, "userid", user.getUsername());
        for (List<Object> record : records) {
            persons.add(personFromRecord(record));
        }
        return persons;
    }

    /**
     * Loads a person with a given person id
     *
     * @param personid
     * @return
     */
    public Person loadPerson(User user, String personid) {
        List<List<Object>> records = this.db.select(PERSON_TABLE, PERSON_COLUMNS, "personid", personid);
        if (records.size() < 1)
            return null;
        List<Object> record = records.get(0);
        Person person = personFromRecord(record);
        if (!person.getDescendant().equals(user.getUsername()))
            return null;
        else
            return person;

    }


    /**
     * Updates database entry for Person
     *
     * @param person
     */
    public void updatePerson(Person person) {
        this.db.update(PERSON_TABLE, PERSON_COLUMNS, person.getEntryList(), "personid", person.getPersonID());
    }

    /**
     * Helper method for creating Person object from database record
     *
     * @param record
     * @return
     */
    private Person personFromRecord(List<Object> record) {
        String personid = (String) record.get(0);
        String username = (String) record.get(1);
        String firstname = (String) record.get(2);
        String lastname = (String) record.get(3);
        String gender = (String) record.get(4);
        String father = (String) record.get(5);
        String mother = (String) record.get(6);
        String spouse = (String) record.get(7);

        return new Person(personid, username, firstname, lastname, gender, father, mother, spouse);
    }

    /**
     * Delete persons associated with user from database
     *
     * @param user
     */
    public void deleteUserPersons(User user) {
        db.delete(PERSON_TABLE, "userid", user.getUsername());
    }

    /**
     * Delete all persons from table
     */
    public void deletePersons() {
        db.delete(PERSON_TABLE, null, null);
    }
}
