package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Super class for server Request objects
 *
 * @author Broderick Gardner
 */
public abstract class Request {

    /**
     * Super class constructor
     */
    public Request() {

    }

    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    public abstract Result checkRequest();


    public class RUser {
        private String userName;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String gender;
        private String personID;
        private String info;

        public User getUser(){
            return new User(userName, password, email, firstName, lastName, gender,
                    personID, info);
        }
    }

    public class REvent {
        private String eventType;
        private String eventID;
        private String personID;
        private String city;
        private String country;
        private double latitude;
        private double longitude;
        private int year;
        private String descendant;

        public Event getEvent(){
            return new Event(eventID, descendant, personID, latitude,
                    longitude, country, city, eventType, Integer.toString(year));
        }
    }

    public class RPerson {
        private String firstName;
        private String lastName;
        private String gender;
        private String personID;
        private String descendant;
        private String father;
        private String mother;
        private String spouse;

        public Person getPerson() {
            return new Person(personID, descendant, firstName, lastName, gender, father,
                    mother, spouse);
        }
    }

}
