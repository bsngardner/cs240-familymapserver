package edu.byu.broderick.fmserver.main.model;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Data model class for a registered User
 * Contains nested AuthToken class
 *
 * @author Broderick Gardner
 */
public class User extends DataModel {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;
    private String info;

    /**
     * Constructor
     *
     * @param username  User's username
     * @param password  User's password
     * @param email     User's email
     * @param firstname User's first name
     * @param lastname  User's last name
     * @param gender    User's gender
     * @param personid  User's Person unique identifier
     * @param info      Historical information section
     */
    public User(String username, String password, String email, String firstname, String lastname, String gender, String personid, String info) {
        super();
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.personID = personid;
        this.info = info;
    }

    /**
     * Generate new auth token for user
     *
     * @return
     */
    public AuthToken authenticate() {
        return new AuthToken(this);
    }

    /**
     * Create list of object fields for insertion into database
     * @return
     */
    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<>();
        entries.add(userName);
        entries.add(password);
        entries.add(email);
        entries.add(firstName);
        entries.add(lastName);
        entries.add(gender);
        entries.add(personID);
        entries.add(info);
        return entries;
    }


    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    public String getInfo() {
        return info;
    }

    public void setPersonID(String personid) {
        this.personID = personid;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Data model class containing auth token for User transaction authentication
     *
     * @author Broderick Gardner
     */
    public static class AuthToken {

        private static final int TOK_LEN = 16;
        private static final char[] key_dict = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        private static Random rand = new Random();

        private String key;
        OffsetDateTime time;
        String username;

        /**
         * Constructor
         * Auth token value is generated here internally.
         */
        public AuthToken(User user) {
            this.time = OffsetDateTime.now();
            key = AuthToken.generateNewKey();
            this.username = user.getUsername();
        }

        /**
         * Constructor.
         * Create AuthToken object from existing token information
         *
         * @param user
         * @param key
         * @param time
         */
        public AuthToken(User user, String key, String time) {
            this.time = OffsetDateTime.parse(time);
            this.key = key;
        }

        /**
         * Check if token is expired by comparing timestamp with current time.  1 hour expiration
         *
         * @return
         */
        public boolean isExpired() {
            OffsetDateTime t = OffsetDateTime.now();
            t = t.minusHours(1);
            return this.time.isBefore(t);
        }

        /**
         * Private helper method for generating a new key for the token
         *
         * @return
         */
        private static String generateNewKey() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < TOK_LEN; i++)
                sb.append(key_dict[rand.nextInt(key_dict.length)]);
            return sb.toString();
        }

        /**
         * Getter
         *
         * @return
         */
        public String key() {
            return this.key;
        }

        /**
         * Setter
         *
         * @return
         */
        public String time() {
            return this.time.toString();
        }

        /**
         * Create list of fields for insertion into database
         *
         * @return
         */
        public List<Object> getEntryList() {
            List<Object> entries = new ArrayList<>();
            entries.add(key);
            entries.add(username);
            entries.add(time.toString());
            return entries;
        }
    }
}
