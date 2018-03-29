package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.RegisterRequest;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.RegisterResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server register call service.  Registers a user, logs the user in, and generates 4 generations of family data for the user.
 *
 * @author Broderick Gardner
 */
public class RegisterService {

    /**
     * Constructor
     */
    public RegisterService() {
        super();
    }


    /**
     * Dispatches this object to handle the register service request, and returns result
     *
     * @param request
     * @return
     */
    public Result service(RegisterRequest request) {

        Result result;
        Database db = Database.openDatabase();
        System.out.println("COMMAND: register");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            db.endTransaction(false);
            return result;
        }

        String username = request.getUserName();
        String password = request.getPassword();
        String email = request.getEmail();
        String firstname = request.getFirstName();
        String lastname = request.getLastName();
        String gender = request.getGender();
        User user = new User(username, password, email, firstname, lastname, gender, "", "");

        User user_check = db.userData.loadUser(username);
        if (user_check != null) {
            result = new ErrorResult("Username unavailable");
            System.out.println("Command failed due to username unavailable");
            db.endTransaction(false);
            return result;
        }
        db.userData.storeUser(user);
        User.AuthToken key = db.userData.authenticateUser(user);
        Services.fillService.fillUser(db, user, 4);

        result = new RegisterResult(key.key(), user.getUsername(), user.getPersonID());
        System.out.println("Command successful");
        db.endTransaction(true);
        return result;
    }
}
