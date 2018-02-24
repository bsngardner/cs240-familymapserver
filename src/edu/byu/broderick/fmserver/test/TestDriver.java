package edu.byu.broderick.fmserver.test;

/**
 * Created by broderick on 4/6/17.
 */
public class TestDriver {

    public static void main(String[] args){
        org.junit.runner.JUnitCore.main("database.EventDAOTest","database.PersonDAOTest","database.UserDAOTest",
                "model.EventTest","model.PersonTest","model.UserTest");
    }
}
