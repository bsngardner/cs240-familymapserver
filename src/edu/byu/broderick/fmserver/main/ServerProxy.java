package edu.byu.broderick.fmserver.main;

import edu.byu.broderick.fmserver.main.server.JSONEncoder;
import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.*;
import edu.byu.broderick.fmserver.main.httpclient.HttpClient;
import edu.byu.broderick.fmserver.main.httpclient.HttpResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class contains methods called by the client to issue requests to the server.
 *
 * @author Broderick Gardner
 */
public class ServerProxy {

    private static final String JSON_TYPE = "application/json";
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    HttpClient client;
    JSONEncoder enc;

    /**
     * Constructor
     */
    public ServerProxy(String serverURL) {
        client = new HttpClient(serverURL);
        enc = new JSONEncoder();
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     *
     * @param request
     * @return
     */
    public RegisterResult register(RegisterRequest request) {
        String json = enc.convertToJSON(request);
        HttpResponse response = client.doPost("/user/register", JSON_TYPE, json.getBytes());
        RegisterResult result = (RegisterResult) enc.convertToObject(new String(response.getBody(), UTF8), RegisterResult.class);
        return result;
    }

    /**
     * Logs in the user and returns an auth token.
     *
     * @param request
     * @return
     */
    public LoginResult login(LoginRequest request) {
        String json = enc.convertToJSON(request);
        HttpResponse response = client.doPost("/user/login", JSON_TYPE, json.getBytes());
        LoginResult result = (LoginResult) enc.convertToObject(new String(response.getBody(), UTF8), LoginResult.class);
        return result;
    }

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data.
     *
     * @param request
     * @return
     */
    public ClearResult clear(ClearRequest request) {
        HttpResponse response = client.doPost("/clear", null, null);
        ClearResult result = (ClearResult) enc.convertToObject(new String(response.getBody(), UTF8), ClearResult.class);
        return result;
    }

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional “generations” parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     *
     * @param request
     * @return
     */
    public FillResult fill(FillRequest request) {
        String path = "/event/" + request.getUsername();
        if (request.getGenerations() >= 0) {
            path += "/" + request.getGenerations();
        }
        HttpResponse response = client.doPost(path, null, null);
        FillResult result = (FillResult) enc.convertToObject(new String(response.getBody(), UTF8), FillResult.class);
        return result;
    }

    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     *
     * @param request
     * @return
     */
    public LoadResult load(LoadRequest request) {
        String json = enc.convertToJSON(request);
        HttpResponse response = client.doPost("/load", JSON_TYPE, json.getBytes());
        LoadResult result = (LoadResult) enc.convertToObject(new String(response.getBody(), UTF8), LoadResult.class);
        return result;
    }

    /**
     * Returns the single Person object with the specified ID.
     * <p>
     * or if no ID specified,
     * <p>
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     *
     * @param request
     * @return
     */
    public PersonResult person(PersonRequest request) {
        String path = "/person";
        if (request.getPersonid() != null) {
            path += "/" + request.getPersonid();
        }
        String authKey = request.getAuthKey();
        client.clearHeaders();
        client.addHeader("Authorization", authKey);
        HttpResponse response = client.doGet(path, null, null);
        return null;
    }

    /**
     * Returns the single Event object with the specified ID.
     * <p>
     * or if no ID specified,
     * <p>
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     *
     * @param request
     * @return
     */
    public EventResult event(EventRequest request) {
        String path = "/event";
        if (request.getEventid() != null) {
            path += "/" + request.getEventid();
        }
        String authKey = request.getAuthKey();
        client.clearHeaders();
        client.addHeader("Authorization", authKey);
        HttpResponse response = client.doGet(path, null, null);
        return null;
    }

}
