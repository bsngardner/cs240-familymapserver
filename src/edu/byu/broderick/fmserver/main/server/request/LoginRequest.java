package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Login request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class LoginRequest extends Request {

    private String userName;

    private String password;

    /**
     * Constructor
     */
    public LoginRequest() {
        super();
    }

    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (userName == null || password == null)
            result = new ErrorResult("Bad request");
        return result;
    }

    public String getuserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
