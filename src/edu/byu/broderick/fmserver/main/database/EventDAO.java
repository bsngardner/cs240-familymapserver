package edu.byu.broderick.fmserver.main.database;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;

import java.util.*;

/**
 * Event Data Access Object
 *
 * @author Broderick Gardner
 */
public class EventDAO {

    private static final String EVENT_TABLE = "main.events";
    private static final List<String> EVENT_COLUMNS = Arrays.asList("eventid", "userid", "personid", "latitude", "longitude", "country", "city", "eventType", "year");

    private final Database db;

    private Set<String> uids = null;

    /**
     * Constructor
     */
    public EventDAO(Database db) {
        this.db = db;
    }

    /**
     * Store new event object in database, generates unique id for the event as well
     *
     * @param event
     */
    public void storeNewEvent(Event event) {

        if (uids == null) {
            uids = new TreeSet<>();
            List<List<Object>> records = this.db.select(EVENT_TABLE, Arrays.asList("eventid"), null, null);
            for (List<Object> record : records) {
                uids.add((String) record.get(0));
            }
        }

        String uid;
        do {
            uid = this.db.generateID();
        } while (uids.contains(uid));
        event.setEventid(uid);

        storeEvent(event);
    }

    /**
     * Stores Event in database, uses given event id.  Should be unique.
     *
     * @param event
     */
    public void storeEvent(Event event) {
        this.db.insert(EVENT_TABLE, EVENT_COLUMNS, event.getEntryList());
    }

    /**
     * Stores vector of events in database
     *
     * @param events
     */
    public void storeEvents(Vector<Event> events) {
        for (Event event : events) {
            storeEvent(event);
        }
    }

    /**
     * Loads all events associated with user from database
     *
     * @param user
     * @return
     */
    public List<Event> loadUserEvents(User user) {
        List<Event> events = new ArrayList<>();
        List<List<Object>> records = this.db.select(EVENT_TABLE, EVENT_COLUMNS, "userid", user.getUserName());
        for (List<Object> record : records) {
            events.add(eventFromRecord(record));
        }
        return events;
    }

    /**
     * loads all events associated with Person from database
     *
     * @param person
     * @return
     */
    public List<Event> loadPersonEvents(Person person) {
        List<Event> events = new ArrayList<>();
        List<List<Object>> records = this.db.select(EVENT_TABLE, EVENT_COLUMNS, "personid", person.getPersonID());
        for (List<Object> record : records) {
            events.add(eventFromRecord(record));
        }
        return events;
    }

    /**
     * Load event with associated id from database
     *
     * @param id
     * @return
     */
    public Event loadEvent(User user, String id) {
        Event event = null;
        List<List<Object>> records = this.db.select(EVENT_TABLE, EVENT_COLUMNS, "eventid", id);

        if (records.size() > 0) {
            event = eventFromRecord(records.get(0));
            if (!event.getUsername().equals(user.getUserName()))
                event = null;
        }

        return event;
    }

    /**
     * Delete all user events from database
     *
     * @param user
     */
    public void deleteUserEvents(User user) {
        String username = user.getUserName();
        this.db.delete(EVENT_TABLE, "userid", username);
    }

    /**
     * Private helper method for making an Event object from database record
     *
     * @param record
     * @return
     */
    private Event eventFromRecord(List<Object> record) {
        String eventid = (String) record.get(0);
        String username = (String) record.get(1);
        String personid = (String) record.get(2);
        double latitude = (Double) record.get(3);
        double longitude = (Double) record.get(4);
        String country = (String) record.get(5);
        String city = (String) record.get(6);
        String eventType = (String) record.get(7);
        String year = (String) record.get(8);

        return new Event(eventid, username, personid, latitude, longitude, country, city, eventType, year);
    }

    /**
     * Delete all events in database
     */
    public void deleteEvents() {
        db.delete(EVENT_TABLE, null, null);
    }
}
