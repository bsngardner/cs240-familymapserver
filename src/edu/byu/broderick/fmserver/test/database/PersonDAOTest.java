package edu.byu.broderick.fmserver.test.database;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by broderick on 3/22/17.
 */
public class PersonDAOTest {


    Database db;
    private User user1 = new User("uA", "a", "email", "firstname",
            "lastname", "gender", "idA", "info");
    private User user2 = new User("uB", "b", "email", "firstname",
            "lastname", "gender", "idB", "info");

    private Person personA = new Person("idA", "uA", "firstname", "lastname",
            "m", "father", "mother", "spouse");
    private Person personB = new Person("idB", "uB", "firstname", "lastname",
            "m", "father", "mother", "spouse");
    private Person personC = new Person("idAp", "uA", "firstname", "lastname",
            "m", "father", "mother", "spouse");
    private Person personD = new Person("idBp", "uB", "firstname", "lastname",
            "m", "father", "mother", "spouse");

    private Event eventA1 = new Event("eA1", "uA", "idA", 1.0, 1.0,
            "country", "city", "eventType", "year");
    private Event eventA2 = new Event("eA2", "uA", "idAp", 1.0, 1.0,
            "country", "city", "eventType", "year");

    private Event eventB1 = new Event("eB1", "uB", "idB", 1.0, 1.0,
            "country", "city", "eventType", "year");
    private Event eventB2 = new Event("eB2", "uB", "idAp", 1.0, 1.0,
            "country", "city", "eventType", "year");

    @Before
    public void setUp() throws Exception {
        db = Database.getDB();
        db.resetDatabase();
    }

    @After
    public void tearDown() throws Exception {
        db.resetDatabase();
    }

    @Test
    public void storeNewPerson() throws Exception {
        db.resetDatabase();
        db.userData.storeUser(user1);
        db.personData.storeNewPerson(new Person(personA));
        List<Person> persons = db.personData.loadUserPersons(user1);
        Person loaded_person = persons.get(0);
        assertFalse(personA.getPersonID().equals(loaded_person.getPersonID()));
    }

    @Test
    public void storePerson() throws Exception {
        db.resetDatabase();
        db.userData.storeUser(user1);
        db.personData.storePerson(personA);
        List<Person> persons = db.personData.loadUserPersons(user1);
        Person loaded_person = persons.get(0);
        assertTrue(personA.equals(loaded_person));
    }

    public void fillDatabase() throws SQLException {
        db.resetDatabase();

        db.userData.storeUser(user1);
        db.userData.storeUser(user2);

        db.personData.storePerson(personA);
        db.personData.storePerson(personB);
        db.personData.storePerson(personC);
        db.personData.storePerson(personD);

        db.eventData.storeEvent(eventA1);
        db.eventData.storeEvent(eventA2);
        db.eventData.storeEvent(eventB1);
        db.eventData.storeEvent(eventB2);
    }

    @Test
    public void loadUserPersons() throws Exception {
        fillDatabase();
        Set<Person> persons = new HashSet<>(db.personData.loadUserPersons(user1));
        assertTrue(persons.contains(personA));
        assertFalse(persons.contains(personB));
        assertTrue(persons.contains(personC));
        assertFalse(persons.contains(personD));

    }

    @Test
    public void loadPerson() throws Exception {
        fillDatabase();
        Person person = db.personData.loadPerson(user1, "idA");
        assertTrue(person.equals(personA));
        assertFalse(person.equals(personB));
        assertFalse(person.equals(personC));
        assertFalse(person.equals(personD));
    }

    @Test
    public void updatePerson() throws Exception {
         fillDatabase();
         Person p = new Person(personA);
         p.setFirstname("jelly");
         db.personData.updatePerson(p);
         p = db.personData.loadPerson(user1, personA.getPersonID());
         assertTrue(p.getFirstname().equals("jelly"));
    }

    @Test
    public void deleteUserPersons() throws Exception {
        fillDatabase();
        db.personData.deleteUserPersons(user2);
        Set<Person> persons = new HashSet<>(db.personData.loadUserPersons(user1));
        assertFalse(persons.size() == 0);
        persons = new HashSet<>(db.personData.loadUserPersons(user2));
        assertTrue(persons.size() == 0);
    }

    @Test
    public void deletePersons() throws Exception {
        fillDatabase();
        db.personData.deletePersons();
        Set<Person> persons = new HashSet<>(db.personData.loadUserPersons(user1));
        assertTrue(persons.size() == 0);
        persons = new HashSet<>(db.personData.loadUserPersons(user2));
        assertTrue(persons.size() == 0);
    }

}