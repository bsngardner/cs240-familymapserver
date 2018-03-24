package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Server Load request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class LoadRequest extends Request {

    private List<Request.RUser> users;

    private List<Request.RPerson> persons;

    private List<Request.REvent> events;


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
        List<User> new_users = new ArrayList<>();
        for (Request.RUser u : this.users) {
            new_users.add(u.getUser());
        }
        return new_users;
    }

    public List<Event> getEvents() {
        List<Event> new_events = new ArrayList<>();
        for (Request.REvent e : events) {
            new_events.add(e.getEvent());
        }
        return new_events;
    }

    public List<Person> getPersons() {
        List<Person> new_persons = new ArrayList<>();
        for(Request.RPerson p : persons){
            new_persons.add(p.getPerson());
        }
        return new_persons;
    }

}
