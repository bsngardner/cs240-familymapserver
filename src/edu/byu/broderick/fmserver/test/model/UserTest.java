package edu.byu.broderick.fmserver.test.model;

import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.serialize.SerialCodec;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for the User model class
 *
 * @author Broderick Gardner
 */
public class UserTest {


    private final String person_json = SerialCodec.inst.serialize(
            new User("AAAAAAAA", "testuser",
                    "G", "Money", "m",
                    "BBBBBBBB", "CCCCCCCC", "DDDDDDDD"));
    private User user = null;

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

        user = new User(personid, username, firstname, lastname, gender, father, mother, spouse);
    }

    @After
    public void tearDown() throws Exception {

    }


    @org.junit.Test
    public void getEntryList() throws Exception {
        List<Object> entries = user.getEntryList();
        assertTrue(user.getUserName().equals(entries.get(0)));
        assertTrue(user.getPassword().equals(entries.get(1)));
        assertTrue(user.getEmail().equals(entries.get(2)));
        assertTrue(user.getFirstname().equals(entries.get(3)));
        assertTrue(user.getLastname().equals(entries.get(4)));
        assertTrue(user.getGender().equals(entries.get(5)));
        assertTrue(user.getPersonID().equals(entries.get(6)));
        assertTrue(user.getInfo().equals(entries.get(7)));
    }

    @org.junit.Test
    public void UserTest() throws Exception {
        User local = (User) SerialCodec.inst.deserialize(person_json, User.class);
        assertTrue(local.equals(user));
    }

}