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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by broderick on 3/22/17.
 */
public class EventDAOTest {

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
    public void storeNewEvent() throws Exception {

        db.resetDatabase();
        db.userData.storeUser(user1);
        db.eventData.storeNewEvent(new Event(eventA1));
        List<Event> events = db.eventData.loadUserEvents(user1);
        Event loaded_event = events.get(0);
        assertFalse(eventA1.getEventid().equals(loaded_event.getEventid()));
    }

    @Test
    public void storeEvent() throws Exception {
        db.resetDatabase();
        db.userData.storeUser(user1);
        db.eventData.storeEvent(eventA1);
        List<Event> events = db.eventData.loadUserEvents(user1);
        Event loaded_event = events.get(0);
        assertTrue(eventA1.equals(loaded_event));
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
    public void loadUserEvents() throws Exception {
        fillDatabase();
        Set<Event> events = new HashSet<>(db.eventData.loadUserEvents(user1));
        assertTrue(events.contains(eventA1));
        assertTrue(events.contains(eventA1));
        assertFalse(events.contains(eventB1));
        assertFalse(events.contains(eventB2));
    }

    @Test
    public void loadPersonEvents() throws Exception {
        fillDatabase();
        Set<Event> events = new HashSet<>(db.eventData.loadPersonEvents(personC));
        assertTrue(events.contains(eventA2));
        assertTrue(events.contains(eventB2));
        assertFalse(events.contains(eventA1));
        assertFalse(events.contains(eventB1));
    }

    @Test
    public void loadEvent() throws Exception {
        fillDatabase();
        Event e = db.eventData.loadEvent(user1, "eA1");
        assertTrue(eventA1.equals(e));
        assertFalse(e.equals(eventA2));
        assertFalse(e.equals(eventB1));
        assertFalse(e.equals(eventB2));
    }

    @Test
    public void deleteUserEvents() throws Exception {
        fillDatabase();
        db.eventData.deleteUserEvents(user2);
        Set<Event> events = new HashSet<>(db.eventData.loadUserEvents(user2));
        assertTrue(events.size() == 0);
        events = new HashSet<>(db.eventData.loadUserEvents(user1));
        assertTrue(events.size() == 2);
    }

    @Test
    public void deleteEvents() throws Exception {
        fillDatabase();
        db.eventData.deleteEvents();
        Set<Event> events = new HashSet<>(db.eventData.loadUserEvents(user2));
        assertTrue(events.size() == 0);
        events = new HashSet<>(db.eventData.loadUserEvents(user1));
        assertTrue(events.size() == 0);
    }

}