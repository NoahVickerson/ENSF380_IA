package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WaterTest {
    private Water water;

    @Before
    public void setUp() {
        water = new Water("Water", 1, "2022-01-01");
    }

    @Test
    public void testCtor() {
        assertNotNull(water);
    }

    @Test
    public void testGetAllocationDate() {
        assertEquals("2022-01-01", water.getAllocationDate());
    }

    @Test
    public void testSetAllocationDate() {
        water.setAllocationDate("2022-01-02");
        assertEquals("2022-01-02", water.getAllocationDate());
    }

    @Test
    public void testIsConsumedNotConsumed() {
        water.setAllocationDate("2022-01-02");
        assertFalse(water.isConsumed("2022-01-02"));
    }

    @Test
    public void testIsConsumedConsumed() {
        water.setAllocationDate("2022-01-02");
        assertTrue(water.isConsumed("2022-01-03"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetAllocationDateInvalidDate() {
        water.setAllocationDate("2022/01/02");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCtorInvalidDate() {
        new Water("Water", 1, "2022/01/02");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIsConsumedInvalidDate() {
        water.isConsumed("2022/01/02");
    }

}
