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

    private ReliefController(String url, String username, String password) throws SQLException, IllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        String dateString = currentDate.toString(); // get current date for water

        validator = TextInputValidator.getInstance();

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
    }

    private ReliefController() {
        // for testing without a database dependency
    }

    public static ReliefController getInstance(String uname, String pword) throws IllegalArgumentException, SQLException {
        if(controller == null) {
            controller = new ReliefController("jdbc:postgresql://localhost/ensf380project", uname, pword);
        }
        return controller;
    }

    public static ReliefController getInstance() {
        if(controller == null) {
            controller = new ReliefController(); // for testing decoupled from the database
        }
        return controller;
    }

    public Location[] getLocations() {
        return locations;
    }

    public Person[] getPeople() {
        return people;
    }

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

    public Inquiry[] getInquiries() {
        return inquiries;
    }

    public FamilyGroup[] getFamilyGroups() {
        return familyGroups;
    }

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

    public void addFamilyGroup(FamilyGroup familyGroup) {
        FamilyGroup[] newFamilyGroups = new FamilyGroup[familyGroups.length + 1];
        for(int i = 0; i < familyGroups.length; i++) {
            newFamilyGroups[i] = familyGroups[i];
        }
        newFamilyGroups[familyGroups.length] = familyGroup;
        familyGroups = newFamilyGroups;
    }

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

    public Person fetchPerson(Person person) {
        for (Person p : people) {
            if (p.equals(person)) {
                return p;
            }
        }
        return null;
    }

    public Person fetchPerson(String firstName, String lastName) {
        for (Person p : people) {
            if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)) {
                return p;
            }
        }
        return null;
    }

    public Person fetchPerson(int id) {
        for (Person p : people) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public DisasterVictim fetchVictim(int id) {
        for (Person p : people) {
            if (p.getId() == id && p instanceof DisasterVictim) {
                return (DisasterVictim) p;
            }
        }
        return null;
    }

    public DisasterVictim fetchVictim(String firstName, String lastName) {
        for (Person p : people) {
            if (p.getFirstName().equals(firstName) && p.getLastName().equals(lastName) && p instanceof DisasterVictim) {
                return (DisasterVictim) p;
            }
        }
        return null;
    }

    public Location fetchLocation(Location location) {
        for (Location l : locations) {
            if (l.equals(location)) {
                return l;
            }
        }
        return null;
    }

    public Location fetchLocation(String name) {
        for (Location l : locations) {
            if (l.getName().equals(name)) {
                return l;
            }
        }
        return null;
    }

    public Location fetchLocation(int id) {
        for (Location l : locations) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    public Inquiry fetchInquiry(Inquiry inquiry) {
        for (Inquiry i : inquiries) {
            if (i.equals(inquiry)) {
                return i;
            }
        }
        return null;
    }

    public Inquiry fetchInquiry(Person inquirer, DisasterVictim victim, String date) {
        for (Inquiry i : inquiries) {
            if (i.getInquirer().equals(inquirer) && i.getMissingPerson().equals(victim) && i.getDateOfInquiry().equals(date)) {
                return i;
            }
        }
        return null;
    }

    public Inquiry fetchInquiry(int id) {
        for (Inquiry i : inquiries) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }   

    public FamilyGroup fetchFamilyGroup(int id) {
        for (FamilyGroup f : familyGroups) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }

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

    public void loadLocations() throws SQLException, IllegalArgumentException {
        String rs = fetcher.getEntries("Location", "location_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            int location_id = Integer.parseInt(columns[0]);
            String name = columns[1];
            String address = columns[2];

            Location newLocation = new Location(location_id, name, address);

            addLocation(newLocation);
        }
    }

    public void loadPeople() throws SQLException {
        String rs = fetcher.getEntries("Person", "person_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

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

    public void loadCurrentLocations() throws SQLException {
        String rs = fetcher.getEntries("PersonLocation");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

            int person_id = Integer.parseInt(columns[0]);
            int location_id = Integer.parseInt(columns[1]);

            fetchVictim(person_id).setCurrentLocation(fetchLocation(location_id));
        }
    }
    
    public void loadFamilyGroups() throws SQLException {

        String rs = fetcher.getEntries("Person", "person_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

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

    public void loadInquiries() throws SQLException {
        String rs = fetcher.getEntries("Inquiry", "inquiry_id");

        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

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

    public void loadSupplies() throws SQLException {
        
        String rs = fetcher.getEntries("Supply", "supply_id");
        ArrayList<Supply> supplies = new ArrayList<>(); // create a temporary supply array
        Supply newSupply = null;

        // get all supplies
        String[] rows = rs.split("\n");

        for (String row : rows) {
            String[] columns = row.split("<\t>");

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
}
