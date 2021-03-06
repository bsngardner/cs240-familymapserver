package edu.byu.broderick.fmserver.main.server.result;

import edu.byu.broderick.fmserver.main.model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Server Person result object.  This is sent as a response from the server and received by the client.
 *
 * @author Broderick Gardner
 */
public class PersonResult extends Result {

    /**
     * Constructor
     */
    public PersonResult() {
        super();

    }

    public static class SinglePerson extends PersonResult {

        private String descendant;
        private String personID;
        private String firstName;
        private String lastName;
        private String gender;
        private String father;
        private String mother;
        private String spouse;

        public SinglePerson(Person person) {
            this.descendant = person.getDescendant();
            this.personID = person.getPersonID();
            this.firstName = person.getFirstname();
            this.lastName = person.getLastname();
            this.gender = person.getGender();
            this.father = person.getFather();
            this.mother = person.getMother();
            this.spouse = person.getSpouse();
        }

    }

    public static class AllPersons extends PersonResult {

        private List<SinglePerson> data;

        public AllPersons(List<Person> persons) {
            data = new ArrayList<>();
            for (Person person : persons) {
                data.add(new SinglePerson(person));
            }
        }

        public int getPersonCount() {
            return data.size();
        }
    }
}
