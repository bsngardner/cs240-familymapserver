package edu.byu.broderick.fmserver.main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model class containing information about an Event
 *
 * @author Broderick Gardner
 */
public class Event extends DataModel {

    private String eventID;
    private String username;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private String year;

    private int hashcode;
    private boolean hasHash = false;

    /**
     * Constructor
     *
     * @param eventid   Unique event identifier
     * @param username  Reference to User associated with event
     * @param personid  Person to whom event belongs
     * @param latitude  Latitude coordinate of event
     * @param longitude Longitude coordinate of event
     * @param country   Country of event
     * @param city      City of event
     * @param eventType Type of event (may later be enum)
     * @param year      Year event took place
     */
    public Event(String eventid, String username, String personid, double latitude, double longitude, String country, String city, String eventType, String year) {
        this(username, personid, latitude, longitude, country, city, eventType, year);
        this.eventID = eventid;
    }

    /**
     * New event constructor, does not take an event id. Assumes that the event id will be added later
     *
     * @param username
     * @param personid
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String username, String personid, double latitude, double longitude, String country, String city, String eventType, String year) {
        super();
        this.eventID = null;
        this.username = username;
        this.personID = personid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Copy constructor
     *
     * @param e
     */
    public Event(Event e) {
        super();
        this.eventID = e.eventID;
        this.username = e.username;
        this.personID = e.personID;
        this.latitude = e.latitude;
        this.longitude = e.longitude;
        this.country = e.country;
        this.city = e.city;
        this.eventType = e.eventType;
        this.year = e.year;
    }


    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<>();
        entries.add(eventID);
        entries.add(username);
        entries.add(personID);
        entries.add(latitude);
        entries.add(longitude);
        entries.add(country);
        entries.add(city);
        entries.add(eventType);
        entries.add(year);
        return entries;
    }


    public String getEventid() {
        return eventID;
    }

    public void setEventid(String eventid) {
        this.eventID = eventid;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public String getYear() {
        return year;
    }

    private void calculateHashCode() {
        int result;
        long temp;
        result = eventID.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + personID.hashCode();
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + country.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + eventType.hashCode();
        result = 31 * result + year.hashCode();
        hashcode = result;
    }

    @Override
    public int hashCode() {
        if (!hasHash) {
            calculateHashCode();
            hasHash = true;
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (Double.compare(event.latitude, latitude) != 0) return false;
        if (Double.compare(event.longitude, longitude) != 0) return false;
        if (!eventID.equals(event.eventID)) return false;
        if (!username.equals(event.username)) return false;
        if (!personID.equals(event.personID)) return false;
        if (!country.equals(event.country)) return false;
        if (!city.equals(event.city)) return false;
        if (!eventType.equals(event.eventType)) return false;
        return year.equals(event.year);
    }
}