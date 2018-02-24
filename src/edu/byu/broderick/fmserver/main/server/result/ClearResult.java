package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Clear result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class ClearResult extends Result {

    private String message = "Clear succeeded.";

    /**
     * Constructor
     * Creates result object from JSON string
     *
     */
    public ClearResult() {
        super();
    }
}
