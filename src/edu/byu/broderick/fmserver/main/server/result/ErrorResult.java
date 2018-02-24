package edu.byu.broderick.fmserver.main.server.result;

/**
 * Generic result object for errors, extends Result so that it can be returned in place of a successful result object
 *
 * @author
 */
public class ErrorResult extends Result {

    private String message;

    /**
     * Constructor.
     *
     * @param s Message describing error
     */
    public ErrorResult(String s) {
        message = s;
    }
}
