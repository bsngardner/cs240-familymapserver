package edu.byu.broderick.fmserver.main.model;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

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
    private String firstname;
    private String lastname;
    private String gender;
    private String personID;
    private String info;

    private boolean hasHash = false;
    private int hashcode;

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
        this.firstname = firstname;
        this.lastname = lastname;
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
     *
     * @return
     */
    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<>();
        entries.add(userName);
        entries.add(password);
        entries.add(email);
        entries.add(firstname);
        entries.add(lastname);
        entries.add(gender);
        entries.add(personID);
        entries.add(info);
        return entries;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personid) {
        this.personID = personid;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!userName.equals(user.userName)) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        if (!firstname.equals(user.firstname)) return false;
        if (!lastname.equals(user.lastname)) return false;
        if (!gender.equals(user.gender)) return false;
        if (!personID.equals(user.personID)) return false;
        return info.equals(user.info);
    }

    private void computeHashCode() {
        int result = userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + personID.hashCode();
        result = 31 * result + info.hashCode();
        hashcode = result;
    }

    @Override
    public int hashCode() {
        if (!hasHash) {
            computeHashCode();
            hasHash = true;
        }
        return hashcode;
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
        OffsetDateTime time;
        String username;
        private String key;

        /**
         * Constructor
         * Auth token value is generated here internally.
         */
        public AuthToken(User user) {
            this.time = OffsetDateTime.now();
            key = AuthToken.generateNewKey();
            this.username = user.getUserName();
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
         * Private helper method for generating a new key for the token
         *
         * @return
         */
        private static String generateNewKey() {
            final int NUM_BITS = 32;
            BigInteger key = new BigInteger(NUM_BITS, new SecureRandom());
            String keyString = new String(Base64.getEncoder().encode(key.toByteArray()));
            return keyString;
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
