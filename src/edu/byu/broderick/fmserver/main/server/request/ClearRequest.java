package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Clear request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class ClearRequest extends Request {

    /**
     * Constructor
     */
    public ClearRequest() {
        super();
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        return null;
    }

}
