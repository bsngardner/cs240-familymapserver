package edu.byu.broderick.fmserver.test;

/**
 * Created by broderick on 4/6/17.
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
