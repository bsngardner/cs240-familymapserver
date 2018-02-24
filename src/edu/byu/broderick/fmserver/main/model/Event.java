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
    private String descendant;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String eventType;
    private String year;

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

    public Event(String username, String personid, double latitude, double longitude, String country, String city, String eventType, String year) {
        super();
        this.eventID = null;
        this.descendant = username;
        this.personID = personid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<>();
        entries.add(eventID);
        entries.add(descendant);
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

    public String getUsername() {
        return descendant;
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

    public void setEventid(String eventid) {
        this.eventID = eventid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event))
            return false;

        Event e = (Event) o;

        if (!eventID.equals(e.eventID))
            return false;
        if (!descendant.equals(e.descendant))
            return false;
        if (!personID.equals(e.personID))
            return false;
        if (latitude != e.latitude)
            return false;
        if (longitude != e.longitude)
            return false;
        if (!country.equals(e.country))
            return false;
        if (!city.equals(e.city))
            return false;
        if (!eventType.equals(e.eventType))
            return false;
        if (!year.equals(e.year))
            return false;
        return true;
    }
}