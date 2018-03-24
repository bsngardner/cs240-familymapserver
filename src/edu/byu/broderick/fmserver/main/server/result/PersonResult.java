package edu.byu.broderick.fmserver.main.server.result;

import edu.byu.broderick.fmserver.main.model.Person;

import java.lang.reflect.Array;
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
            this.descendant = person.getUsername();
            this.personID = person.getPersonID();
            this.firstName = person.getFirstname();
            this.lastName = person.getLastname();
            this.gender = person.getGender();
            this.father = person.getFather();
            this.mother = person.getMother();
            this.spouse = person.getSpouse();
        }

        public Person getPerson(){
            Person p = new Person(personID, descendant, firstName, lastName, gender, father, mother, spouse);
            return p;
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

        public List<Person> getPersons(){
            List<Person> persons = new ArrayList<>();
            for(SinglePerson p : data){
                persons.add(p.getPerson());
            }
            return persons;
        }

        public int getPersonCount() {
            return data.size();
        }
    }
}
