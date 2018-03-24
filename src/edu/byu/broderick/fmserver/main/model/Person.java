package edu.byu.broderick.fmserver.main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model class containing information about a Person
 *
 * @author Broderick Gardner
 */
public class Person extends DataModel {

    public String personID;
    private String username;
    private String firstname;
    private String lastname;
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
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public Person(Person p) {
        this(p.personID, p.username, p.firstname, p.lastname, p.gender, p.father, p.mother, p.spouse);
    }


    /**
     * Returns all fields in an ordered list
     *
     * @return
     */
    public List<Object> getEntryList() {
        List<Object> entries = new ArrayList<Object>();
        entries.add(personID);
        entries.add(username);
        entries.add(firstname);
        entries.add(lastname);
        entries.add(gender);
        entries.add(father);
        entries.add(mother);
        entries.add(spouse);

        return entries;
    }

    public String getPersonID() {
        return personID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public void setPersonid(String personid) {
        this.personID = personid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!username.equals(person.username)) return false;
        if (!personID.equals(person.personID)) return false;
        if (!firstname.equals(person.firstname)) return false;
        if (!lastname.equals(person.lastname)) return false;
        if (!gender.equals(person.gender)) return false;
        if (father != null ? !father.equals(person.father) : person.father != null) return false;
        if (mother != null ? !mother.equals(person.mother) : person.mother != null) return false;
        return spouse != null ? spouse.equals(person.spouse) : person.spouse == null;
    }

    private void computeHashCode() {
        int result = username.hashCode();
        result = 31 * result + personID.hashCode();
        result = 31 * result + firstname.hashCode();
        result = 31 * result + lastname.hashCode();
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
