package edu.byu.broderick.fmserver.test.model;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.server.json.JSONEncoder;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by broderick on 3/22/17.
 */
public class PersonTest {


    private Person person = null;

    private final String event_json = "{\n" +
            "\"descendant\":\"brody\",\n" +
            "\"eventID\":\"qhYad7dHK4D2at6n\",\n" +
            "\"personID\":\"Jwt9e2MheH0L4jXw\",\n" +
            "\"latitude\":44.75,\n" +
            "\"longitude\":19.7,\n" +
            "\"country\":\"Serbia\",\n" +
            "\"city\":\"Šabac\",\n" +
            "\"eventType\":\"birth\",\n" +
            "\"year\":\"1967\"\n" +
            "}";

    @Before
    public void setUp() throws Exception {
        final String personid = "AAAAAAAA";
        final String username = "testuser";
        final String firstname = "G";
        final String lastname = "Money";
        final String gender = "m";
        final String father = "BBBBBBBB";
        final String mother = "CCCCCCCC";
        final String spouse = "DDDDDDDD";

        person = new Person(personid, username, firstname, lastname, gender, father, mother, spouse);
    }

    @After
    public void tearDown() throws Exception {
        person = null;
    }


    @org.junit.Test
    public void getEntryList() throws Exception {
        List<Object> entries = person.getEntryList();
        assertTrue(person.getPersonID().equals(entries.get(0)));
        assertTrue(person.getDescendant().equals(entries.get(1)));
        assertTrue(person.getFirstname() == entries.get(2));
        assertTrue(event1.getLongitude() == (double) entries.get(4));
        assertTrue(event1.getCountry().equals((String) entries.get(5)));
    }

    @org.junit.Test
    public void EventTest() throws Exception {
        Event local = (Event) JSONEncoder.encoder.convertToObject(event_json, Event.class);
        assertTrue(local.equals(event1));
    }

}