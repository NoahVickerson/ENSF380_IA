/**
 * @author Noah Vickerson
 * ErrorLogger.java 
 * @version 1.0
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.io.*;
import java.util.Date;

public class ErrorLogger {
    private String fileName;
    private RandomAccessFile logFile;

    /**
     * Constructor
     * @param fileName
     */
    public ErrorLogger(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Logs an error, formats it and writes it to the log file
     * @param e the error to log
     */
    public void logError(Exception e) {
        try {
            logFile = new RandomAccessFile(fileName, "rw");
            logFile.seek(logFile.length());
            logFile.writeChars((new Date()).toString() + ": " + e.toString() + "\n\n");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        finally {
            try {
                if (logFile != null){
                    logFile.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
