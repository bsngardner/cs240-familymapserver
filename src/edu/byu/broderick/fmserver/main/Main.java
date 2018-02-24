package edu.byu.broderick.fmserver.main;

import edu.byu.broderick.fmserver.main.server.Server;

import java.io.FileNotFoundException;

/**
 * Contains main method
 */
public class Main {


    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Invalid number of arguments.  Must have port number");
            return;
        }

        int port_num = Integer.parseInt(args[0]);

        Server server = null;
        try {
            server = new Server(port_num);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (server != null)
            server.run();
        else
            System.out.println("File Exception");
    }
}
