package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ReliefControllerTest {
    private ReliefController controller;
    private Location[] locations;
    private Person[] people;
    private Inquiry[] inquiries;

    @Before
    public void setUp() {
        controller = ReliefController.getInstance();
        locations = new Location[3];
        locations[0] = new Location("A", "1234 Shelter Ave");
        locations[1] = new Location("B", "4321 Shelter Blvd");
        locations[2] = new Location("C", "5678 Shelter Ct");

        people = new Person[4];
        people[0] = new DisasterVictim("John", "Doe", "2002-01-01", "m", "123-456-7890", "2025-01-01");
        people[1] = new DisasterVictim("Jane",  "Doe", "2001-01-01", "f", "123-456-7890", "2025-01-01");
        people[2] = new DisasterVictim("Joe",  "Doe", "2003-01-01", "m", "123-456-7890", "2025-01-01");
        people[3] = new Person("Jill", "Doe", "2025-01-01", "f", "123-456-7890");

        inquiries = new Inquiry[3];
        inquiries[0] = new Inquiry(people[1], (DisasterVictim) people[0], "2025-01-01", "This is a test inquiry", locations[0]);
        inquiries[1] = new Inquiry(people[2], (DisasterVictim) people[1], "2025-01-01", "This is a test inquiry", locations[1]);
        inquiries[2] = new Inquiry(people[3], (DisasterVictim) people[2], "2025-01-01", "This is a test inquiry", locations[2]);

        for (Person person : people) {
            controller.addPerson(person);
        }

        for (Location location : locations) {
            controller.addLocation(location);
        }

        for (Inquiry inquiry : inquiries) {
            controller.addInquiry(inquiry);
        }
    }
    
    @Test
    public void testSingleton() {
        assertEquals("ReliefController should be a singleton", controller, ReliefController.getInstance());
    }

    @Test
    public void testGetLocations() {
        boolean allFound = true;
        boolean found = false;
        for (Location location : locations) {
            for (Location loc : controller.getLocations()) {
                found = false;
                if (location.equals(loc)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allFound = false;
                break;
            }
        }
        assertTrue("getLocations should return the array of locations", allFound);
    }

    @Test
    public void testGetPeople() {
        boolean allFound = true;
        boolean found = false;
        for (Person person : people) {
            for (Person p : controller.getPeople()) {
                found = false;
                if (person.equals(p)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allFound = false;
                break;
            }
        }
        assertTrue("getPeople should return the array of people", allFound);
    }

    @Test
    public void testGetInquiries() {
        boolean allFound = true;
        boolean found = false;
        for (Inquiry inquiry : inquiries) {
            for (Inquiry i : controller.getInquiries()) {
                found = false;
                if (inquiry.equals(i)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                allFound = false;
                break;
            }
        }
        assertTrue("getInquiries should return the array of inquiries", allFound);
    }

    @Test
    public void testAddDisasterVictimAtomic() {
        controller.addDisasterVictim("Other", "Person", "2002-01-01", "m", "123-456-7890", locations[0], "2025-01-01");

        boolean found = false;
        for (Person person : controller.getPeople()) {
            if (person.getFirstName().equals("Other") && person.getLastName().equals("Person")) {
                found = true;
                break;
            }
        }
        assertTrue("addDisasterVictim should add a new DisasterVictim", found);
    }

    @Test
    public void testAddDisasterVictimObject() {
        DisasterVictim victim = new DisasterVictim("Other", "Person", "2002-01-01", "m", "123-456-7890", "2025-01-01");

        controller.addDisasterVictim(victim);

        boolean found = false;
        for (Person person : controller.getPeople()) {
            if (person.getFirstName().equals("Other") && person.getLastName().equals("Person")) {
                found = true;
                break;
            }
        }
        assertTrue("addDisasterVictim should add a new DisasterVictim", found);
    }

    @Test
    public void testAddInquiryAtomic() {
        controller.addInquiry(people[3], (DisasterVictim)people[2], "2025-01-01", "This is a test inquiry", locations[0]);

        boolean found = false;
        for (Inquiry inquiry : controller.getInquiries()) {
            if (inquiry.getInquirer().equals(people[3]) && inquiry.getMissingPerson().equals(people[2]) && inquiry.getDateOfInquiry().equals("2025-01-01")) {
                found = true;
                break;
            }
        }
        assertTrue("addInquiry should add a new Inquiry", found);
    }

    @Test
    public void testAddInquiryObject() {
        Inquiry neqInq = new Inquiry(people[3], (DisasterVictim)people[2], "2025-01-01", "This is a test inquiry", locations[0]);

        controller.addInquiry(neqInq);

        boolean found = false;
        for (Inquiry inquiry : controller.getInquiries()) {
            if (inquiry.getInquirer().equals(people[3]) && inquiry.getMissingPerson().equals(people[2]) && inquiry.getDateOfInquiry().equals("2025-01-01")) {
                found = true;
                break;
            }
        }
        assertTrue("addInquiry should add a new Inquiry", found);
    }

    @Test
    public void testAddPersonAtomic() {
        controller.addPerson("Other", "Person", "2002-01-01", "m", "123-456-7890");

        boolean found = false;
        for (Person person : controller.getPeople()) {
            if (person.getFirstName().equals("Other") && person.getLastName().equals("Person")) {
                found = true;
                break;
            }
        }
        assertTrue("addPerson should add a new Person", found);
    }

    @Test
    public void testAddPersonObject() {
        Person newP = new Person("Other", "Person", "2002-01-01", "m", "123-456-7890");

        controller.addPerson(newP);

        boolean found = false;
        for (Person person : controller.getPeople()) {
            if (person.getFirstName().equals("Other") && person.getLastName().equals("Person")) {
                found = true;
                break;
            }
        }
        assertTrue("addPerson should add a new Person", found);
    }

    @Test
    public void testAddLocationAtomic() {
        controller.addLocation("Other", "1234 Other Ave");

        boolean found = false;
        for (Location location : controller.getLocations()) {
            if (location.getName().equals("Other") && location.getAddress().equals("1234 Other Ave")) {
                found = true;
                break;
            }
        }
        assertTrue("addLocation should add a new Location", found);
    }

    @Test
    public void testAddLocationObject() {
        Location newL = new Location("Other", "1234 Other Ave");

        controller.addLocation(newL);

        boolean found = false;
        for (Location location : controller.getLocations()) {
            if (location.getName().equals("Other") && location.getAddress().equals("1234 Other Ave")) {
                found = true;
                break;
            }
        }
        assertTrue("addLocation should add a new Location", found);
    }

    @Test 
    public void testFetchPersonAtomic() {
        Person newPerson = new Person("Some", "Name", "2025-02-10", "f", "123-456-7890");
        controller.addPerson(newPerson);
        Person person = controller.fetchPerson("Some", "Name");
        assertEquals("fetchPerson should return the correct person", newPerson, person);
    }

    @Test 
    public void testFetchPersonObject() {
        Person person = controller.fetchPerson(people[1]);
        assertEquals("fetchPerson should return the correct person", people[1], person);
    }

    @Test 
    public void testFetchPersonId() {
        Person person = controller.fetchPerson(people[1]);
        assertEquals("fetchPerson should return the correct person", people[1], person);
    }

    @Test
    public void testFetchInquiryAtomic() {
        Inquiry inquiry = controller.fetchInquiry(inquiries[0].getInquirer(), inquiries[0].getMissingPerson(), inquiries[0].getDateOfInquiry());
        assertEquals("fetchInquiry should return the correct inquiry", inquiries[0], inquiry);
    }

    @Test
    public void testFetchInquiryId() {
        Inquiry inquiry = controller.fetchInquiry(inquiries[0].getId());
        assertEquals("fetchInquiry should return the correct inquiry", inquiries[0], inquiry);
    }

    @Test
    public void testFetchInquiryObject() {
        Inquiry inquiry = controller.fetchInquiry(inquiries[0]);
        assertEquals("fetchInquiry should return the correct inquiry", inquiries[0], inquiry);
    }

    @Test 
    public void testFetchLocationAtomic() {
        Location newLocation = new Location("Test Other Location", "1234 Other Ave");
        controller.addLocation(newLocation);
        Location location = controller.fetchLocation("Test Other Location");
        assertEquals("fetchLocation should return the correct location", newLocation, location);
    }

    @Test 
    public void testFetchLocationObject() {
        Location location = controller.fetchLocation(locations[1]);
        assertEquals("fetchLocation should return the correct location", locations[1], location);
    }

    @Test 
    public void testFetchLocationId() {
        Location location = controller.fetchLocation(locations[1].getId());
        assertEquals("fetchLocation should return the correct location", locations[1], location);
    }
    
}
