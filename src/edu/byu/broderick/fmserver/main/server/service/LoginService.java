package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.model.User;
import edu.byu.broderick.fmserver.main.server.request.LoginRequest;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.LoginResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server login call service.  Log user into the server, returning the authentication token for requests made during
 * the login session
 *
 * @author Broderick Gardner
 */
public class LoginService {

    /**
     * Constructor
     */
    public LoginService() {
        super();
    }


    /**
     * Dispatches this object to handle the login service request, and returns result
     *
     * @param request
     * @return
     */
    public Result login(LoginRequest request) {

        Result result;
        Database db = Database.getDB();
        System.out.println("COMMAND: login");

        if ((result = request.checkRequest()) != null) {
            System.out.println("Command failed due to bad request");
            return result;
        }

        String username = request.getUserName();
        String password = request.getPassword();

        User user = db.userData.loadUser(username);
        if (user == null) {
            result = new ErrorResult("Invalid username or password");
            return result;
        }

        if (password.equals(user.getPassword())) {
            String key = Database.getDB().userData.authenticateUser(user).key();
            result = new LoginResult(key, user.getUserName(), user.getPersonID());
        } else {
            result = new ErrorResult("Invalid username or password");
        }

        return result;
    }
}
