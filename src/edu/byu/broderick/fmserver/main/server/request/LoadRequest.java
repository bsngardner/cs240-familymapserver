package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.util.List;

/**
 * Server Load request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class LoadRequest extends Request {

    List<User> users;

    List<Person> persons;

    List<Event> events;


    /**
     * Constructor
     * Creates request object form JSON string
     */
    public LoadRequest() {
        super();
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (users == null || persons == null || events == null)
            result = new ErrorResult("Bad request");
        return result;
    }


    public List<User> getUsers() {
        return users;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Person> getPersons() {
        return persons;
    }
}
