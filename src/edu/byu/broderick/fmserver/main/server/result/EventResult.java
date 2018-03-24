package edu.byu.broderick.fmserver.main.server.result;

import edu.byu.broderick.fmserver.main.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Server Event result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class EventResult extends Result {

    /**
     * Constructor
     */
    public EventResult() {
        super();
    }

    /**
     * Factory method for creating a single event result object
     *
     * @param event
     * @return
     */
    public static EventResult createSingleEvent(Event event) {
        return new SingleEvent(event);
    }

    /**
     * Factory method for creating an all events result object
     *
     * @param events
     * @return
     */
    public static EventResult createAllEvents(List<Event> events) {
        return new AllEvents(events);
    }

    /**
     * EventResult inner class for storing event information
     */
    public static class SingleEvent extends EventResult {

        private String descendant;
        private String eventID;
        private String personID;
        private double latitude;
        private double longitude;
        private String country;
        private String city;
        private String eventType;
        private String year;

        private SingleEvent(Event event) {
            super();
            this.descendant = event.getUsername();
            this.eventID = event.getEventid();
            this.personID = event.getPersonID();
            this.latitude = event.getLatitude();
            this.longitude = event.getLongitude();
            this.country = event.getCountry();
            this.city = event.getCity();
            this.eventType = event.getEventType();
            this.year = event.getYear();
        }

        public Event getEvent() {
            Event event = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
            return event;
        }
    }

    /**
     * EventResult inner class for storing multiple events' information; useful for converting to JSON
     */
    public static class AllEvents extends EventResult {

        List<SingleEvent> data;

        private AllEvents(List<Event> events) {
            super();
            data = new ArrayList<>();
            for (Event event : events) {
                data.add(new SingleEvent(event));
            }
        }

        public int getEventCount() {
            return data.size();
        }

        public List<Event> getEvents() {
            List<Event> events = new ArrayList<>();
            for(SingleEvent e : data){
                events.add(e.getEvent());
            }
            return events;
        }
    }

}