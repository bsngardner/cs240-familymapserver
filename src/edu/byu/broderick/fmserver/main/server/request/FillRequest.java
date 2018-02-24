package edu.byu.broderick.fmserver.main.server.request;

import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

/**
 * Server Fill request object.  This is sent by the client and received by the server.
 *
 * @author Broderick Gardner
 */
public class FillRequest extends Request {

    private String username;

    private int generations;

    /**
     * Constructor
     *
     * @param username
     * @param generations
     */
    public FillRequest(String username, int generations) {
        super();
        this.username = username;
        this.generations = generations;
    }


    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }


    /**
     * Request common method implemented to error-check the contents of the request object.  Returns null for no errors.
     *
     * @return
     */
    @Override
    public Result checkRequest() {
        Result result = null;
        if (username == null || generations < 0)
            result = new ErrorResult("Bad request");
        return result;
    }
}
