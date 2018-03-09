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
        message = null;
    }

    public Result(String msg){
        this.message = msg;
    }

    public String getMessage(){
        return message;
    }

    /**
     * Returns whether or not the result object represents an error.  Subclasses that use the message field will
     * override this method to implement error detection for that Result type
     *
     * @return
     */
    public boolean isError(){
        return (this instanceof ErrorResult) || this.message != null;
    }

}
