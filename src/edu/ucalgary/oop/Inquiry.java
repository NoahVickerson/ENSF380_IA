/**
 * @author Noah Vickerson
 * Inquiry.java 
 * @version 1.1
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.sql.*;

public class Inquiry implements DatabaseInterfaceable {

    private Person inquirer;
    private DisasterVictim missingPerson;
    private Location lastKnownLocation;
    private String infoProvided;
    private final String dateOfInquiry;
    private final int id;
    private static int counter = 0;

    /**
     * Creates a new inquiry
     * @param inquirer
     * @param missingPerson
     * @param dateOfInquiry must be in the format YYYY-MM-DD
     * @param infoProvided
     * @param lastKnownLocation
     * @throws IllegalArgumentException if inquirer and missing person different, or invalid date
     */
    public Inquiry(Person inquirer, DisasterVictim missingPerson, String dateOfInquiry, String infoProvided, Location lastKnownLocation) throws IllegalArgumentException {
        if(inquirer == missingPerson){
            throw new IllegalArgumentException("inq_is_person");
        }
        
        if(!isValidDateFormat(dateOfInquiry)){
            throw new IllegalArgumentException("inv_date");
        }
        
        this.inquirer = inquirer;
        this.missingPerson = missingPerson;
        this.lastKnownLocation = lastKnownLocation;
        this.infoProvided = infoProvided;
        this.dateOfInquiry = dateOfInquiry;
        counter++;
        this.id = counter;
    }

    /** 
     * Creates a new inquiry with id specified
     * @param id
     * @param inquirer
     * @param missingPerson
     * @param dateOfInquiry must be in the format YYYY-MM-DD
     * @param infoProvided
     * @param lastKnownLocation
     * @throws IllegalArgumentException if inquirer and missing person different, or invalid date
     */
    public Inquiry(int id, Person inquirer, DisasterVictim missingPerson, String dateOfInquiry, String infoProvided, Location lastKnownLocation) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("repeat_id");
		}

        if(inquirer == missingPerson){
            throw new IllegalArgumentException("inq_is_person");
        }
        
        if(!isValidDateFormat(dateOfInquiry)){
            throw new IllegalArgumentException("inv_date");
        }
        
        this.inquirer = inquirer;
        this.missingPerson = missingPerson;
        this.lastKnownLocation = lastKnownLocation;
        this.infoProvided = infoProvided;
        this.dateOfInquiry = dateOfInquiry;
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    /**
     * Returns the id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the inquirer
     * @return
     */
    public Person getInquirer() {
        return inquirer;
    }

    /**
     * Sets the inquirer
     * @param inquirer
     * @throws IllegalArgumentException if inquirer and missing person different
     */
    public void setInquirer(Person inquirer) throws IllegalArgumentException {
        if(missingPerson == inquirer){
            throw new IllegalArgumentException("inq_is_person");
        }
        this.inquirer = inquirer;
    }

    /**
     * Returns the missing person
     * @return
     */
    public DisasterVictim getMissingPerson() {
        return missingPerson;
    }

    /**
     * gets the date of the inquiry
     * @return
     */
    public String getDateOfInquiry() {
        return dateOfInquiry;
    }

    /**
     * gets the info provided
     * @return
     */
    public String getInfoProvided() {
        return infoProvided;
    }

    /**
     * sets the info provided
     * @param infoProvided
     */
    public void setInfoProvided(String infoProvided) {
        this.infoProvided = infoProvided;
    }

    /**
     * gets the last known location
     * @return
     */
    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    /**
     * sets the last known location
     * @param lastKnownLocation
     */
    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    /**
     * Inserts the entry into the database
     * @throws SQLException
     */
    public void addEntry() throws SQLException {
        String query = "INSERT INTO Inquiry (inquiry_id, inquirer_id, seeking_id, location_id, date_of_inquiry, comments) VALUES (?, ?, ?, ?, ?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(inquirer.getId()), String.valueOf(missingPerson.getId()), String.valueOf(lastKnownLocation.getId()), dateOfInquiry, infoProvided};
        String[] types = {"int", "int", "int", "date", "string"};

        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    /**
     * Updates the entry in the database
     * @throws SQLException
     */
    public void updateEntry() throws SQLException {
        String query = "UPDATE Inquiry SET inquirer_id = ?, seeking_id = ?, location_id = ?, date_of_inquiry = ?, comments = ? WHERE inquiry_id = ?";
        String[] values = {String.valueOf(inquirer.getId()), String.valueOf(missingPerson.getId()), String.valueOf(lastKnownLocation.getId()), dateOfInquiry, infoProvided, String.valueOf(id)};
        String[] types = {"int", "int", "int", "date", "string", "int"};

        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    private static boolean isValidDateFormat(String date) {
        if(date == null) {
            return true;
        }

        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || date.equalsIgnoreCase("null");
    }
}