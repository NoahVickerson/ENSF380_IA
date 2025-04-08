/**
 * @author Noah Vickerson
 * ReliefController.java 
 * @version 2.5
 * @date Apr 7 2025
 */

package edu.ucalgary.oop;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.time.*;
import java.util.Date;

public class ReliefController {
    private static ReliefController controller = null;
    private Location[] locations = new Location[0];
    private Person[] people = new Person[0];
    private Inquiry[] inquiries = new Inquiry[0];
    private FamilyGroup[] familyGroups = new FamilyGroup[0];
    private DatabaseQueryHandler fetcher;
    private TextInputValidator validator;

    /**
     * Constructor 
     * @param url for database
     * @param username for database
     * @param password  for database
     * @throws SQLException with database errors in loads or connections
     * @throws IllegalArgumentException with database connection errors due to invalid uname or password
     */
    private ReliefController(String url, String username, String password, TextInputValidator validator) throws SQLException, IllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString(); // get current date for water

        this.validator = validator;
        try {
            fetcher = DbConnector.getInstance(url, username, password); // initialize database connection
        } catch (SQLException e) {
            throw new IllegalArgumentException("Failed to connect to database" + "\n" + e.getMessage());
        }

        loadLocations();

        // populate people array
        loadPeople();

        // add currentLocation and famiy group fields
        loadCurrentLocations();

        loadFamilyGroups();

        loadInquiries();

        loadSupplies();

