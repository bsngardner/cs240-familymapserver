package edu.byu.broderick.fmserver.main;

import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.*;
import edu.byu.broderick.fmserver.main.server.serialize.JSONEncoder;
import edu.byu.broderick.fmserver.main.server.serialize.SerialCodec;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class contains methods called by the client to issue requests to the server.
 *
 * @author Broderick Gardner
 */
public class ServerProxy {

    private static final String JSON_TYPE = "application/serialize";
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    URL url;

    JSONEncoder enc;

    /**
     * Constructor
     */
    public ServerProxy(String serverURL) {
        try {
            url = new URL(serverURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     *
     * @param request
     * @return
     */
    public RegisterResult register(RegisterRequest request) {
        String body = SerialCodec.inst.serialize(request);
        String response = executeServerPost("/user/register", body);
        RegisterResult result = (RegisterResult) SerialCodec.inst.deserialize(response, RegisterResult.class);
        return result;
    }

    /**
     * Logs in the user and returns an auth token.
     *
     * @param request
     * @return
     */
    public LoginResult login(LoginRequest request) {
        String body = SerialCodec.inst.serialize(request);
        String response = executeServerPost("/user/login", body);
        LoginResult result = (LoginResult) SerialCodec.inst.deserialize(response, LoginResult.class);
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
        String response = executeServerPost("/clear", null);
        ClearResult result = (ClearResult) SerialCodec.inst.deserialize(response, ClearResult.class);
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
        String path = "/fill/" + request.getUsername();
        if (request.getGenerations() >= 0) {
            path += "/" + request.getGenerations();
        }
        String response = executeServerPost(path, null);
        FillResult result = (FillResult) SerialCodec.inst.deserialize(response, FillResult.class);
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
        String body = SerialCodec.inst.serialize(request);
        String response = executeServerPost("/load", body);
        LoadResult result = (LoadResult) SerialCodec.inst.deserialize(response, LoadResult.class);
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
        String response = executeServerGet(path, authKey);
        PersonResult result = (PersonResult) SerialCodec.inst.deserialize(response, PersonResult.class);
        return result;
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
        String response = executeServerGet(path, authKey);
        EventResult result = (EventResult) SerialCodec.inst.deserialize(response, EventResult.class);
        return result;
    }

    private String executeServerGet(String path, String authkey) {
        String response = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet get = new HttpGet(new URL(url, path).toURI());
            if (authkey != null) {
                get.setHeader("Authorization", authkey);
            }

            ResponseHandler<String> responseHandler = response1 -> {
                //int status = response.getStatusLine().getStatusCode();
                HttpEntity entity = response1.getEntity();
                return (entity != null) ? (EntityUtils.toString(entity)) : (null);
            };

            response = client.execute(get, responseHandler);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String executeServerPost(String path, String body) {
        String response = null;

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost post = new HttpPost(new URL(url, path).toURI());
            if (body != null) {
                StringEntity postBody = new StringEntity(body, Charset.defaultCharset());
                post.setEntity(postBody);
            }

            ResponseHandler<String> responseHandler = response1 -> {
                //int status = response.getStatusLine().getStatusCode();
                HttpEntity entity = response1.getEntity();
                return (entity != null) ? (EntityUtils.toString(entity)) : (null);
            };

            response = client.execute(post, responseHandler);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return response;
    }


}
