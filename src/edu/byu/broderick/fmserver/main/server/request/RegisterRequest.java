package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Register request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class RegisterRequest extends Request {

    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String gender;

    /**
     * Constructor
     * Creates request object from JSON string
     */
    public RegisterRequest() {
        super();
    }

    public RegisterRequest(String username, String password, String email, String firstname, String lastname, String gender){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (username == null || password == null || email == null || firstname == null || lastname == null || !(gender.equals("m") || gender.equals("f")))
            result = new ErrorResult("Bad request");
        return result;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }
}
