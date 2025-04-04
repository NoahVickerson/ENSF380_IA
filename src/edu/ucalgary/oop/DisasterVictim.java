package edu.ucalgary.oop;

import java.util.Arrays;
import java.util.Date;
import java.time.*;
import java.sql.*;

public class DisasterVictim extends Person {
    private Location currentLocation;
    private MedicalRecord[] medicalRecords;
    private Supply[] personalBelongings;
    private final String entryDate;

    public DisasterVictim(String firstName, String lastName, String birthDate, String gender, String phoneNum, String entryDate) throws IllegalArgumentException {
        super(firstName, lastName, birthDate, gender, phoneNum, "disastervictim");

        if(!isValidDateFormat(entryDate)){
            throw new IllegalArgumentException("Invalid date format: " +  entryDate);
        }
        this.entryDate = entryDate;
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

    public void removePersonalBelonging(Supply supply){
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
            return;
        }
        personalBelongings = smallerBelongings;
    }

    public void transferSupply(Supply supply) throws IllegalArgumentException{
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
        this.currentLocation = currentLocation;
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
            String[] values = {String.valueOf(id), firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(id)};
            String[] types = {"int", "string", "string", "string", "string", "string", "string", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }else{
            String query = "UPDATE Person SET person_id = ?, first_name = ?, last_name = ?, date_of_birth = ?, gender = ?, comments = ?, phone_number = ?, family_group = ? WHERE id = ?";
            String[] values = {String.valueOf(id), firstName, lastName, birthDate, gender, comments, phoneNum, String.valueOf(familyGroup.getId()), String.valueOf(id)};
            String[] types = {"int", "string", "string", "string", "string", "string", "string", "int", "int"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }

        for(Supply s : this.personalBelongings) {
            s.updateEntry();
            String query = "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) VALUES (?, ?, ?, ?)";
            String curDate = (new Date()).toString().substring(0, 10);
            String[] values = {String.valueOf(s.getId()), String.valueOf(id), "0", curDate};
            String[] types = {"int", "int", "int", "date"};
            DbConnector db = DbConnector.getInstance();
            db.deadEndQuery(query, values, types);
        }

        String query = "INSERT INTO PersonLocation (person_id, location_id) VALUES (?, ?)";
        String[] values = {String.valueOf(id), String.valueOf(currentLocation.getId())};
        String[] types = {"int", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

}
