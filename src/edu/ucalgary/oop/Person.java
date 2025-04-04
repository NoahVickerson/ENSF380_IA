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

    public Person(String firstName, String lastName, String birthDate, String gender, String phoneNum, String type) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("Invalid date format: " + birthDate);
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.TYPE = type;
        this.familyGroup = null;
        this.id = counter++;
    }

    public Person(String firstName, String lastName, String birthDate, String gender, String phoneNum) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("Invalid date format: " + birthDate);
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.familyGroup = null;
        this.TYPE = "inquirer";
        this.id = counter++;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return birthDate;
    }

    public void setDateOfBirth(String birthDate) throws IllegalArgumentException {
        if (!isValidDateFormat(birthDate)) {
            throw new IllegalArgumentException("Invalid date format: " + birthDate);
        }
        this.birthDate = birthDate;
    }

    public String getType() {
        return TYPE;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) throws IllegalArgumentException {
        if (!isValidGender(gender)) {
            throw new IllegalArgumentException("Invalid gender: " + gender);
        }
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNum;
    }

    public void setPhoneNumber(String phoneNum) throws IllegalArgumentException {

        this.phoneNum = phoneNum;
    }

    public void addFamilyMember(Person familyMember) throws IllegalArgumentException {
        if (familyGroup == null) {
            if (familyMember.getFamilyGroup() != null) {
                familyMember.addFamilyMember(this);
                this.familyGroup = familyMember.getFamilyGroup();
            } else {
                familyGroup = new FamilyGroup(this, familyMember);
            }
        } else {
            familyGroup.addFamilyMember(familyMember);
        }

    }

    public FamilyGroup getFamilyGroup() {
        return familyGroup;
    }

    public void setFamilyGroup(FamilyGroup familyGroup) {
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

    protected static boolean isValidDateFormat(String date) {
        if(date == null) {
            return true;
        }

        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || date.equalsIgnoreCase("null");
    }

    private static boolean isValidGender(String gender) {
        if(gender == null) {
            return true;
        }

        gender = gender.toLowerCase();
        if (gender.equals("male") || gender.equals("female") || gender.equals("non_binary") || gender.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    private static boolean isValidPhoneNum(String phoneNum) {
        if(phoneNum == null) {
            return true;
        }

        return phoneNum.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$") || phoneNum.equalsIgnoreCase("null");
    }

    public int getId() {
        return id;
    }

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

    public void updateEntry() throws SQLException {
        if (this.familyGroup == null) {
            String query = "UPDATE Person SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = NULL WHERE id = ?";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(id)};
            String[] types = {"string", "string", "string", "string", "string", "string", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }else{
            String query = "UPDATE Person SET first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = ? WHERE id = ?";
            String[] values = {firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(familyGroup.getId()), String.valueOf(id)};
            String[] types = {"string", "string", "string", "string", "string", "string", "int", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }
    }
}
