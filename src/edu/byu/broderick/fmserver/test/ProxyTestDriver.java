package edu.byu.broderick.fmserver.test;

/**
 * Created by broderick on 4/6/17.
 */
public class ProxyTestDriver {

    public static String server_url = "";

    public static void main(String[] args) {
        server_url = args[0];
        org.junit.runner.JUnitCore.main("edu.byu.broderick.fmserver.test.ServerProxyTest");
    }
}
