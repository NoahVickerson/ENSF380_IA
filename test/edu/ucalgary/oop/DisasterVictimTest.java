package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisasterVictimTest {
    private DisasterVictim victim;
    private Location otherLocation;
    private Location currentLocation;
    private Supply otherSupply;

    @Before
    public void setUp() {
        victim = new DisasterVictim("Freda", "Kahlo", "2002-01-23", "w", "123-456-7890", "2025-01-23");
        otherLocation = new Location("Shelter A", "1234 Shelter Ave");
        currentLocation = new Location("Shelter B", "150 8 Ave NW ");
        otherSupply = new PersonalBelonging("desc", "comments", 10);
        otherLocation.addSupply(otherSupply);

        victim.setCurrentLocation(currentLocation);
    }

    @Test
    public void testCtor() {
        assertNotNull(victim);
    }

    @Test
    public void testGetEntryDate() {
        assertEquals("getEntryDate should return the expected entry date", "2025-01-23", victim.getEntryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidDates() {
        DisasterVictim newVictim = new DisasterVictim("Freda", "Kahlo", "2002/01/23", "w", "123-456-7890", "2025/01/23");
    }

    @Test
    public void testInheritance() {
        assertEquals("disastervictim", victim.getType());
    }

    @Test
    public void testSetPersonalBelongings() {
        Supply[] belongings = new Supply[2];
        belongings[0] = new Water("comments", 10, "2025-01-23");
        belongings[1] = new PersonalBelonging("desc", "comments", 10);

        victim.setPersonalBelongings(belongings);
        assertEquals(belongings, victim.getPersonalBelongings());
    }

    @Test
    public void testAddPersonalBelonging() {
        Supply supply = new Water("comments", 10, "2025-01-23");
        victim.addPersonalBelonging(supply);

        boolean found = false;
        for(Supply belonging : victim.getPersonalBelongings()) {
            if(belonging == supply) {
                found = true;
                break;
            }
        }
        assertTrue("addPersonalBelonging should add the supply to personal belongings", found);
    }

    @Test
    public void testRemovePersonalBelonging() {
        Supply supply = new Water("comments", 10, "2025-01-23");
        victim.addPersonalBelonging(supply);
        victim.removePersonalBelonging(supply);

        boolean found = false;
        for(Supply belonging : victim.getPersonalBelongings()) {
            if(belonging == supply) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testSetMedicalRecords() {
        MedicalRecord[] records = new MedicalRecord[2];
        records[0] = new MedicalRecord(currentLocation, victim, "test for strep", "2025-02-09");
        records[1] = new MedicalRecord(otherLocation, victim, "test for hep", "2025-02-10");

        victim.setMedicalRecords(records);
        assertEquals(records, victim.getMedicalRecords());
    }

    @Test
    public void testAddMedicalRecord() {
        MedicalRecord record = new MedicalRecord(currentLocation, victim, "test for strep", "2025-02-09");
        victim.addMedicalRecord(record);

        boolean found = false;
        for(MedicalRecord findRecord : victim.getMedicalRecords()) {
            if(findRecord == record) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testRemoveMedicalRecord() {
        MedicalRecord record = new MedicalRecord(currentLocation, victim, "test for strep", "2025-02-09");
        victim.addMedicalRecord(record);
        victim.removeMedicalRecord(record);

        boolean found = false;
        for(MedicalRecord findRecord : victim.getMedicalRecords()) {
            if(findRecord == record) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test 
    public void testTransferSupply() {
        Supply supplyToTransfer = new Water("comments", 10, "2025-01-23");
        currentLocation.addSupply(supplyToTransfer);
        victim.transferSupply(supplyToTransfer);

        boolean found = false;
        for(Supply belonging : victim.getPersonalBelongings()) {
            if(belonging == supplyToTransfer) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferSupplyInvalidSupply() {
        victim.transferSupply(otherSupply);
    }

    @Test
    public void testRemoveUsedSupplies() {
        Supply expiredSupply = new Water("comments", 10, "2025-01-23");
        String currentDate = "2025-01-27";  // if we're allowed to use java.time.LocalDate;, use that

        victim.addPersonalBelonging(expiredSupply);

        victim.removeUsedSupplies(currentDate);

        boolean found = false;
        for(Supply belonging : victim.getPersonalBelongings()) {
            if(belonging == expiredSupply) {
                found = true;
                break;
            }
        }
        assertFalse(found);
    }

    @Test
    public void testGetCurrentLocation() {
        assertEquals(currentLocation, victim.getCurrentLocation());
    }

    @Test
    public void testSetCurrentLocation() {
        victim.setCurrentLocation(otherLocation);
        assertEquals(otherLocation, victim.getCurrentLocation());
    }

}
