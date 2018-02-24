package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Fill result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class FillResult extends Result {

    private String message;

    /**
     * Constructor
     */
    public FillResult(int numPersons, int numEvents) {
        super();
        StringBuilder b = new StringBuilder();
        b.append("Successfully added ");
        b.append(numPersons);
        b.append(" persons and ");
        b.append(numEvents);
        b.append(" events to the database.");
        message = b.toString();
    }

}
