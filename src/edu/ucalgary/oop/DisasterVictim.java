package edu.ucalgary.oop;

import java.util.Arrays;
import java.util.Date;

import java.text.SimpleDateFormat;

import java.time.*;
import java.sql.*;

public class DisasterVictim extends Person {
    private Location currentLocation;
    private MedicalRecord[] medicalRecords;
    private Supply[] personalBelongings;
    private final String entryDate;
    private TextInputValidator validator;

    public DisasterVictim(String firstName, String lastName, String birthDate, String gender, String phoneNum, String entryDate) throws IllegalArgumentException {
        super(firstName, lastName, birthDate, gender, phoneNum, "disastervictim");

        if(!isValidDateFormat(entryDate)){
            throw new IllegalArgumentException("Invalid date format: " +  entryDate);
        }
        this.entryDate = entryDate;
        validator = TextInputValidator.getInstance();
    }

    public DisasterVictim(int id, String firstName, String lastName, String birthDate, String gender, String phoneNum, String entryDate) throws IllegalArgumentException {
        super(id, firstName, lastName, birthDate, gender, phoneNum, "disastervictim");

        if(!isValidDateFormat(entryDate)){
            throw new IllegalArgumentException("Invalid date format: " +  entryDate);
        }
        this.entryDate = entryDate;
        validator = TextInputValidator.getInstance();
    }

    public MedicalRecord[] getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(MedicalRecord[] medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public void addMedicalRecord(MedicalRecord record){
        if(medicalRecords == null){
            medicalRecords = new MedicalRecord[1];
            medicalRecords[0] = record;
        } else {
            MedicalRecord[] largerRecords = new MedicalRecord[medicalRecords.length + 1];
            for(int i = 0; i < medicalRecords.length; i++){
                largerRecords[i] = medicalRecords[i];
            }
            largerRecords[medicalRecords.length] = record;
            medicalRecords = largerRecords;
        }
    }

    public void removeMedicalRecord(MedicalRecord record){
        MedicalRecord[] smallerRecords = new MedicalRecord[medicalRecords.length - 1];
        int index = 0;
        boolean foundRecord = false;
        for(MedicalRecord medicalRecord : medicalRecords){
            if(medicalRecord != record){
                smallerRecords[index] = medicalRecord;
                index++;
            }else{
                foundRecord = true;
            }
        }

        if(!foundRecord){
            return;
        }
        medicalRecords = smallerRecords;
    }

    public Supply[] getPersonalBelongings() {
        return personalBelongings;
    }

    public void setPersonalBelongings(Supply[] personalBelongings) {
        this.personalBelongings = personalBelongings;
    }

    public void addPersonalBelonging(Supply supply){
        if(personalBelongings == null){
            personalBelongings = new Supply[1];
            personalBelongings[0] = supply;
        } else {
            Supply[] largerBelongings = new Supply[personalBelongings.length + 1];
            for(int i = 0; i < personalBelongings.length; i++){
                largerBelongings[i] = personalBelongings[i];
            }
            largerBelongings[personalBelongings.length] = supply;
            personalBelongings = largerBelongings;
        }
    }

    public void removePersonalBelonging(Supply supply) throws IllegalArgumentException{
        Supply[] smallerBelongings = new Supply[personalBelongings.length - 1];
        int index = 0;
        boolean foundSupply = false;
        for(Supply personalBelonging : personalBelongings){
            if(personalBelonging != supply){
                smallerBelongings[index] = personalBelonging;
                index++;
            }else{
                foundSupply = true;
            }
        }

        if(!foundSupply){
            throw new IllegalArgumentException("The supply is not in the personal belongings");
        }
        personalBelongings = smallerBelongings;
    }

    public void transferSupply(Supply supply) throws IllegalArgumentException {
        if(!this.currentLocation.containsSupply(supply)){
            throw new IllegalArgumentException("The supply is not in the current location");
        }

        this.currentLocation.removeSupply(supply);
        this.addPersonalBelonging(supply);
    }

    public void addSupply(Supply supply){
        this.addPersonalBelonging(supply);
    }

    public void removeUsedSupplies(String currentDate){
        for(Supply supply : this.personalBelongings){
            if(supply.getType() == "Water"){
                Water water = (Water) supply;
                if(water.isConsumed(currentDate)){
                    this.removePersonalBelonging(supply);
                }
            }
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        if(this.currentLocation != null){
            this.currentLocation.removeOccupant(this);
        }
        this.currentLocation = currentLocation;
        this.currentLocation.addOccupant(this);
    }

    public String getEntryDate() {
        return entryDate;
    }

    @Override
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

        String query = "INSERT INTO PersonLocation (person_id, location_id) VALUES (?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(currentLocation.getId())};
        String[] types = {"int", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    public void updateEntry() throws SQLException {
        if (this.familyGroup == null) {
            String query = "UPDATE Person SET person_id = ?, first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = NULL WHERE person_id = ?";
            String[] values = {String.valueOf(id), firstName, lastName, birthDate, validator.translateToLanguage(gender), comments, phoneNum, String.valueOf(id)};
            String[] types = {"int", "string", "string", "date", "string", "string", "string", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }else{
            String query = "UPDATE Person SET person_id = ?, first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = ? WHERE person_id = ?";
            String[] values = {String.valueOf(id), firstName, lastName, birthDate, validator.translateToLanguage(gender), comments, phoneNum, String.valueOf(familyGroup.getId()), String.valueOf(id)};
            String[] types = {"int", "string", "string", "date", "string", "string", "string", "int", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }

        if(this.personalBelongings != null){
            for(Supply s : this.personalBelongings) {
                try{
                    String query = "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) VALUES (?, ?, ?, ?)";
                    String curDate = null;
                    if(s instanceof Water && ((Water)s).getAllocationDate() != null){
                        curDate = ((Water)s).getAllocationDate();
                    }else{
                        curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().substring(0, 10);
                    }
                    String[] values = {String.valueOf(s.getId()), String.valueOf(id), "NULL", curDate};
                    String[] types = {"int", "int", "int", "date"};
                    DbConnector db = DbConnector.getInstance();
                    db.deadEndQuery(query, values, types);
                } catch (SQLException e) {
                    // update in case where supply exists
                    String query = "UPDATE SupplyAllocation SET supply_id = ?, person_id = ?, location_id = NULL, allocation_date = ? WHERE supply_id = ?";
                    String curDate = null;
                    if(s instanceof Water && ((Water)s).getAllocationDate() != null){
                        curDate = ((Water)s).getAllocationDate();
                    }else{
                        curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().substring(0, 10);
                    }
                    String[] values = {String.valueOf(s.getId()), String.valueOf(id), curDate, String.valueOf(s.getId())};
                    String[] types = {"int", "int", "date", "int"};
                    DbConnector db = DbConnector.getInstance();
                    db.deadEndQuery(query, values, types);
                }
            }
 
        }
        
        if(this.currentLocation != null){
            try{
                // update in case where location exists
                String query = "UPDATE PersonLocation SET location_id = ? WHERE person_id = ?";
                String[] values = {String.valueOf(currentLocation.getId()), String.valueOf(id)};
                String[] types = {"int", "int"};
                DbConnector db = DbConnector.getInstance();
                db.deadEndQuery(query, values, types);
            } catch (SQLException e) {
                String query = "INSERT INTO PersonLocation (person_id, location_id) VALUES (?, ?)";
                String[] values = {String.valueOf(id), String.valueOf(currentLocation.getId())};
                String[] types = {"int", "int"};
                DbConnector db = DbConnector.getInstance();
                db.deadEndQuery(query, values, types);
            }
        }
    }

}
