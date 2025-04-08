/**
 * @author Noah Vickerson
 * DisasterVictim.java 
 * @version 1.3
 * @date Apr 7 2025
 */

package edu.ucalgary.oop;

import java.util.Arrays;
import java.util.Date;

import java.text.SimpleDateFormat;

import java.time.*;
import java.sql.*;

public class DisasterVictim extends Person implements SupplyHolder {
    private Location currentLocation;
    private MedicalRecord[] medicalRecords;
    private Posession[] personalBelongings;
    private final String entryDate;
    private TextInputValidator validator;

    /**
     * Constructor autogenerate id
     * @param firstName
     * @param lastName
     * @param birthDate must be yyyy-mm-dd
     * @param gender    must be "male" or "female" or "non_binary"
     * @param phoneNum  must be (xxx-)xxx-xxxx
     * @param entryDate must be yyyy-mm-dd
     * @throws IllegalArgumentException if the above conditions not met
     */
    public DisasterVictim(String firstName, String lastName, String birthDate, String gender, String phoneNum, String entryDate) throws IllegalArgumentException {
        super(firstName, lastName, birthDate, gender, phoneNum, "disastervictim");

        this.validator = new TextInputValidator("../data/en-CA.xml"); // for database values

        if(!isValidDateFormat(entryDate)){
            throw new IllegalArgumentException("inv_date");
        }
        this.entryDate = entryDate;
    }

    /**
     * Constructor
     * @param id        must be greater than alll other initialized ids
     * @param firstName
     * @param lastName
     * @param birthDate must be yyyy-mm-dd
     * @param gender    must be "male" or "female" or "non_binary"
     * @param phoneNum  must be (xxx-)xxx-xxxx
     * @param entryDate must be yyyy-mm-dd
     * @throws IllegalArgumentException if the above conditions not met
     */ 
    public DisasterVictim(int id, String firstName, String lastName, String birthDate, String gender, String phoneNum, String entryDate) throws IllegalArgumentException {
        super(id, firstName, lastName, birthDate, gender, phoneNum, "disastervictim");

        this.validator = new TextInputValidator("../data/en-CA.xml"); // for database values

        if(!isValidDateFormat(entryDate)){
            throw new IllegalArgumentException("inv_date");
        }
        this.entryDate = entryDate;
    }

    /**
     * Get the list of medical records
     * @return the list of medical records
     */
    public MedicalRecord[] getMedicalRecords() {
        return medicalRecords;
    }

    /**
     * Set the list of medical records
     * @param medicalRecords the list of medical records
     */
    public void setMedicalRecords(MedicalRecord[] medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    /**
     * Add a medical record to the list of medical records
     * @param record the medical record to add
     */
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

    /**
     * Remove a medical record from the list of medical records
     * @param record the medical record to remove
     */
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

    /**
     * Get the list of personal belongings
     * @return the list of personal belongings
     */
    public Posession[] getPersonalBelongings() {
        return personalBelongings;
    }

    /**
     * Set the list of personal belongings
     * @param personalBelongings the list of personal belongings
     */
    public void setPersonalBelongings(Posession[] personalBelongings) {
        this.personalBelongings = personalBelongings;
    }

    /**
     * Add a personal belonging to the list of personal belongings
     * @param supply the personal belonging to add
     */
    public void addPersonalBelonging(Posession supply){
        if(personalBelongings == null){
            personalBelongings = new Posession[1];
            personalBelongings[0] = supply;
        } else {
            Posession[] largerBelongings = new Posession[personalBelongings.length + 1];
            for(int i = 0; i < personalBelongings.length; i++){
                largerBelongings[i] = personalBelongings[i];
            }
            largerBelongings[personalBelongings.length] = supply;
            personalBelongings = largerBelongings;
        }
    }

    /**
     * Remove a personal belonging from the list of personal belongings
     * @param supply
     * @throws IllegalArgumentException if the supply is not in the personal belongings
     */
    public void removePersonalBelonging(Posession supply) throws IllegalArgumentException{
        Posession[] smallerBelongings = new Posession[personalBelongings.length - 1];
        int index = 0;
        boolean foundSupply = false;
        for(Posession personalBelonging : personalBelongings){
            if(personalBelonging != supply){
                smallerBelongings[index] = personalBelonging;
                index++;
            }else{
                foundSupply = true;
            }
        }

        if(!foundSupply){
            throw new IllegalArgumentException("supply_not_found");
        }
        personalBelongings = smallerBelongings;
    }

    /**
     * Alias for removePersonalBelonging
     * @param supply
     * @throws IllegalArgumentException
     */
    public void removeSupply(Posession supply) throws IllegalArgumentException{
        this.removePersonalBelonging(supply);
    }

    /**
     * Alias for addPersonalBelonging
     * @param supply the supply to add
     */
    public void addSupply(Posession supply){
        this.addPersonalBelonging(supply);
    }

    /**
     * Transfer a supply from the current location to the personal belongings
     * @param supply the supply to transfer
     * @throws IllegalArgumentException if the supply is not in the current location
     */
    public void transferSupply(Posession supply) throws IllegalArgumentException {
        if(!this.currentLocation.containsSupply(supply)){
            throw new IllegalArgumentException("supply_not_found");
        }

        this.currentLocation.removeSupply(supply);
        this.addPersonalBelonging(supply);
    }



    /**
     * Deletes water with allocation date before the current date with the consumption offset
     * @param currentDate
     */
    public void removeUsedSupplies(String currentDate){
        for(Posession supply : this.personalBelongings){
            if(supply.getType() == "Water"){
                Water water = (Water) supply;
                if(water.isConsumed(currentDate)){
                    this.removePersonalBelonging(supply);
                }
            }
        }
    }

    /**
     * Get the current location
     * @return the current location
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Set the current location
     * @param currentLocation the current location
     */
    public void setCurrentLocation(Location currentLocation) {
        if(this.currentLocation != null){
            this.currentLocation.removeOccupant(this);
        }
        this.currentLocation = currentLocation;
        this.currentLocation.addOccupant(this);
    }

    /**
     * Check if a supply is in the personal belongings
     * @return found if supply in personal belongings
     */
    public boolean containsSupply(Posession supply) {
        boolean found = false;
        for(Posession personalBelonging : personalBelongings){
            if(personalBelonging == supply){
                found = true;
            }
        }
        return found;
    }

    /**
     * Get the entry date
     * @return the entry date
     */
    public String getEntryDate() {
        return entryDate;
    }

    /**
     * Override the addEntry method from the Person class to add insertion into locations
     */
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

    /**
     * Override the updateEntry method from the Person class to update locations and supply allocation
     */
    @Override
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
            for(Posession s : this.personalBelongings) {
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
                if(db.deadEndQuery(query, values, types) == 0){
                    query = "INSERT INTO SupplyAllocation (supply_id, person_id, location_id, allocation_date) VALUES (?, ?, ?, ?)";
                    curDate = null;
                    if(s instanceof Water && ((Water)s).getAllocationDate() != null){
                        curDate = ((Water)s).getAllocationDate();
                    }else{
                        curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().substring(0, 10);
                    }
                    String[] values2 = {String.valueOf(s.getId()), String.valueOf(id), "NULL", curDate};
                    String[] types2 = {"int", "int", "int", "date"};
                    db.deadEndQuery(query, values2, types2);
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
