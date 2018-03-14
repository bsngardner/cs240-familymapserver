package edu.byu.broderick.fmserver.main.database;

import edu.byu.broderick.fmserver.main.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by broderick on 3/7/17.
 */
public class UserDAO {

    private static final String USER_TABLE = "main.users";
    private static final List<String> USER_COLUMNS = Arrays.asList("username", "password", "email", "firstname", "lastname", "gender", "personid", "info");

    private static final String AUTH_TABLE = "main.authkeys";
    private static final List<String> AUTH_COLUMNS = Arrays.asList("key", "userid", "time");

    private final Database db;

    /**
     * Constructor
     */
    public UserDAO(Database db) {
        this.db = db;
    }

    /**
     * Stores User in database
     *
     * @param user
     */
    public boolean storeUser(User user) {
        return this.db.insert(USER_TABLE, USER_COLUMNS, user.getEntryList());
    }

    public User.AuthToken authenticateUser(User user) {
        User.AuthToken key = user.authenticate();
        this.db.insert(AUTH_TABLE, AUTH_COLUMNS, key.getEntryList());
        return key;
    }

    public User loadUserFromAuthKey(String key) {
        List<List<Object>> records = this.db.select(AUTH_TABLE, null, "key", key);
        if (records.size() < 1)
            return null;
        List<Object> record = records.get(0);
        String username = (String) record.get(1);
        User user = loadUser(username);
        User.AuthToken tok = new User.AuthToken(user, (String) record.get(0), (String) record.get(2));
        if (tok.isExpired()) {
            deleteKey(tok);
            return null;
        }
        return user;
    }

    private void deleteKey(User.AuthToken token) {
        this.db.delete(AUTH_TABLE, "key", token.key());
    }

    /**
     * Loads User from database
     *
     * @return
     */
    public User loadUser(String username) {

        List<List<Object>> records = this.db.select(USER_TABLE, null, "username", username);
        if (records.size() < 1)
            return null;
        List<Object> record = records.get(0);
        return userFromRecord(record);
    }

    private User userFromRecord(List<Object> record) {
        String uname = (String) record.get(0);
        String password = (String) record.get(1);
        String email = (String) record.get(2);
        String firstname = (String) record.get(3);
        String lastname = (String) record.get(4);
        String gender = (String) record.get(5);
        String personid = (String) record.get(6);
        String info = (String) record.get(7);

        return new User(uname, password, email, firstname, lastname, gender, personid, info);
    }

    public void updateUser(User user) {
        this.db.update(USER_TABLE, USER_COLUMNS, user.getEntryList(), "username", user.getUserName());
    }

    public void deleteUsers() {
        db.delete(USER_TABLE, null, null);
    }
}
