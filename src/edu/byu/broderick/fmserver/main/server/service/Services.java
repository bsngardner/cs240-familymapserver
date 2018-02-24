package edu.byu.broderick.fmserver.main.server.service;

/**
 * Container class for static service objects for handling service requests.  Services accept a request object as a
 * parameter and return a result object.  Services are called by the Handlers.
 */
public class Services {
    public static RegisterService registerService = new RegisterService();
    public static LoginService loginService = new LoginService();
    public static ClearService clearService = new ClearService();
    public static FillService fillService = new FillService();
    public static LoadService loadService = new LoadService();
    public static PersonService personService = new PersonService();
    public static EventService eventService = new EventService();
}
