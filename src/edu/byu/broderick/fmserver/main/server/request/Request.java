package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Super class for server Request objects
 *
 * @author Broderick Gardner
 */
public abstract class Request {

    /**
     * Super class constructor
     */
    public Request() {

    }

    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    public abstract Result checkRequest();
}
