package edu.byu.broderick.fmserver.main.server.request;


import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Event request object. This is sent from the client and received by the server
 *
 * @author Broderick Gardner
 */
public class EventRequest extends Request {

    private String authKey;

    private String eventid;

    /**
     * Constructor
     *
     * @param eventid
     */
    public EventRequest(String key, String eventid) {
        super();
        this.authKey = key;
        this.eventid = eventid;
    }

    public String getAuthKey() {
        return authKey;
    }

    public String getEventid() {
        return eventid;
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (authKey == null) {
            result = new ErrorResult("Bad request");
        }
        return result;
    }
}

