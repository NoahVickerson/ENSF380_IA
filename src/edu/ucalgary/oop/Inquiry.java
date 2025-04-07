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

    public Inquiry(Person inquirer, DisasterVictim missingPerson, String dateOfInquiry, String infoProvided, Location lastKnownLocation) throws IllegalArgumentException {
        if(inquirer == missingPerson){
            throw new IllegalArgumentException("Inquirer and missing person cannot be the same");
        }
        
        if(!isValidDateFormat(dateOfInquiry)){
            throw new IllegalArgumentException("Invalid Date: " + dateOfInquiry);
        }
        
        this.inquirer = inquirer;
        this.missingPerson = missingPerson;
        this.lastKnownLocation = lastKnownLocation;
        this.infoProvided = infoProvided;
        this.dateOfInquiry = dateOfInquiry;
        counter++;
        this.id = counter;
    }

    public Inquiry(int id, Person inquirer, DisasterVictim missingPerson, String dateOfInquiry, String infoProvided, Location lastKnownLocation) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("id may not be unique");
		}

        if(inquirer == missingPerson){
            throw new IllegalArgumentException("Inquirer and missing person cannot be the same");
        }
        
        if(!isValidDateFormat(dateOfInquiry)){
            throw new IllegalArgumentException("Invalid Date: " + dateOfInquiry);
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

    public int getId() {
        return id;
    }

    public Person getInquirer() {
        return inquirer;
    }

    public void setInquirer(Person inquirer) {
        this.inquirer = inquirer;
    }

    public DisasterVictim getMissingPerson() {
        return missingPerson;
    }

    public String getDateOfInquiry() {
        return dateOfInquiry;
    }

    public String getInfoProvided() {
        return infoProvided;
    }

    public void setInfoProvided(String infoProvided) {
        this.infoProvided = infoProvided;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public void addEntry() throws SQLException {
        String query = "INSERT INTO Inquiry (inquiry_id, inquirer_id, seeking_id, location_id, date_of_inquiry, comments) VALUES (?, ?, ?, ?, ?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(inquirer.getId()), String.valueOf(missingPerson.getId()), String.valueOf(lastKnownLocation.getId()), dateOfInquiry, infoProvided};
        String[] types = {"int", "int", "int", "date", "string"};

        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

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