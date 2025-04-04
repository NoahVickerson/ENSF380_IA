package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.regex.*;

import java.io.*;

public class ErrorLoggerTest {
    private ErrorLogger logger;
    private String logFileName = "testlog.txt";
    private RandomAccessFile logFile;
    String initialMessage;

    @Before
    public void setUp(){
        try {
            logger = new ErrorLogger(logFileName);
            logFile = new RandomAccessFile(logFileName, "rw"); 
            logFile.setLength(0);
            initialMessage = new IllegalArgumentException("Initial error").toString();
            logFile.writeChars(initialMessage + "\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogError(){
        String errorMessage1;

        try {
            throw new IllegalArgumentException("Test error message");
        }
        catch (Exception e) {
            logger.logError(e);
            errorMessage1 = e.toString();
        }

        try {
            String line = logFile.readLine();
            line = line.replaceAll("[^\\p{Print}]", "");        // remove any non-printable characters to make the comparison accurate
            assertEquals("Error message should be logged", errorMessage1, line);
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue("Log file not found", false);
        }
    }

    @Test
    public void testLogDoesntOverwrite(){
        String errorMessage1;

        try {
            throw new IllegalArgumentException("Test error message");
        }
        catch (Exception e) {
            logger.logError(e);
            errorMessage1 = e.toString();
        }

        try {
            logFile.seek(0);
            String line = logFile.readLine();
            line = line.replaceAll("[^\\p{Print}]", "");        // remove any non-printable characters to make the comparison accurate
            assertEquals("Initial content in the file must not change", initialMessage, line);
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue("Log file not found", false);
        }
    }


    @After
    public void tearDown(){
        try {
            logFile.setLength(0);
            logFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
