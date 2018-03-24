package edu.byu.broderick.fmserver.test;

import edu.byu.broderick.fmserver.main.ServerProxy;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.*;
import edu.byu.broderick.fmserver.main.server.serialize.SerialCodec;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for server and server proxy. Tests all server functionality, replacing unit tests for Service classes
 * Important: This is why I don't have Service test cases; that is done here, via server calls.
 *
 * @author Broderick Gardner
 */
public class ServerProxyTest {

    final String username = "testuser";
    final String password = "test1234";
    final String email = "test@test.info";
    final String firstname = "test";
    final String lastname = "user";
    final String gender = "m";
    ServerProxy server;

    @Before
    public void setUp() throws Exception {
        server = new ServerProxy(ProxyTestDriver.server_url);
        server.clear();
    }

    @After
    public void tearDown() throws Exception {
        server.clear();
        server = null;
    }

    public void testBasic(){

    }

    @Test
    public void registerTest() throws Exception {

        RegisterRequest req = new RegisterRequest(username, password, email, firstname, lastname, gender);
        Result result = server.register(req);
        //Test if register attempt was successful
        assertFalse(result.isError());
        RegisterResult regResult = (RegisterResult) result;
        assertTrue(regResult.getUserName().equals(username)); //Result username is same as attempt username
        assertTrue(regResult.getPersonId() != null && !regResult.getPersonId().equals("")); //Non empty personID
        assertTrue(regResult.getAuthorization() != null && !regResult.getAuthorization().equals("")); //Non empty auth key
    }

    @Test
    public void loginTest() throws Exception {

        Result result;
        result = server.register(new RegisterRequest(username, password, email, firstname, lastname, gender));
        assertFalse(result.isError()); //Make sure register is successful

        result = server.login(new LoginRequest(username, password));
        assertFalse(result.isError()); //Check if attempt was successful
        LoginResult loginResult = (LoginResult) result;
        assertTrue(loginResult.getUserName().equals(username));
        assertTrue(loginResult.getPersonId() != null && !loginResult.getPersonId().equals("")); //Non empty person id
        assertTrue(loginResult.getAuthKey() != null && !loginResult.getAuthKey().equals("")); //Non empty auth key

        result = server.login(new LoginRequest("Billy", "Bob"));
        assertTrue(result.isError()); //Login attempt should result in error
    }

    @Test
    public void fillTest() throws Exception {
        Result result;
        result = server.register(new RegisterRequest(username, password, email, firstname, lastname, gender));
        String key = ((RegisterResult) result).getAuthorization();
        result = server.fill(new FillRequest(username, 4));
        assertTrue(!result.isError());
        FillResult fillResult = (FillResult) result;
        //Make sure the full number of events and people were added.  I can't think of a reasonable way to test things
        // like event order
        assertTrue(result.getMessage().contains("31 persons"));

        result = server.event(new EventRequest(key, null));
        assertFalse(result.isError()); //No error in result
        assertTrue(result instanceof EventResult.AllEvents); //The result object is as expected
        EventResult.AllEvents eResult = (EventResult.AllEvents) result;
        assertTrue(eResult.getEventCount() > 0); //Some events were generated
    }

    @Test
    public void clearTest() throws Exception {
        Result result;
        result = server.register(new RegisterRequest(username, password, email, firstname, lastname, gender));
        assertFalse(result.isError());
        result = server.clear();
        assertFalse(result.isError());

        result = server.login(new LoginRequest(username, password));
        assertTrue(result.isError()); //Make sure login attempt is a fail after clear
    }

