/*
Copyright Ann Barcomb and Khawla Shnaikat, 2024
Licensed under GPL v3
See LICENSE.txt for more information.
*/
package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;


public class LocationTest {
    private Location location;
    private DisasterVictim victim;
    private Supply supply;

    @Before
    public void setUp() {
        // Initializing test objects before each test method
        location = new Location("Amis Shelter", "1234 Amis Shelterve");
        victim = new DisasterVictim("Yaw", "Dwomoh", "2005-01-01", "male", "123-456-7890", "2024-01-01");
        supply = new Supply("Water", 10);
    }

    // Helper method to check if a supply is in the list
    private boolean containsSupply(Posession[] supplies, Posession supplyToCheck) {
        int i;
        for (i = 0; i < supplies.length; i++) {
            if (supplies[i] == supplyToCheck) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testConstructor() {
        assertNotNull("Constructor should create a non-null Location object", location);
        assertEquals("Constructor should set the name correctly", "Amis Shelter", location.getName());
        assertEquals("Constructor should set the address correctly", "1234 Amis Shelterve", location.getAddress());
    }

    @Test
    public void testSetName() {
        String newName = "Shelter B";
        location.setName(newName);
        assertEquals("setName should update the name of the location", newName, location.getName());
    }

    @Test
    public void testSetAddress() {
        String newAddress = "4321 Shelter Blvd";
        location.setAddress(newAddress);
        assertEquals("setAddress should update the address of the location", newAddress, location.getAddress());
    }

    @Test
    public void testAddOccupant() {
        location.addOccupant(victim);
        Occupant[] actualVictim = location.getOccupants();
        assertEquals("addOccupant should add a disaster victim to the occupants list", victim, actualVictim[actualVictim.length-1]);
    }

    @Test
    public void testRemoveOccupant() {
        DisasterVictim victim2 = new DisasterVictim("John", "Smith", "2005-01-01", "male", "123-456-7890", "2024-01-01");
        location.addOccupant(victim2); // Ensure the victim is added first
        location.removeOccupant(victim2);
        Occupant[] actualVictims = location.getOccupants();
        int i;
        boolean success = true;
        for (i = 0; i < actualVictims.length; i++) {
            if (actualVictims[i] == victim2) {
                success = false;
                break;
            } 
        }
        assertTrue("removeOccupant should remove the disaster victim from the occupants list", success);
    }

    @Test
    public void testSetAndGetOccupants() {
        DisasterVictim[] newOccupants = {victim};
        location.setOccupants(newOccupants);
        Occupant[] actualOccupants = location.getOccupants();
        assertEquals("setOccupants should replace the occupants list with the new list", newOccupants, actualOccupants);
    }

    @Test
    public void testAddSupply() {
        location.addSupply(supply);
        assertTrue("addSupply should add a supply to the supplies list", containsSupply(location.getSupplies(), supply));
    }

    @Test
    public void testRemoveSupply() {
        location.addSupply(supply); // Ensure the supply is added first
        location.removeSupply(supply);
        assertFalse("removeSupply should remove the supply from the supplies list", containsSupply(location.getSupplies(), supply));
    }

    @Test
    public void testSetAndGetSupplies() {
        Supply[] newSupplies = { supply };
        location.setSupplies(newSupplies);
        Posession[] actualSupplies = location.getSupplies();

        assertEquals("setSupplies should replace the supplies list with the new list", actualSupplies, newSupplies);
    }
}
