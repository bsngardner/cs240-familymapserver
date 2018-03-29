package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.EventRequest;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.EventResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.util.List;

/**
 * Server event call service.  Returns either a single event or all the events associated with a user
 *
 * @author Broderick Gardner
 */
public class EventService {

    /**
     * Constructor
     */
    public EventService() {
        super();
    }

    /**
     * Dispatches this object to handle the event service request, and returns result
     *
     * @param request
     * @return
     */
    public Result service(EventRequest request) {
        Result result;
        Database db = Database.openDatabase();
        boolean success = true;
        System.out.println("COMMAND: event");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            return result;
        }

        String authKey = request.getAuthKey();

        db.startTransaction();
        User user = db.userData.loadUserFromAuthKey(authKey);
        if (user == null) {
            result = new ErrorResult("Invalid Auth token");
            System.out.println("Command failed due to invalid auth token");
            db.endTransaction(false);
            return result;
        }
        String eventid;
        if ((eventid = request.getEventid()) != null) {
            Event event = db.eventData.loadEvent(user, eventid);
            if (event == null) {
                result = new ErrorResult("Invalid event id parameter");
                System.out.println("Command failed due to invalid event id parameter");
                success = false;
            } else {
                result = EventResult.createSingleEvent(event);
                System.out.println("Command successful");
            }
        } else {
            List<Event> events = db.eventData.loadUserEvents(user);
            result = EventResult.createAllEvents(events);
            System.out.println("Command successful");
        }
        db.endTransaction(success);

        return result;
    }
}