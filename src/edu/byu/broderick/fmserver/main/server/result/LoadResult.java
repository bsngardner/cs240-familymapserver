package edu.byu.broderick.fmserver.main.server.result;

/**
 * Server Load result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class LoadResult extends Result {

    private String message;

    /**
     * Constructor
     */
    public LoadResult(int numUsers, int numPersons, int numEvents) {
        super();
        StringBuilder b = new StringBuilder();
        b.append("Successfully added ");
        b.append(numUsers);
        b.append(" users, ");
        b.append(numPersons);
        b.append(" persons, and ");
        b.append(numEvents);
        b.append(" events to the database.");
        this.message = b.toString();
    }
}
