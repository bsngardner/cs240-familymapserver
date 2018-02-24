package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Register result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class RegisterResult extends Result {

    private String authKey;

    private String userName;

    private String personId;


    /**
     * Constructor
     */
    public RegisterResult(String key, String userName, String personId) {
        super();
        this.authKey = key;
        this.userName = userName;
        this.personId = personId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonId() {
        return personId;
    }

    public String getAuthKey() {
        return authKey;
    }
}
