/**
 * @author Noah Vickerson
 * MedicalRecord.java 
 * @version 1.3
 * @date Apr 2 2025
 */

package edu.ucalgary.oop;

import java.sql.*;

public class MedicalRecord implements DatabaseInterfaceable {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;
    private DisasterVictim person;
    private final int id;
    private static int counter = 0;

    /**
     * Creates a new medical record
     * @param location
     * @param person
     * @param treatmentDetails
     * @param dateOfTreatment must be in the format YYYY-MM-DD
     * @throws IllegalArgumentException for invalid dateoftreatment
     */
    public MedicalRecord(Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("inv_date");
        }

        this.person = person;
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
        counter++;
        this.id = counter;
    }

    /**
     * Creates a new medical record
     * @param id must be unique
     * @param location
     * @param person
     * @param treatmentDetails 
     * @param dateOfTreatment must be in the format YYYY-MM-DD
     * @throws IllegalArgumentException for invalid dateoftreatment or id
     */
    public MedicalRecord(int id, Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("inv_date");
        }
        if(id < counter){
			throw new IllegalArgumentException("repeat_id");
		}

        this.person = person;
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    /**
     * Returns the location
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the person
     * @return person
     */
    public DisasterVictim getPerson() {
        return person;
    }

    /**
     * Sets the person
     * @param person
     */
    public void setPerson(DisasterVictim person) {
        this.person = person;
    }

    /**
     * Returns the treatment details
     * @return
     */
    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    /**
     * Sets the treatment details
     * @param treatmentDetails
     */
    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    /**
     * Returns the date of treatment
     * @return
     */
    public String getDateOfTreatment() {
        return dateOfTreatment;
    }

    /**
     * Sets the date of treatment
     * @param dateOfTreatment must be in the format YYYY-MM-DD
     * @throws IllegalArgumentException for invalid dateoftreatment
     */
    public void setDateOfTreatment(String dateOfTreatment) throws IllegalArgumentException {
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("inv_date");
        }
        this.dateOfTreatment = dateOfTreatment;
    }

    /**
     * Checks if the date is in the format YYYY-MM-DD
     * @param date
     * @return true if the date is in the format YYYY-MM-DD
     */
    private static boolean isValidDateFormat(String date){
        if(date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")){
            return true;
        }
        return false;
    }

    /**
     * Returns the id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Adds the entry to the database
     * @throws SQLException
     */
    public void addEntry() throws SQLException {
        String query = "INSERT INTO MedicalRecord (medical_record_id, location_id, person_id, date_of_treatment, treatment_details) VALUES (?, ?, ?, ?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails};
        String[] types = {"int", "int", "int", "date", "string"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    /**
     * Updates the entry in the database
     * @throws SQLException
     */
    public void updateEntry() throws SQLException {
        String query = "UPDATE MedicalRecord SET location_id = ?, person_id = ?, date_of_treatment = ?, treatment_details = ? WHERE medical_record_id = ?";
        String[] values = {String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails, String.valueOf(id)};
        String[] types = {"int", "int", "date", "string", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }
}
