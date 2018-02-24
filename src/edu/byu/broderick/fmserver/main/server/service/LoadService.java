package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Event;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.LoadRequest;
import edu.byu.broderick.fmserver.main.server.result.LoadResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.util.List;

/**
 * Server load call service.  Loads provided data into the database, erasing all preexisting data.  This includes users, events, and persons.
 *
 * @author Broderick Gardner
 */
public class LoadService {

    /**
     * Constructor
     */
    public LoadService() {
        super();
    }


    /**
     * Dispatches this object to handle the load service request, and returns result
     *
     * @param request
     * @return
     */
    public Result service(LoadRequest request) {

        Result result;
        Database db = Database.getDB();
        System.out.println("COMMAND: load");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            return result;
        }

        db.userData.deleteUsers();
        db.eventData.deleteEvents();
        db.personData.deletePersons();

        List<User> users = request.getUsers();
        List<Event> events = request.getEvents();
        List<Person> persons = request.getPersons();

        for (User user : users) {
            user.setInfo("");
            db.userData.storeUser(user);
        }

        for (Person person : persons) {
            db.personData.storePerson(person);
        }

        for (Event event : events) {
            db.eventData.storeEvent(event);
        }

        result = new LoadResult(users.size(), persons.size(), events.size());

        System.out.println("Command successful");

        return result;
    }
}
