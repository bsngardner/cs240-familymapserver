package edu.byu.broderick.fmserver.test.database;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * JUnit tests for UserDAO
 *
 * @author Broderick Gardner
 */
public class UserDAOTest {

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
    public void storeUser() throws Exception {
        db.userData.storeUser(user1);
        assertTrue(db.userData.loadUser(user1.getUsername()).equals(user1));
    }

    @Test
    public void authenticateUser() throws Exception {
        fillDatabase();
        User.AuthToken key = db.userData.authenticateUser(user1);
        User user = db.userData.loadUserFromAuthKey(key.key());
        assertTrue(user1.equals(user));
    }

    @Test
    public void loadUserFromAuthKey() throws Exception {
        fillDatabase();
        User.AuthToken key = db.userData.authenticateUser(user1);
        User user = db.userData.loadUserFromAuthKey(key.key());
        assertTrue(user1.equals(user));
    }

    @Test
    public void loadUser() throws Exception {
        fillDatabase();
        User user = db.userData.loadUser(user1.getUsername());
        assertTrue(user1.equals(user));
    }

    @Test
    public void updateUser() throws Exception {
        fillDatabase();
        User user = new User(user1);
        user.setInfo("Quick brown fox");
        db.userData.updateUser(user);
        user = db.userData.loadUser(user.getUsername());
        assertTrue(user.getInfo().equals("Quick brown fox"));
    }

    @Test
    public void deleteUsers() throws Exception {
        fillDatabase();
        db.userData.deleteUsers();
        User user = db.userData.loadUser(user1.getUsername());
        assertTrue(user == null);
        user = db.userData.loadUser(user2.getUsername());
        assertTrue(user == null);
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

}
