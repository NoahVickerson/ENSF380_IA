package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonalBelongingTest {
    private PersonalBelonging personalBelonging;
    private String expectedDescription = "description";
    private String expectedComments = "comments";
    private int expectedQuantity = 1;
    private int invalidQuantity = -1;

    @Before
    public void setUp() {
        personalBelonging = new PersonalBelonging("some desc", "some comments", 5);
    }

    @Test
    public void testPersonalBelongingCtor() {
        assertNotNull("PersonalBelonging should not be null", personalBelonging);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorInvalidQuantity() {
        PersonalBelonging newPersonalBelonging = new PersonalBelonging(expectedDescription, expectedComments, invalidQuantity);
    }

    @Test 
    public void testSetDescription() {
        personalBelonging.setDescription(expectedDescription);
        assertEquals("Description should match the one set in setup", expectedDescription, personalBelonging.getDescription());
    }

}
