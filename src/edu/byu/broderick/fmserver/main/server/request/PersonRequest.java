package edu.byu.broderick.fmserver.main.server.request;


import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Person request object. This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class PersonRequest extends Request {

    private String authKey;

    private String personid;

    /**
     * Constructor
     *
     * @param key
     * @param personid
     */
    public PersonRequest(String key, String personid) {
        super();
        this.authKey = key;
        this.personid = personid;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getPersonid() {
        return personid;
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (authKey == null)
            result = new ErrorResult("Bad request");
        return result;
    }
}
