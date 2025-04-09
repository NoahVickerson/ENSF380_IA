package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InquiryTest {
    private Person inquirer;
    private Person expectedInquirer;
    private Location lastSeen;
    private Location expectedLastSeen;
    private DisasterVictim victim;
    private DisasterVictim secondVictim;
    private Inquiry inquiry;
    private String validDate = "2025-02-10";
    private String expectedDate = "2025-02-11";
    private String invalidDate = "2025/02/10";
    private String expectedInfo = "Looking for family member";


    @Before
    public void setUp() {
        inquirer = new Person("John", "Doe", validDate, "male", "123-456-7890");
        expectedInquirer = new Person("James", "Smith", validDate, "male", "123-456-7890");
        lastSeen = new Location("University of Calgary", "2500 University Dr NW");
        expectedLastSeen = new Location("UofC", "2500 University Dr NW");
        victim = new DisasterVictim("Jane", "Doe", validDate, "male", "123-456-7891", "2025-02-10");
        inquiry = new Inquiry(inquirer, victim, validDate, expectedInfo, lastSeen);
        secondVictim = new DisasterVictim("Jane", "Doe", validDate, "male", "123-456-7891", "2025-02-10");
    }

    @Test 
    public void testInquiryCtor() {
        Inquiry newInquiry = new Inquiry(inquirer, secondVictim, validDate, expectedInfo, lastSeen);

        assertEquals("Victim should match the new one set", secondVictim, newInquiry.getMissingPerson());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInquiryCtorWithInvalidDate() {
        Inquiry newInquiry = new Inquiry(inquirer, secondVictim, invalidDate, expectedInfo, lastSeen);
    }

    @Test
    public void testGetInquirer() {
        assertEquals("Inquirer should match the one set in setup", inquirer, inquiry.getInquirer());
    }

    @Test
    public void testGetMissingPerson() {
        assertEquals("Victim should match the one set in setup", victim, inquiry.getMissingPerson());
    }

    @Test
    public void testGetDateOfInquiry() {
        assertEquals("Date of inquiry should match the one set in setup", validDate, inquiry.getDateOfInquiry());
    }

    @Test
    public void testGetInfoProvided() {
        assertEquals("Info provided should match the one set in setup", expectedInfo, inquiry.getInfoProvided());
    }

    @Test
    public void testGetLastKnownLocation() {
        assertEquals("Last known location should match the one set in setup", lastSeen, inquiry.getLastKnownLocation());
    }

    @Test
    public void testSetInquirer() {
        inquiry.setInquirer(expectedInquirer);
        assertEquals("Inquirer should match the one set in setup", expectedInquirer, inquiry.getInquirer());
    }

    @Test
    public void testSetInfoProvided() {
        inquiry.setInfoProvided(expectedInfo);
        assertEquals("Info provided should match the one set in setup", expectedInfo, inquiry.getInfoProvided());
    }

    @Test
    public void testSetLastKnownLocation() {
        inquiry.setLastKnownLocation(expectedLastSeen);
        assertEquals("Last known location should match the one set in setup", expectedLastSeen, inquiry.getLastKnownLocation());
    }

}
