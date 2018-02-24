package edu.byu.broderick.fmserver.main.server;

import com.sun.net.httpserver.HttpServer;
import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main server managing class
 *
 * @author Broderick Gardner
 */
public class Server {

    private static final int MAX_GENERATIONS = 10;
    private static final int DEFAULT_PORT = 8080;
    private static final int MAX_WAITING_CONNECTIONS = 10;
    private static final String HTML_PATH = "data/HTML";

    private HttpServer server;
    private File htmlFile;
    private Handlers handlers;
    private int port;

    /**
     * Constructor
     */
    public Server(int port_num) throws FileNotFoundException {
        this.port = port_num;
        handlers = new Handlers();
        htmlFile = new File(HTML_PATH);
        if (!htmlFile.exists())
            throw new FileNotFoundException();
    }

    public void run() {
        try {
            server = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), MAX_WAITING_CONNECTIONS);


        } catch (IOException e) {
            e.printStackTrace();
        }

        server.setExecutor(null);
        server.createContext("/clear", handlers.clearHandler);
        server.createContext("/fill", handlers.fillHandler);
        server.createContext("/person", handlers.personHandler);
        server.createContext("/event", handlers.eventHandler);
        server.createContext("/user/login", handlers.loginHandler);
        server.createContext("/user/register", handlers.registerHandler);
        server.createContext("/fill", handlers.fillHandler);
        server.createContext("/load", handlers.loadHandler);
        server.createContext("/", handlers.indexHandler);

        server.start();
    }

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token.
     *
     * @param request
     * @return
     */
    public RegisterResult register(RegisterRequest request) {
        return null;
    }

    /**
     * Logs in the user and returns an auth token.
     *
     * @param request
     * @return
     */
    public LoginResult login(LoginRequest request) {
        return null;
    }

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data.
     *
     * @param request
     * @return
     */
    public ClearResult clear(ClearRequest request) {
        return null;
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
        return null;
    }

    /**
     * Clears all data from the database (just like the /clear API), and then loads the
     * posted user, person, and event data into the database.
     *
     * @param request
     * @return
     */
    public LoadResult load(LoadRequest request) {
        return null;
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
        return null;
    }


}
