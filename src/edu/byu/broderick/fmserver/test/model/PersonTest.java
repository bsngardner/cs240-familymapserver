package edu.byu.broderick.fmserver.test.model;

import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.server.serialize.JSONEncoder;
import edu.byu.broderick.fmserver.main.server.serialize.SerialCodec;
import org.junit.After;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for the Person model class
 *
 * @author Broderick Gardner
 */
public class PersonTest {

    private Person person = null;
    private final String person_json = JSONEncoder.inst.serialize(
            new Person("AAAAAAAA", "testuser",
                    "G", "Money", "m",
                    "BBBBBBBB", "CCCCCCCC", "DDDDDDDD"));

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

        person = new Person(personid, username, firstname, lastname, gender, father, mother, spouse);
    }

    @After
    public void tearDown() throws Exception {
        person = null;
    }

    /**
     * Check the order of fields in the entry list by comparing via order in the list and
     *  the getters
     *
     * @throws Exception
     */
    @org.junit.Test
    public void getEntryList() throws Exception {
        List<Object> entries = person.getEntryList();
        assertTrue(person.getPersonID().equals(entries.get(0)));
        assertTrue(person.getDescendant().equals(entries.get(1)));
        assertTrue(person.getFirstname().equals(entries.get(2)));
        assertTrue(person.getLastname().equals(entries.get(3)));
        assertTrue(person.getGender().equals(entries.get(4)));
        assertTrue(person.getFather().equals(entries.get(5)));
        assertTrue(person.getMother().equals(entries.get(6)));
        assertTrue(person.getSpouse().equals(entries.get(7)));
    }

    /**
     * Just test that
     *
     * @throws Exception
     */
    @org.junit.Test
    public void EventTest() throws Exception {
        Person local = (Person) SerialCodec.inst.deserialize(person_json, Person.class);
        assertTrue(local.equals(person));
    }

}