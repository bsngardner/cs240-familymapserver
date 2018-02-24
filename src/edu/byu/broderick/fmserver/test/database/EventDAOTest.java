package edu.byu.broderick.fmserver.test.database;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.database.EventDAO;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by broderick on 3/22/17.
 */
public class EventDAOTest {

    Database db;
    User user = new User("username","password","email", "firstname", "lastname", "gender", "personid", "info");
    Event event = new Event("eventid","username","personid",1.0,1.0,"country","city","eventType","year");

    @Before
    public void setUp() throws Exception {
        db = Database.getDB();
        db.resetDatabase();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void storeNewEvent() throws Exception {

        db.resetDatabase();
        db.eventData.storeNewEvent(event);
        List<Event> events = db.eventData.loadUserEvents(user);
        Event loaded_event = events.get(0);
        assertTrue(!event.getPersonID().equals(loaded_event.getPersonID()));
    }

    @Test
    public void storeEvent() throws Exception {
        db.resetDatabase();
        db.eventData.storeNewEvent(event);
        List<Event> events = db.eventData.loadUserEvents(user);
        Event loaded_event = events.get(0);
        assertTrue(event.equals(loaded_event));
    }

    @Test
    public void loadUserEvents() throws Exception {

    }

    @Test
    public void loadPersonEvents() throws Exception {

    }

    @Test
    public void loadEvent() throws Exception {
        
    }

    @Test
    public void deleteUserEvents() throws Exception {

    }

    @Test
    public void deleteEvents() throws Exception {

    }

}