package edu.ucalgary.oop;

import java.sql.*;

import org.w3c.dom.Text;

public class MedicalRecord implements DatabaseInterfaceable {
    private Location location;
    private String treatmentDetails;
    private String dateOfTreatment;
    private DisasterVictim person;
    private final int id;
    private static int counter = 0;
    private TextInputValidator validator;

    public MedicalRecord(Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        validator = TextInputValidator.getInstance();
        if(!validator.isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException(validator.translateToLanguage("inv_date") + ": " + dateOfTreatment);
        }

        this.person = person;
        this.location = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment = dateOfTreatment;
        counter++;
        this.id = counter;
    }

    public MedicalRecord(int id, Location location, DisasterVictim person, String treatmentDetails, String dateOfTreatment) throws IllegalArgumentException {
        validator = TextInputValidator.getInstance();
        if(!validator.isValidDateFormat(dateOfTreatment)){
            throw new IllegalArgumentException(validator.translateToLanguage("inv_date") + ": " + dateOfTreatment);
        }
        if(id < counter){
			throw new IllegalArgumentException(validator.translateToLanguage("repeat_id") + ": " + id);
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
        String query = "INSERT INTO MedicalRecord (medical_record_id, location_id, person_id, date_of_treatment, treatment_details) VALUES (?, ?, ?, ?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails};
        String[] types = {"int", "int", "int", "date", "string"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    public void updateEntry() throws SQLException {
        String query = "UPDATE MedicalRecord SET location_id = ?, person_id = ?, date_of_treatment = ?, treatment_details = ? WHERE medical_record_id = ?";
        String[] values = {String.valueOf(location.getId()), String.valueOf(person.getId()), dateOfTreatment, treatmentDetails, String.valueOf(id)};
        String[] types = {"int", "int", "date", "string", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }
}
