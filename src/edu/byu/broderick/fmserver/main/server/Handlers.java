package edu.byu.broderick.fmserver.main.server;

import edu.byu.broderick.fmserver.main.server.request.*;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;
import edu.byu.broderick.fmserver.main.server.service.Services;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

/**
 * Container class for Handler objects.  Handlers convert from JSON to a server request object and from a response
 * object back to JSON.  They also perform some minimal parameter error checking where possible.
 *
 * @author Broderick Gardner
 */
public class Handlers {

    private JSONEncoder enc = new JSONEncoder();

    /**
     * Handler object for server root access to html index.  Returns main test page for server.
     */
    public HttpHandler indexHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String cmd = httpExchange.getRequestURI().toString();
            String[] params = cmd.split("/", 2);
            String target = "index.html";

            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

            OutputStreamWriter writer = new OutputStreamWriter(httpExchange.getResponseBody());
            String filepath = "data/HTML/" + target;
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
            }
            reader.close();
            writer.close();
        }
    };

    /**
     * Handler object for register requests
     */
    public HttpHandler registerHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String body = streamToString(httpExchange.getRequestBody());
            RegisterRequest req = (RegisterRequest) enc.convertToObject(body, RegisterRequest.class);
            if (req == null) {
                sendResponse(httpExchange, new ErrorResult("Invalid register request"));
            } else {
                Result result = Services.registerService.service(req);
                sendResponse(httpExchange, result);
            }
        }
    };

    /**
     * Handler object for login requests
     */
    public HttpHandler loginHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String body = streamToString(httpExchange.getRequestBody());
            LoginRequest req = (LoginRequest) enc.convertToObject(body, LoginRequest.class);
            if (req == null) {
                sendResponse(httpExchange, new ErrorResult("Invalid Login Request"));
            } else {
                Result result = Services.loginService.login(req);
                sendResponse(httpExchange, result);
            }
        }
    };

    /**
     * Handler object for clear requests
     */
    public HttpHandler clearHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            ClearRequest req = new ClearRequest(); //TODO There is no data in the request, delete?
            Result result = Services.clearService.service(req);
            sendResponse(httpExchange, result);
        }
    };

    /**
     * Handler object for fill requests
     */
    public HttpHandler fillHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String cmd = httpExchange.getRequestURI().toString();
            String[] params = cmd.split("/");
            if (params.length < 3 || params.length > 4) {
                sendResponse(httpExchange, new ErrorResult("Invalid fill URI"));
                return;
            }
            int generations = (params.length == 4) ? Integer.parseInt(params[3]) : 4;
            String username = params[2];
            FillRequest req = new FillRequest(username, generations);
            Result result = Services.fillService.service(req);
            sendResponse(httpExchange, result);
        }
    };

    /**
     * Handler object for load requests
     */
    public HttpHandler loadHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String body = streamToString(httpExchange.getRequestBody());
            LoadRequest req = (LoadRequest) enc.convertToObject(body, LoadRequest.class);
            if (req == null) {
                sendResponse(httpExchange, new ErrorResult("Invalid load request"));
            } else {
                System.out.println("Handling load request");
                Result result = Services.loadService.service(req);
                System.out.println("Responding to load request");
                sendResponse(httpExchange, result);
            }
        }
    };

    /**
     * Handler object for event requests
     */
    public HttpHandler eventHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String key = httpExchange.getRequestHeaders().get("Authorization").get(0);
            String cmd = httpExchange.getRequestURI().toString();
            String[] params = cmd.split("/");
            if (params.length < 2 || params.length > 3) {
                sendResponse(httpExchange, new ErrorResult("Invalid event URI"));
                return;
            }
            String eventid = (params.length == 3) ? params[2] : null;
            EventRequest req = new EventRequest(key, eventid);
            Result result = Services.eventService.service(req);
            sendResponse(httpExchange, result);
        }
    };

    /**
     * Handler object for person requests
     */
    public HttpHandler personHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String key = httpExchange.getRequestHeaders().get("Authorization").get(0);
            String cmd = httpExchange.getRequestURI().toString();
            String[] params = cmd.split("/");
            if (params.length < 2 || params.length > 3) {
                sendResponse(httpExchange, new ErrorResult("Invalid person URI"));
                return;
            }
            String personid = (params.length == 3) ? params[2] : null;
            PersonRequest req = new PersonRequest(key, personid);
            Result result = Services.personService.service(req);
            sendResponse(httpExchange, result);
        }
    };

    public Handlers() {

    }


    /**
     * Helper method for sending http responses
     *
     * @param httpExchange
     * @param result
     */
    private void sendResponse(HttpExchange httpExchange, Result result) {
        OutputStreamWriter writer = null;
        try {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            //httpExchange.setStatus(HttpURLConnection.HTTP_OK);
            writer = new OutputStreamWriter(httpExchange.getResponseBody(), Charset.forName("UTF-8"));
            String json = enc.convertToJSON(result);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method for converting a stream to a string
     *
     * @param input
     * @return
     * @throws IOException
     */
    private String streamToString(InputStream input) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

}
