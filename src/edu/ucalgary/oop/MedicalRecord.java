package edu.ucalgary.oop;

import java.sql.*;

public class MedicalRecord implements DatabaseInterfaceable {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;
    private DisasterVictim person;
    private final int id;
    private static int counter = 0;

    public MedicalRecord(Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("Invalid date format");
        }

        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
        this.id = counter++;
    }

    public MedicalRecord(int id, Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("id may not be unique");
		}
        
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("Invalid date format");
        }

        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DisasterVictim getPerson() {
        return person;
    }

    public void setPerson(DisasterVictim person) {
        this.person = person;
    }

    public String getTreatmentDetails() {
        return treatmentDetails;
    }

    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    public String getDateOfTreatment() {
        return dateOfTreatment;
    }

    public void setDateOfTreatment(String dateOfTreatment) throws IllegalArgumentException {
        if(!isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException("Invalid date format");
        }
        this.dateOfTreatment = dateOfTreatment;
    }

    private static boolean isValidDateFormat(String date){
        if(date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")){
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void addEntry() throws SQLException {
        String query = "INSERT INTO MedicalRecord (location_id, person_id, date_of_treatment, treatment_details) VALUES (?, ?, ?, ?)";
        String[] values = {String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails};
        String[] types = {"int", "int", "string", "string"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    public void updateEntry() throws SQLException {
        String query = "UPDATE MedicalRecord SET location_id = ?, person_id = ?, date_of_treatment = ?, treatment_details = ? WHERE id = ?";
        String[] values = {String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails, String.valueOf(id)};
        String[] types = {"int", "int", "string", "string", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }
}
