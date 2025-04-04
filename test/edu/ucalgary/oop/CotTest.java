package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CotTest {
    private Cot cot;
    private String invalidGrid = "10";
    private String validGrid = "A10";
    private String comments = "Comments";
    private String expectedRoom = "115";

    @Before
    public void setUp() {
        cot = new Cot("110", "A1", comments);
    }

    @Test
    public void testCotCtor() {
        Cot newcot = new Cot(expectedRoom, validGrid, comments);
        assertEquals("Cot number should be 115", expectedRoom, newcot.getRoom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCotCtorInvalidGridNumber() {
        Cot newcot = new Cot(expectedRoom, invalidGrid, comments);
    }

    @Test
    public void testSetRoom() {
        cot.setRoom(expectedRoom);
        assertEquals("Cot number should be 115", expectedRoom, cot.getRoom());
    }

    @Test
    public void testSetGrid() {
        cot.setGrid(validGrid);
        assertEquals("Grid should be A10", validGrid, cot.getGrid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGridInvalidData() {
        cot.setGrid(invalidGrid);
    }

    @Test
    public void testInheritance() {
        assertEquals("type should be cot", "Cot", cot.getType());
    }
}
