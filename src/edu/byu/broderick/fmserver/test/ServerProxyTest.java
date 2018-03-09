package edu.byu.broderick.fmserver.test;

import edu.byu.broderick.fmserver.main.ServerProxy;
import edu.byu.broderick.fmserver.main.server.json.JSONEncoder;
import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by broderick on 3/22/17.
 */
public class ServerProxyTest {

    ServerProxy server;

    final String username = "testuser";
    final String password = "test1234";
    final String email = "test@test.info";
    final String firstname = "test";
    final String lastname = "user";
    final String gender = "m";

    @Before
    public void setUp() throws Exception {
        server = new ServerProxy(ProxyTestDriver.server_url);
        this.server.clear(null);
    }

    @After
    public void tearDown() throws Exception {
        this.server.clear(null);
        server = null;
    }

    @Test
    public void testAllServerCalls() throws Exception{
        register();
        login();
        fill();
        person();
        event();
        load();
        clear();
    }

    public void register() throws Exception {

        RegisterRequest req = new RegisterRequest(username, password, email, firstname, lastname, gender);
        Result result = this.server.register(req);
        assertTrue(!result.isError());
        RegisterResult regResult = (RegisterResult) result;
        assertTrue(regResult.getUserName().equals(username));
        assertTrue(regResult.getPersonId() != null && !regResult.getPersonId().equals(""));
        assertTrue(regResult.getAuthKey() != null && !regResult.getAuthKey().equals(""));
    }

    public void login() throws Exception {

        Result result = this.server.login(new LoginRequest(username, password));
        assertTrue(!result.isError());
        LoginResult loginResult = (LoginResult) result;
        assertTrue(loginResult.getUserName().equals(username));
        assertTrue(loginResult.getPersonId() != null && !loginResult.getPersonId().equals(""));
        assertTrue(loginResult.getAuthKey() != null && !loginResult.getAuthKey().equals(""));
    }

    public void fill() throws Exception {
        Result result = this.server.fill(new FillRequest(username, 4));
        assertTrue(!result.isError());
        FillResult fillResult = (FillResult) result;
        assertTrue(fillResult.getMessage().contains("Successfully"));
    }

    public void clear() throws Exception {
        Result result = this.server.clear(null);
        assertTrue(!result.isError());
        assertTrue(result.getMessage().equals("Clear succeeded."));
    }

    public void load() throws Exception {

        String jsonString = streamToString(new FileInputStream("data/json/example.json"));
        LoadRequest req = (LoadRequest) JSONEncoder.encoder.convertToObject(jsonString,LoadRequest.class);
        Result result = this.server.load(req);
        assertTrue(!result.isError());
        result = this.server.login(new LoginRequest("sheila","parker"));
        assertTrue(result.getMessage() == null);
        LoginResult loginResult = (LoginResult) result;
        assertTrue(loginResult.getUserName().equals("sheila"));
    }

    public void person() throws Exception {
        LoginResult login = this.server.login(new LoginRequest(username,password));
        Result result = this.server.person(new PersonRequest(login.getAuthKey(),null));
        assertTrue(!result.isError());
        PersonResult personResult = (PersonResult) result;
        assertTrue(personResult instanceof PersonResult.AllPersons);
        PersonResult.AllPersons allPersons = (PersonResult.AllPersons) personResult;
        assertTrue(allPersons.getPersonCount() == 31);
    }

    public void event() throws Exception {
        LoginResult login = this.server.login(new LoginRequest(username,password));
        Result result = this.server.event(new EventRequest(login.getAuthKey(),null));
        assertTrue(!result.isError());
        EventResult eventResult= (EventResult) result;
        assertTrue(eventResult instanceof EventResult.AllEvents);
        EventResult.AllEvents allEvents = (EventResult.AllEvents) eventResult;
        assertTrue(allEvents.getEventCount() > 0);
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

