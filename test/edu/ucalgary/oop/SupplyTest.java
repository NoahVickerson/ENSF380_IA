package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SupplyTest {
    private Supply supply;

    @Before
    public void setUp() {
        supply = new Supply("Blanket", 5);
    }

    @Test
    public void testSupplyCtor() {
        Supply newSupply = new Supply("Blanket", 5);
        assertEquals(" Quantity should match the one set", 5, newSupply.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupplyCtorInvalidQuantity() {
        Supply newSupply = new Supply("Blanket", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupplyCtorInvalidType() {
        Supply newSupply = new Supply("different", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSupplyCtorInvalidId() {
        Supply newSupply = new Supply(0, "Blanket", 5);
    }

    @Test
    public void testGetId() {
        Supply newSupply = new Supply(100, "Blanket", 5);
        assertEquals("getId should return the correct id", 100, newSupply.getId());
    }

    @Test
    public void testGetQuantity() {
        assertEquals("getQuantity should return the correct quantity", 5, supply.getQuantity());
    }

    @Test
    public void testSetQuantity() {
        supply.setQuantity(20);
        assertEquals("setQuantity should update the quantity", 20, supply.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetQuantityInvalidQuantity() {
        supply.setQuantity(-1);
    }

    @Test
    public void testUniqueId() {
        Supply newSupply = new Supply("Blanket", 5);
        assertNotEquals("Supply should have a unique id", supply.getId(), newSupply.getId());
    }    

    @Test 
    public void testSetComments() {
        supply.setComments("Comments");
        assertEquals("Comments should be updated", "Comments", supply.getComments());
    }

    @Test 
    public void testGetType() {
        assertEquals("getType should return the correct type", "Blanket", supply.getType());
    }
}
