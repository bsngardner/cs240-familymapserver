package edu.byu.broderick.fmserver.test;

/**
 * Test driver for all local unit tests (no HTTP server calls)
 *
 * Important: Service class tests are bundled with Server Proxy tests. It tests all server
 *  functionality, so a TA said it was okay.
 *
 * @author Broderick Gardner
 */
public class TestDriver {

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("edu.byu.broderick.fmserver.test.database.EventDAOTest",
                "edu.byu.broderick.fmserver.test.database.PersonDAOTest",
                "edu.byu.broderick.fmserver.test.database.UserDAOTest",
                "edu.byu.broderick.fmserver.test.model.EventTest",
                "edu.byu.broderick.fmserver.test.model.PersonTest",
                "edu.byu.broderick.fmserver.test.model.UserTest");
    }
}