    @Test
    public void loadTest() throws Exception {
        Result result;

        String jsonString = streamToString(new FileInputStream("data/json/example.json"));
        LoadRequest req = (LoadRequest) SerialCodec.inst.deserialize(jsonString, LoadRequest.class);
        result = server.load(req);
        assertFalse(result.isError()); //Load is successful

        result = server.login(new LoginRequest("sheila", "parker"));
        assertFalse(result.isError()); //Login should be successful

        LoginResult loginResult = (LoginResult) result;
        assertTrue(loginResult.getUserName().equals("sheila"));

        String key = loginResult.getAuthKey();
        result = server.person(new PersonRequest(key, null));
        assertFalse(result.isError());
        assertTrue(result instanceof PersonResult.AllPersons);
        PersonResult.AllPersons pResult = (PersonResult.AllPersons) result;
        assertEquals(3, pResult.getPersonCount()); //In the example, there are 3 persons to add
    }

    @Test
    public void personTest() throws Exception {
        Result result;
        RegisterResult reg;

        //Make 2 users and fill them
        reg = server.register(new RegisterRequest(username, password, email, firstname, lastname, gender));
        assertFalse(reg.isError());
        String key1 = reg.getAuthorization();
        assertFalse(server.fill(new FillRequest(username, 2))
                .isError());

        reg = server.register(new RegisterRequest("billy", "password", email, firstname, lastname, gender));
        assertFalse(reg.isError());
        String key2 = reg.getAuthorization();
        assertFalse(server.fill(new FillRequest(username, 2))
                .isError());

        //Fetch people info for the two users, make sure they don't intersect
        PersonResult.AllPersons pResult = (PersonResult.AllPersons) server.person(new PersonRequest(key1, null));
        Set<Person> persons1 = new HashSet<>(pResult.getPersons());
        pResult = (PersonResult.AllPersons) server.person(new PersonRequest(key2, null));
        Set<Person> persons2 = new HashSet<>(pResult.getPersons());
        Set<Person> intersect = new HashSet<>(persons1);
        intersect.retainAll(persons2);
        assertTrue(intersect.size() == 0); //Event sets should not have anything in the intersect

        //Grab a person from user1, make sure it fetches via ID, make sure user2 can't fetch it
        Person test_person = persons1.iterator().next();
        PersonResult.SinglePerson pr = (PersonResult.SinglePerson) server.person(new PersonRequest(key1, test_person.getPersonID()));
        assertFalse(pr.isError());
        Person p1 = pr.getPerson();
        assertTrue(p1.equals(test_person));

        pr = (PersonResult.SinglePerson) server.person(new PersonRequest(key2, test_person.getPersonID()));
        assertTrue(pr.isError());
    }

    @Test
    public void eventTest() throws Exception {
        Result result;
        RegisterResult reg;

        reg = server.register(new RegisterRequest(username, password, email, firstname, lastname, gender));
        assertFalse(reg.isError());
        String key1 = reg.getAuthorization();
        assertFalse(server.fill(new FillRequest(username, 2))
                .isError());

        reg = server.register(new RegisterRequest("billy", "password", email, firstname, lastname, gender));
        assertFalse(reg.isError());
        String key2 = reg.getAuthorization();
        assertFalse(server.fill(new FillRequest(username, 2))
                .isError());

        //Pull events for the two users, see if they intersect at all
        EventResult.AllEvents eResult = (EventResult.AllEvents) server.event(new EventRequest(key1, null));
        Set<Event> events1 = new HashSet<>(eResult.getEvents());
        eResult = (EventResult.AllEvents) server.event(new EventRequest(key2, null));
        Set<Event> events2 = new HashSet<>(eResult.getEvents());
        Set<Event> intersect = new HashSet<>(events1);
        intersect.retainAll(events2);
        assertTrue(intersect.size() == 0); //Event sets should  not have anything in the intersect

        //Pick an event, make sure it fetches from one user, make sure the other can't get it
        Event test_event = events1.iterator().next();
        EventResult.SingleEvent er = (EventResult.SingleEvent) server.event(new EventRequest(key1, test_event.getEventid()));
        assertFalse(er.isError());
        Event e1 = er.getEvent();
        assertTrue(e1.equals(test_event));

        er = (EventResult.SingleEvent) server.event(new EventRequest(key2, test_event.getEventid()));
        assertTrue(er.isError());

    }


    private String streamToString(InputStream input) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}

