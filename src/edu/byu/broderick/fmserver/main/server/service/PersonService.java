package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.Person;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.PersonRequest;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.PersonResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.util.List;

/**
 * Server person call service.  Returns either a single person or all persons associated with the user.
 *
 * @author Broderick Gardner
 */
public class PersonService {

    /**
     * Constructor
     */
    public PersonService() {
        super();
    }


    /**
     * Dispatches this object to handle the person service request, and returns result
     *
     * @param request
     * @return
     */
    public Result service(PersonRequest request) {

        Result result;
        Database db = Database.getDB();
        System.out.println("COMMAND: persons");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            return result;
        }

        User user = Database.getDB().userData.loadUserFromAuthKey(request.getAuthKey());
        if (user == null) {
            result = new ErrorResult("Invalid Auth Token");
            System.out.println("Command failed due to invalid auth token");
            return result;
        }

        String personid = request.getPersonid();
        if (personid == null) {
            List<Person> persons = Database.getDB().personData.loadUserPersons(user);
            result = new PersonResult.AllPersons(persons);
        } else {
            Person person = Database.getDB().personData.loadPerson(user, personid);
            if (person == null) {
                result = new ErrorResult("Person not found");
                System.out.println("Person not found");
                return result;
            }
            result = new PersonResult.SinglePerson(person);
        }

        System.out.println("Command successful");
        return result;
    }
}
