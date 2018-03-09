package edu.byu.broderick.fmserver.test.model;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.server.serialize.JSONEncoder;
import edu.byu.broderick.fmserver.main.server.serialize.SerialCodec;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by broderick on 3/22/17.
 */
public class EventTest {

    private Event event1 = null;

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

    @org.junit.Before
    public void setUp() throws Exception {
        final String descendant = "brody";
        final String eventID = "qhYad7dHK4D2at6n";
        final String personID = "Jwt9e2MheH0L4jXw";
        final double latitude = 44.75;
        final double longitude = 19.7;
        final String country = "Serbia";
        final String city = "Šabac";
        final String eventType = "birth";
        final String year = "1967";

        event1 = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        event1 = null;
    }

    @org.junit.Test
    public void getEntryList() throws Exception {
        List<Object> entries = event1.getEntryList();
        assertTrue(event1.getEventid().equals(entries.get(0)));
        assertTrue(event1.getPersonID().equals((String) entries.get(2)));
        assertTrue(event1.getLatitude() == ((double) entries.get(3)));
        assertTrue(event1.getLongitude() == (double) entries.get(4));
        assertTrue(event1.getCountry().equals((String) entries.get(5)));
        assertTrue(event1.getCity().equals(entries.get(6)));
        assertTrue(event1.getEventType().equals(entries.get(7)));
        assertTrue(event1.getYear().equals(entries.get(8)));
    }

    @org.junit.Test
    public void EventTest() throws Exception {
        Event local = (Event) SerialCodec.inst.deserialize(event_json, Event.class);
        assertTrue(local.equals(event1));
    }

}