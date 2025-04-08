/**
 * @author Noah Vickerson
 * Person.java 
 * @version 1.1
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.sql.*;

public class Person implements DatabaseInterfaceable {
    protected String firstName;
    protected String lastName;
    protected String birthDate;
    protected String gender;
    protected String comments;
    protected String phoneNum;

    protected FamilyGroup familyGroup;
    protected final String TYPE;
    protected final int id;
    protected static int counter = 0;

    /**
     * Constructor
     * @param firstName
     * @param lastName
     * @param birthDate must be in the format YYYY-MM-DD
     * @param gender must be male, female, or non-binary
     * @param phoneNum must be in the format XXX-XXX-XXXX or XXX-XXXX
     * @param type must be either inquirer or disastervictim
     * @throws IllegalArgumentException if above not followed
     */
    public Person(String firstName, String lastName, String birthDate, String gender, String phoneNum, String type) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("inv_date");
        }
        if(!isValidPhoneNum(phoneNum)){
            throw new IllegalArgumentException("inv_phone");
        }
        if(!isValidGender(gender)){
            throw new IllegalArgumentException("inv_gender");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.TYPE = type;
        this.familyGroup = null;
        counter++;
        this.id = counter;
    }

    /**
     * Constructor without type parameter
     * @param firstName
     * @param lastName
     * @param birthDate must be in the format YYYY-MM-DD
     * @param gender must be male, female, or non-binary
     * @param phoneNum must be in the format XXX-XXX-XXXX or XXX-XXXX
     * @throws IllegalArgumentException if above not followed
     */
    public Person(String firstName, String lastName, String birthDate, String gender, String phoneNum) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("inv_date");
        }
        if(!isValidPhoneNum(phoneNum)){
            throw new IllegalArgumentException("inv_phone");
        }
        if(!isValidGender(gender)){
            throw new IllegalArgumentException("inv_gender");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.familyGroup = null;
        this.TYPE = "inquirer";
        counter++;
        this.id = counter;
    }

    /**
     * Constructor with id
     * @param id        must be unique
     * @param firstName
     * @param lastName
     * @param birthDate must be in the format YYYY-MM-DD
     * @param gender must be male, female, or non-binary
     * @param phoneNum must be in the format XXX-XXX-XXXX or XXX-XXXX
     * @param type must be either inquirer or disastervictim
     * @throws IllegalArgumentException if above not followed
     */
    public Person(int id, String firstName, String lastName, String birthDate, String gender, String phoneNum, String type) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("repeat_id");
		}
        
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("inv_date");
        }
        if(!isValidPhoneNum(phoneNum)){
            throw new IllegalArgumentException("inv_phone");
        }
        if(!isValidGender(gender)){
            throw new IllegalArgumentException("inv_gender");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.TYPE = type;
        this.familyGroup = null;
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    /**
     * Constructor without type parameter with id
     * @param id        must be unique
     * @param firstName
     * @param lastName
     * @param birthDate must be in the format YYYY-MM-DD
     * @param gender must be male, female, or non-binary
     * @param phoneNum must be in the format XXX-XXX-XXXX or XXX-XXXX
     * @throws IllegalArgumentException if above not followed
     */
    public Person(int id, String firstName, String lastName, String birthDate, String gender, String phoneNum) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("repeat_id");
		}
        
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("inv_date");
        }
        if(!isValidPhoneNum(phoneNum)){
            throw new IllegalArgumentException("inv_phone");
        }
        if(!isValidGender(gender)){
            throw new IllegalArgumentException("inv_gender");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.familyGroup = null;
        this.TYPE = "inquirer";
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    /**
     * Returns the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the comments
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the comments
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Returns the date of birth
     * @return date of birth
     */
    public String getDateOfBirth() {
        return birthDate;
    }

    /**
     * Sets the date of birth
     * @param birthDate must be in the format YYYY-MM-DD
     * @throws IllegalArgumentException if above not followed
     */
    public void setDateOfBirth(String birthDate) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("inv_date");
        }
        this.birthDate = birthDate;
    }

    /**
     * Returns the type
     * @return type
     */
    public String getType() {
        return TYPE;
    }

    /**
     * Returns the gender
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender
     * @param gender must be male, female, or non-binary
     * @throws IllegalArgumentException if above not followed
     */
    public void setGender(String gender) throws IllegalArgumentException {
        if (!isValidGender(gender)) {
            throw new IllegalArgumentException("inv_gender");
        }
        this.gender = gender;
    }

    /**
     * Returns the phone number
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNum;
    }

    /**
     * Sets the phone number
     * @param phoneNum must be in the format XXX-XXX-XXXX or XXX-XXXX
     * @throws IllegalArgumentException if above not followed
     */
    public void setPhoneNumber(String phoneNum) throws IllegalArgumentException {

        this.phoneNum = phoneNum;
    }

    /**
     * Adds a family member
     * @param familyMember
     * @throws IllegalArgumentException if family member already has a family group
     */
    public void addFamilyMember(Person familyMember) throws IllegalArgumentException {
        if (familyGroup == null) {
            if (familyMember.getFamilyGroup() != null) {
                familyMember.addFamilyMember(this);
                this.familyGroup = familyMember.getFamilyGroup();
            } else {
                familyGroup = new FamilyGroup(this, familyMember);
            }
        } else {
            if (familyMember.getFamilyGroup() != null) {
                throw new IllegalArgumentException("inv_family");
            }
            familyMember.setFamilyGroup(familyGroup);
        }

    }

    /**
     * Returns the family group
     * @return family group
     */
    public FamilyGroup getFamilyGroup() {
        return familyGroup;
    }

    /**
     * Sets the family group
     * @param familyGroup
     */
    public void setFamilyGroup(FamilyGroup familyGroup) {
        if(this.familyGroup == familyGroup) {
            return;
        }
        if(this.familyGroup != null) {
            this.familyGroup.removeFamilyMember(this);
        }
        if (familyGroup == null) {
            this.familyGroup = null;
            return;
        }

        this.familyGroup = familyGroup;
        familyGroup.addFamilyMember(this);
    }

    /**
     * Checks if the date is in the format YYYY-MM-DD
     * @param date
     * @return true if valid
     */
    protected static boolean isValidDateFormat(String date) {
        if(date == null) {
            return true;
        }

        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || date.equalsIgnoreCase("null");
    }

    /**
     * Checks if the gender is valid
     * @param gender
     * @return true if valid
     */
    private static boolean isValidGender(String gender) {
        if(gender == null) {
            return true;
        }

        gender = gender.toLowerCase();
        if (gender.equals("male") || gender.equals("female") || gender.equals("non_binary") || gender.equalsIgnoreCase("null") || gender.equals("woman") || gender.equals("man")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the phone number is valid
     * @param phoneNum
     * @return true if valid
     */
    private static boolean isValidPhoneNum(String phoneNum) {
        if(phoneNum == null) {
            return true;
        }

        return phoneNum.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$") || phoneNum.equalsIgnoreCase("null") || phoneNum.matches("^[0-9]{3}-[0-9]{4}$");
    }

    /**
     * Returns the id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Adds an entry to the database
     * @throws SQLException
     */
    public void addEntry() throws SQLException {
        if(this.familyGroup == null) {
            String query = "INSERT INTO Person (first_name, last_name, date_of_birth, gender, comments, phone_number, family_group) VALUES (?, ?, ?, ?, ?, ?, NULL)";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum};
            String[] types = {"string", "string", "date", "string", "string", "string"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }else{
            String query = "INSERT INTO Person (first_name, last_name, date_of_birth, gender, comments, phone_number, family_group) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(familyGroup.getId())};
            String[] types = {"string", "string", "date", "string", "string", "string", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }
    }

    /**
     * Updates an entry in the database
     * @throws SQLException
     */
    public void updateEntry() throws SQLException {
        if (this.familyGroup == null) {
            String query = "UPDATE Person SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = NULL WHERE person_id = ?";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(id)};
            String[] types = {"string", "string", "string", "string", "string", "string", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }else{
            String query = "UPDATE Person SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = ? WHERE person_id = ?";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(familyGroup.getId()), String.valueOf(id)};
            String[] types = {"string", "string", "string", "string", "string", "string", "int", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }
    }
}
