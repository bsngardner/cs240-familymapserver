package edu.byu.broderick.fmserver.main.server.result;

/**
 * Abstract super class for Result objects, allowing individualized successful results or common error results to be
 * returned in a single object.
 *
 * @author Broderick Gardner
 */
public abstract class Result {

    String message;

    /**
     * Super class constructor
     */
    public Result() {

    }

    public String getMessage(){
        return message;
    }

}
