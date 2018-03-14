package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Register request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class RegisterRequest extends Request {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Constructor
     * Creates request object from JSON string
     */
    public RegisterRequest() {
        super();
    }

    public RegisterRequest(String username, String password, String email, String firstname, String lastname, String gender) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
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
        if (userName == null || password == null || email == null || firstName == null || lastName == null || !(gender.equals("m") || gender.equals("f")))
            result = new ErrorResult("Bad request");
        return result;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
