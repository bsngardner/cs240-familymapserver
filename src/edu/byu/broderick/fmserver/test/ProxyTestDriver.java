package edu.byu.broderick.fmserver.test;

/**
 * Proxy test driver; assumes server is running at url with port passed in as argument.
 *
 * @author Broderick Gardner
 */
public class ProxyTestDriver {

    public static String server_url = "";

    public static void main(String[] args) {
        server_url = args[0];
        org.junit.runner.JUnitCore.main("edu.byu.broderick.fmserver.test.ServerProxyTest");
    }
}
