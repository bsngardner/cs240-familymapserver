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


}
