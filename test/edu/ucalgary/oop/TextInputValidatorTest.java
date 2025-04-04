package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileOutputStream;

public class TextInputValidatorTest {
    private TextInputValidator validator;
    private String validXml = "<translation>\n" + //
                "  <key>gender_man</key>\n" + //
                "  <value>Man</value>\n" + //
                "</translation>\n" + //
                "\n" + //
                "<translation>\n" + //
                "  <key>gender_woman</key>\n" + //
                "  <value>Woman</value>\n" + //
                "</translation>\n" + //
                "\n" + //
                "<translation>\n" + //
                "  <key>gender_nb</key>\n" + //
                "  <value>Non-binary person</value>\n" + //
                "</translation>\n" + //
                "\n" + //
                "<translation>\n" + //
                "  <key>input_firstname</key>\n" + //
                "  <value>Enter the person's first name:</value>\n" + //
                "</translation>\n" + //
                "\n" + //
                "<translation>\n" + //
                "  <key>report_person</key>\n" + //
                "  <value>%s entered the %s facility on %s.</value>\n" + //
                "</translation>\n" + //
                "";

    @Before
    public void setUp() {
        // create and invalid file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("invalid.xml");
            fos.write("invalid".getBytes());
            fos.close();
            fos = new FileOutputStream("en-CA.xml");
            fos.write(validXml.getBytes());
            validator = new TextInputValidator("en-CA.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCtor() {
        assertNotNull("TextInputValidator should not be null", validator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtorInvalidFile() {
        TextInputValidator invalidValidator = new TextInputValidator("invalid.xml");
    }

    @Test
    public void testIsNumericNumber() {
        assertTrue("123 should be numeric", validator.isNumeric("123"));
    }

    @Test
    public void testIsNumericNotNumber() {
        assertFalse("abc should not be numeric", validator.isNumeric("abc"));
    }

    @Test
    public void testTranslateToLanguage() {
        assertEquals("Key should translate to language", "Man", validator.translateToLanguage("gender_man"));
    }

    @Test
    public void testTranslateToKey() {
        assertEquals("Translation should relate to key","gender_man", validator.translateToKey("Man"));
    }

    @Test
    public void testIsValidDateFormat() {
        assertTrue("2025-02-10 should be valid", validator.isValidDateFormat("2025-02-10"));
    }

    @Test
    public void testIsValidDateFormatInvalidDate() {
        assertTrue("2025/02/10 should not be valid", !validator.isValidDateFormat("2025/02/10"));
    }

    @Test
    public void testIsValidQuantity() {
        assertTrue("0 should be valid", validator.isValidQuantity("0"));
    }

    @Test
    public void testIsValidQuantityInvalidQuantity() {
        assertFalse("-1 should not be valid", validator.isValidQuantity("-1"));
    }

    @Test
    public void testIsValidGrid() {
        assertTrue("A1 should be valid", validator.isValidGrid("A1"));
    }

    @Test
    public void testIsValidGridInvalidGrid() {
        assertFalse("A should not be valid", validator.isValidGrid("A"));
    }

    @Test
    public void testIsValidGender() {
        assertTrue("male should be valid", validator.isValidGender("male"));
    }

    @Test
    public void testIsValidGenderInvalidGender() {
        assertFalse("x should not be valid", validator.isValidGender("x"));
    }


}
