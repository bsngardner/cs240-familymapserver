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
    private static final String HTML_PATH = "data/web";

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

        System.out.println("Server started");

        server.start();
    }

}
