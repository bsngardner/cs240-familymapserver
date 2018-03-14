package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Register result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class RegisterResult extends Result {

    private String Authorization;

    private String userName;

    private String personId;


    /**
     * Constructor
     */
    public RegisterResult(String key, String userName, String personId) {
        super();
        this.Authorization = key;
        this.userName = userName;
        this.personId = personId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAuthorization() {
        return Authorization;
    }
}