        loadMedicalRecords();
    }

    /**
     * Default constructor without loading from database to test on test data
     */
    private ReliefController() {
        // for testing without a database dependency

    }

    /**
     * Get singleton instance of ReliefController
     * @param uname username for database
     * @param pword password for database
     * @return ReliefController
     * @throws IllegalArgumentException with database connection errors due to invalid uname or password
     * @throws SQLException with database errors in loads or connections
     */
    public static ReliefController getInstance(String uname, String pword, TextInputValidator validator) throws IllegalArgumentException, SQLException {
        if(controller == null) {
            controller = new ReliefController("jdbc:postgresql://localhost/ensf380project", uname, pword, validator);
        }
        return controller;
    }

    /**
     * Get singleton instance of ReliefController when already initialized
     * @return ReliefController
     * @throws IllegalStateException when ReliefController has not been initialized
     */
    public static ReliefController getInstance() throws IllegalStateException{
        if(controller == null) {
            controller = new ReliefController(); // for testing decoupled from the database
        }
        return controller;
    }

    /**
     * Get locations
     * @return locations stored
     */
    public Location[] getLocations() {
        return locations;
    }

    /**
     * Get people
     * @return people stored
     */
    public Person[] getPeople() {
        return people;
    }

    /**
     * Get victims
     * @return people stored that are victims
     */
    public DisasterVictim[] getVictims() {
        int numVictims = 0;
        for(int i = 0; i < people.length; i++) {
            if(people[i] instanceof DisasterVictim) {
                numVictims++;
            }
        }
        DisasterVictim[] tempVictims = new DisasterVictim[numVictims];
        for(int i = 0; i < people.length; i++) {
            if(people[i] instanceof DisasterVictim) {
                tempVictims[i] = (DisasterVictim) people[i];
            }
        }
        return tempVictims;
    }

    /**
     * Get inquiries
     * @return inquiries stored
     */
    public Inquiry[] getInquiries() {
        return inquiries;
    }

    /**
     * Get family groups
     * @return family groups stored
     */
    public FamilyGroup[] getFamilyGroups() {
        return familyGroups;
    }

    /**
     * Add disaster victim to people array
     * @param firstName first name of person
     * @param lastName last name of person
     * @param dateOfBirth date of birth of person
     * @param gender gender of person
     * @param phoneNum phone number of person
     * @param currentLocation current location of person
     * @param entryDate entry date of person
     */
    public void addDisasterVictim(String firstName, String lastName, String dateOfBirth, String gender, String phoneNum, Location currentLocation, String entryDate) {
        if(fetchPerson(firstName, lastName) != null) {
            return;
        }

        DisasterVictim victim = new DisasterVictim(firstName, lastName, dateOfBirth, gender, phoneNum, entryDate);

        if(fetchLocation(currentLocation) == null) {
            addLocation(currentLocation);
        }

        Person[] newPeople = new Person[people.length + 1];
        for (int i = 0; i < people.length; i++) {
            newPeople[i] = people[i];
        }
        newPeople[people.length] = victim;
        people = newPeople;

    }

    /**
     * Add disaster victim to people array
     * @param victim disaster victim to add
     */
    public void addDisasterVictim(DisasterVictim victim) {
        if(fetchPerson(victim) != null) {
            return;
        }

        Person[] newPeople = new Person[people.length + 1];
        for (int i = 0; i < people.length; i++) {
            newPeople[i] = people[i];
        }
        newPeople[people.length] = victim;
        people = newPeople;
    }

    /**
     * Add family group to family groups array
     * @param members members of family group
     */
    public void addFamilyGroup(Person[] members) {
        FamilyGroup familyGroup = new FamilyGroup();
        familyGroup.setFamilyMembers(members);

        FamilyGroup[] newFamilyGroups = new FamilyGroup[familyGroups.length + 1];
        for(int i = 0; i < familyGroups.length; i++) {
            newFamilyGroups[i] = familyGroups[i];
        }
        newFamilyGroups[familyGroups.length] = familyGroup;
        familyGroups = newFamilyGroups;
    }

    /**
     * Add family group to family groups array
     * @param familyGroup family group to add
     */
    public void addFamilyGroup(FamilyGroup familyGroup) {
        FamilyGroup[] newFamilyGroups = new FamilyGroup[familyGroups.length + 1];
        for(int i = 0; i < familyGroups.length; i++) {
            newFamilyGroups[i] = familyGroups[i];
        }
        newFamilyGroups[familyGroups.length] = familyGroup;
        familyGroups = newFamilyGroups;
    }

    /**
     * Add inquiry to inquiries array
     * @param inquirer inquirer of inquiry
     * @param missingPerson missing person of inquiry
     * @param dateOfInquiry date of inquiry
     * @param description description of inquiry
     * @param location location of inquiry
     */
    public void addInquiry(Person inquirer, DisasterVictim missingPerson, String dateOfInquiry, String description, Location location) {
        if(fetchInquiry(inquirer, missingPerson, dateOfInquiry) != null) {
            return;
        }

        Inquiry inquiry = new Inquiry(inquirer, missingPerson, dateOfInquiry, description, location);

        if(fetchLocation(location) == null) {
            addLocation(location);
        }

        if(fetchPerson(missingPerson) == null) {
            addPerson(missingPerson);
        }

        if(fetchInquiry(inquiry) != null) {
            return;
        }

        Inquiry[] newInquiries = new Inquiry[inquiries.length + 1];
        for(int i = 0; i < inquiries.length; i++) {
            newInquiries[i] = inquiries[i];
        }
        newInquiries[inquiries.length] = inquiry;
        inquiries = newInquiries;

    }

    /**
     * Add inquiry to inquiries array
     * @param inquiry inquiry to add
     */
    public void addInquiry(Inquiry inquiry) {
        if(fetchInquiry(inquiry) != null) {
            return;
        }

        Inquiry[] newInquiries = new Inquiry[inquiries.length + 1];
        for(int i = 0; i < inquiries.length; i++) {
            newInquiries[i] = inquiries[i];
        }
        newInquiries[inquiries.length] = inquiry;
        inquiries = newInquiries;
    }

    /**
     * Add location to locations array
     * @param location location to add
     */
    public void addLocation(Location location) {
        if(fetchLocation(location) != null) {
            return;
        }

        Location[] newLocations = new Location[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            newLocations[i] = locations[i];
        }
        newLocations[locations.length] = location;
        locations = newLocations;
    }

    /**
     * Add location to locations array
     * @param name name of location
     * @param address address of location
     */
    public void addLocation(String name, String address) {
        if(fetchLocation(name) != null) {
            return;
        }

        Location location = new Location(name, address);

        Location[] newLocations = new Location[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            newLocations[i] = locations[i];
        }
        newLocations[locations.length] = location;
        locations = newLocations;
    }

    /**
     * Add person to people array
     * @param firstName first name of person
     * @param lastName last name of person
     * @param dateOfBirth date of birth of person
     * @param gender gender of person
     * @param phoneNum phone number of person
     */
    public void addPerson(String firstName, String lastName, String dateOfBirth, String gender, String phoneNum) {
        if(fetchPerson(firstName, lastName) != null) {
            return;
        }

        Person person = new Person(firstName, lastName, dateOfBirth, gender, phoneNum);

        Person[] newPeople = new Person[people.length + 1];
        for (int i = 0; i < people.length; i++) {
            newPeople[i] = people[i];
        }
        newPeople[people.length] = person;
        people = newPeople;
    }

    /**
     * Add person to people array
     * @param person person to add
     */
    public void addPerson(Person person) {
        if(fetchPerson(person) != null) {
            return;
        }

        Person[] newPeople = new Person[people.length + 1];
        for (int i = 0; i < people.length; i++) {
            newPeople[i] = people[i];
        }
        newPeople[people.length] = person;
        people = newPeople;
    }

    /**
     * Fetch person from people array
     * @param person person to fetch
     * @return null if not found, found otherwise
     */
    public Person fetchPerson(Person person) {
        for (Person p : people) {
            if (p.equals(person)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Fetch person from people array
     * @param firstName first name of person
     * @param lastName last name of person
     * @return null if not found, found otherwise
     */
    public Person fetchPerson(String firstName, String lastName) {
        for (Person p : people) {
            if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Fetch person from people array
     * @param id id of person
     * @return null if not found, found otherwise
     */
    public Person fetchPerson(int id) {
        for (Person p : people) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Fetch victim from people array
     * @param id id of victim
     * @return null if not found, found otherwise
     */
    public DisasterVictim fetchVictim(int id) {
        for (Person p : people) {
            if (p.getId() == id && p instanceof DisasterVictim) {
                return (DisasterVictim) p;
            }
        }
        return null;
    }

    /**
     * Fetch victim from people array
     * @param firstName first name of victim
     * @param lastName last name of victim
     * @return null if not found, found otherwise
     */
    public DisasterVictim fetchVictim(String firstName, String lastName) {
        for (Person p : people) {
            if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName) && p instanceof DisasterVictim) {
                return (DisasterVictim) p;
            }
        }
        return null;
    }

    /**
     * Fetch location from locations array
     * @param location location to fetch
     * @return location if found, null otherwise
     */
    public Location fetchLocation(Location location) {
        for (Location l : locations) {
            if (l.equals(location)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Fetch location from locations array
     * @param name name of location
     * @return location if found, null otherwise
     */
    public Location fetchLocation(String name) {
        for (Location l : locations) {
            if (l.getName().equals(name)) {
                return l;
            }
        }
        return null;
    }

    /**
     * Fetch location from locations array
     * @param id id of location
     * @return location if found, null otherwise
     */
    public Location fetchLocation(int id) {
        for (Location l : locations) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    /**
     * Fetch inquiry from inquiries array
     * @param inquiry inquiry to fetch
     * @return inquiry if found, null otherwise
     */
    public Inquiry fetchInquiry(Inquiry inquiry) {
        for (Inquiry i : inquiries) {
            if (i.equals(inquiry)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Fetch inquiry from inquiries array
     * @param inquirer inquirer of inquiry
     * @param victim victim of inquiry
     * @param date date of inquiry
     * @return inquiry if found, null otherwise
     */
    public Inquiry fetchInquiry(Person inquirer, DisasterVictim victim, String date) {
        for (Inquiry i : inquiries) {
            if (i.getInquirer().equals(inquirer) && i.getMissingPerson().equals(victim) && i.getDateOfInquiry().equals(date)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Fetch inquiry from inquiries array
     * @param id id of inquiry
     * @return inquiry if found, null otherwise
     */
    public Inquiry fetchInquiry(int id) {
        for (Inquiry i : inquiries) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }   

    /**
     * Fetch family group from familyGroups array
     * @param id id of family group
     * @return family group if found, null otherwise
     */
    public FamilyGroup fetchFamilyGroup(int id) {
        for (FamilyGroup f : familyGroups) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    /**
     * Save data to database
     */
    public void saveData(){
        try {
            for( Person p : people) {
                p.updateEntry();
            }

            for( Location l : locations) {
                l.updateEntry();
            }

            for( Inquiry i : inquiries) {
                i.updateEntry();
            }

        } catch (SQLException e) {
            System.out.println("Unable to save data to database.");
        }

    }

    /**
     * Load locations from database
     * @throws SQLException if database query fails
     */
    public void loadLocations() throws SQLException{
        String rs = fetcher.getEntries("Location", "location_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int location_id = Integer.parseInt(columns[0]);
            String name = columns[1];
            String address = columns[2];

            Location newLocation = new Location(location_id, name, address);

            addLocation(newLocation);
        }
    }

    /**
     * Load people from database
     * @throws SQLException if database query fails
     */
    public void loadPeople() throws SQLException {
        String rs = fetcher.getEntries("Person", "person_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int person_id = Integer.parseInt(columns[0]);
            String first_name = columns[1];
            String last_name = columns[2];
            String date_of_birth = columns[3];
            String gender =  validator.translateToKey(columns[4]);
            String comments = columns[5];
            String phone_number = columns[6];

            if(!date_of_birth.equals("null")) {
                date_of_birth = date_of_birth.substring(0, 10);
            }

            Person person = new DisasterVictim(person_id, first_name, last_name, date_of_birth, gender, phone_number, null); // no date in database, give a null value, and family group can be added later

            if(!comments.equals("")) {
                person.setComments(comments);
            }
            addPerson(person);
        }
    }

    /**
     * Load current locations from database
     * @throws SQLException if database query fails
     */
    public void loadCurrentLocations() throws SQLException {
        String rs = fetcher.getEntries("PersonLocation");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int person_id = Integer.parseInt(columns[0]);
            int location_id = Integer.parseInt(columns[1]);

            fetchVictim(person_id).setCurrentLocation(fetchLocation(location_id));
        }
    }
    
    /**
     * Load family groups from database
     * @throws SQLException if database query fails
     */
    public void loadFamilyGroups() throws SQLException {

        String rs = fetcher.getEntries("Person", "person_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            if(columns[7].equalsIgnoreCase("null")) {
                continue;
            }

            int person_id = Integer.parseInt(columns[0]);
            int family_group_id = Integer.parseInt(columns[7]);

            if(fetchFamilyGroup(family_group_id) == null) { 
                FamilyGroup newGroup = new FamilyGroup(family_group_id);

                addFamilyGroup(newGroup);
                newGroup.addFamilyMember(fetchPerson(person_id));
            }

            fetchPerson(person_id).setFamilyGroup(fetchFamilyGroup(family_group_id));

        }
    }

    /**
     * Load inquiries from database
     * @throws SQLException if database query fails
     */
    public void loadInquiries() throws SQLException {
        String rs = fetcher.getEntries("Inquiry", "inquiry_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int inquiry_id = Integer.parseInt(columns[0]);
            int inquirer_id = Integer.parseInt(columns[1]);
            int seeking_id = Integer.parseInt(columns[2]);
            int location_id = Integer.parseInt(columns[3]);
            String date_of_inquiry = columns[4].substring(0, 10);
            String comments = columns[5];

            if(!date_of_inquiry.equals("null")) {
                date_of_inquiry = date_of_inquiry.substring(0, 10);
            }
        
            addInquiry(fetchPerson(inquirer_id), fetchVictim(seeking_id), date_of_inquiry, comments, fetchLocation(location_id));
        }
    }

    /**
     * Load supplies from database
     * @throws SQLException if database query fails
     */
    public void loadSupplies() throws SQLException {
        
        String rs = fetcher.getEntries("Supply", "supply_id");
        ArrayList<Supply> supplies = new ArrayList<>(); // create a temporary supply array
        Supply newSupply = null;

        // get all supplies
        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int id = Integer.parseInt(columns[0]);
            String type = columns[1];
            String comments = columns[2];

            if(type.equalsIgnoreCase("blanket")) {
                newSupply = new Supply(id, type, 1); // assume a quantity of 1
                if(comments.length() > 0) {
                    newSupply.setComments(comments);
                }
            }else if(type.equalsIgnoreCase("water")) {
                newSupply = new Water(id, type, 1); // assume a quantity of 1
                if(comments.length() > 0) {
                    newSupply.setComments(comments);
                }
            }else if (type.equalsIgnoreCase("personal belonging") || type.equals("personal item")) {
                newSupply = new Supply(id, "personal belonging", 1); // assume a quantity of 1
                if(comments.length() > 0) {
                    newSupply.setComments(comments);
                }
            }else if(type.equalsIgnoreCase("cot")) {
                newSupply = new Cot(id, comments.split(" ")[0], comments.split(" ")[1], comments); // assume a quantity of 1
                if(comments.length() > 0) {
                    newSupply.setComments(comments);
                }
            }else {
                throw new SQLException("Invalid supply type from database: " + type);
            }

            supplies.add(newSupply);
        }

        // add supplies to their owners
        rs = fetcher.getEntries("SupplyAllocation");

        rows = rs.split("\n");

        for (String row : rows) {
            if(row.equals("")) {
                return;
            }

            String[] columns = row.split("<\t>");

            if(columns[1].equalsIgnoreCase("null")) {
                columns[1] = "-1";
            }

            if(columns[2].equalsIgnoreCase("null")) {
                columns[2] = "-1";
            }

            int supplyId = Integer.parseInt(columns[0]);
            Supply supply = null;
            for(Supply testSupply : supplies) {
                if(testSupply.getId() == supplyId) {
                    supply = testSupply;
                    break;
                }
            }

            int personId = Integer.parseInt(columns[1]);
            int locationId = Integer.parseInt(columns[2]);
            String dateAllocated = columns[3];

            if(personId != -1){
                if(dateAllocated != null && supply.getType().toLowerCase().equals("water")) {
                    Water tempSupply = (Water) supply;
                    dateAllocated = dateAllocated.substring(0, 10);
                    tempSupply.setAllocationDate(dateAllocated);

                    String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().substring(0, 10);

                    if(!tempSupply.isConsumed(curDate)) {
                        fetchVictim(personId).addPersonalBelonging(tempSupply);
                    }
                }else{
                    fetchVictim(personId).addPersonalBelonging(supply);
                }
            }else if(locationId != -1){
                fetchLocation(locationId).addSupply(supply);
            }
        }
    }

    /**
     * Load medical records from database
     * @throws SQLException if database query fails
     */
    public void loadMedicalRecords() throws SQLException {
        String rs = fetcher.getEntries("MedicalRecord", "medical_record_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            if(columns[0].equals("")){
                return;
            }

            int medical_record_id = Integer.parseInt(columns[0]);
            Location location = fetchLocation(Integer.parseInt(columns[1]));
            DisasterVictim victim = fetchVictim(Integer.parseInt(columns[2]));
            String treatment_details = columns[4];
            String date_of_treatment = columns[3];

            if(!date_of_treatment.equals("null") && date_of_treatment != null) {
                date_of_treatment = date_of_treatment.substring(0, 10);
            }

            fetchVictim(victim.getId()).addMedicalRecord(new MedicalRecord(medical_record_id, location, victim, treatment_details, date_of_treatment));
        }
    }

    /**
     * Reflects a supply transfer from one location to another in database
     * @param victim the victim to transfer the supply to
     * @param supply the supply to transfer
     * @throws SQLException if database query fails
     * @throws IllegalArgumentException if the victim or supply is null
     */
    public void reflectSupplyTransfer(DisasterVictim victim, Posession supply) throws SQLException, IllegalArgumentException {
        victim.transferSupply(supply);
        String query = "DELETE FROM SupplyAllocation WHERE location_id = " + victim.getCurrentLocation().getId() + " AND supply_id = " + supply.getId();
        String[] args = new String[0];
        String[] types = new String[0];
        int result = fetcher.deadEndQuery(query, args, types);
        System.out.println(result);
    }
}
