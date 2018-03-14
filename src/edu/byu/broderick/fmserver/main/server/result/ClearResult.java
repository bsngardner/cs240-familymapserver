package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Clear result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class ClearResult extends Result {

    private static final String SUCCESS_STRING = "Clear succeeded.";

    /**
     * Constructor
     * Creates result object from JSON string
     */
    public ClearResult() {
        super(SUCCESS_STRING);
    }

    @Override
    public boolean isError() {
        return !this.message.equals(SUCCESS_STRING);
    }

}
