package edu.ucalgary.oop;

import java.io.*;
import java.util.Random;

public class ErrorLogger {
    private String fileName;
    private RandomAccessFile logFile;

    public ErrorLogger(String fileName) {
        this.fileName = fileName;
    }

    public static void main (String[] args) {
        ErrorLogger logger = new ErrorLogger("errorlog.txt");
        try {
            throw new IllegalArgumentException("Test error message");
        }
        catch (Exception e) {
            logger.logError(e);
        }
    }

    public void logError(Exception e) {
        try {
            logFile = new RandomAccessFile(fileName, "rw");
            logFile.seek(logFile.length());
            logFile.writeChars(e.toString() + "\n\n");
            e.printStackTrace();
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
