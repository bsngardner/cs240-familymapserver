package edu.byu.broderick.fmserver.main.server.service;

import edu.byu.broderick.fmserver.main.database.Database;
import edu.byu.broderick.fmserver.main.server.request.ClearRequest;
import edu.byu.broderick.fmserver.main.server.result.ClearResult;
import edu.byu.broderick.fmserver.main.server.result.ErrorResult;
import edu.byu.broderick.fmserver.main.server.result.Result;

import java.sql.SQLException;

/**
 * Server clear call service.  Clears all data from the database.
 *
 * @author Broderick Gardner
 */
public class ClearService {

    /**
     * Constructor
     */
    public ClearService() {
        super();
    }

    /**
     * Dispatches this object to service the clear request
     *
     * @param request
     * @return
     */
    public Result service(ClearRequest request) {
        Result result;
        System.out.println("COMMAND: clear");
        try {
            Database.getDB().resetDatabase();
            result = new ClearResult();
            System.out.println("Succesfully cleared database");
        } catch (SQLException e) {
            e.printStackTrace();
            result = new ErrorResult("Internal server error");
            System.out.println("Failed: SQL exception");
        }

        return result;
    }
}
