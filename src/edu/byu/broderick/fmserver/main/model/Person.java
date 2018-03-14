package edu.byu.broderick.fmserver.main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model class containing information about a Person
 *
 * @author Broderick Gardner
 */
public class Person extends DataModel {

    private String descendant;
    public String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    private boolean hasHash = false;
    private int hashcode;

    /**
     * Constructor
     *
     * @param personid  Unique Person identifier
     * @param username  User with which Person is associated
     * @param firstname First name of Person
     * @param lastname  Last name of Person
     * @param gender    Gender of Person
     * @param father    Reference to father of Person
     * @param mother    Reference to Mother of Person
     * @param spouse    Reference to spouse of Person
     */
    public Person(String personid, String username, String firstname, String lastname, String gender, String father, String mother, String spouse) {
        this(username, firstname, lastname, gender, father, mother, spouse);
        this.personID = personid;
    }

    /**
     * Constructor
     *
     * @param username  User with which Person is associated
     * @param firstname First name of Person
     * @param lastname  Last name of Person
     * @param gender    Gender of Person
     * @param father    Reference to father of Person
     * @param mother    Reference to Mother of Person
     * @param spouse    Reference to spouse of Person
     */
    public Person(String username, String firstname, String lastname, String gender, String father, String mother, String spouse) {
        super();
        this.descendant = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public Person(Person p) {
        this(p.personID, p.descendant, p.firstName, p.lastName, p.gender, p.father, p.mother, p.spouse);
    }


    /**
     * Returns all fields in an ordered list
     *
     * @return
     */
    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<Object>();
        entries.add(personID);
        entries.add(descendant);
        entries.add(firstName);
        entries.add(lastName);
        entries.add(gender);
        entries.add(father);
        entries.add(mother);
        entries.add(spouse);

        return entries;
    }

    public String getPersonID() {
        return personID;
    }

    public String getDescendant() {
        return descendant;
    }


    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setPersonid(String personid) {
        this.personID = personid;
    }

    public void setDescendant(String username) {
        this.descendant = username;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!descendant.equals(person.descendant)) return false;
        if (!personID.equals(person.personID)) return false;
        if (!firstName.equals(person.firstName)) return false;
        if (!lastName.equals(person.lastName)) return false;
        if (!gender.equals(person.gender)) return false;
        if (father != null ? !father.equals(person.father) : person.father != null) return false;
        if (mother != null ? !mother.equals(person.mother) : person.mother != null) return false;
        return spouse != null ? spouse.equals(person.spouse) : person.spouse == null;
    }

    private void computeHashCode() {
        int result = descendant.hashCode();
        result = 31 * result + personID.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (father != null ? father.hashCode() : 0);
        result = 31 * result + (mother != null ? mother.hashCode() : 0);
        result = 31 * result + (spouse != null ? spouse.hashCode() : 0);
        hashcode = result;
    }

    @Override
    public int hashCode() {
        if (!hasHash) {
            computeHashCode();
            hasHash = true;
        }
        return hashcode;
    }
}
