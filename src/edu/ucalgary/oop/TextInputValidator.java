/**
 * @author Noah Vickerson
 * Supply.java 
 * @version 1.1
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class TextInputValidator {
    private ArrayList<String> keys;
    private ArrayList<String> translations;

    /**
     * Initializes the TextInputValidator
     * @param languageFilePath of the language file
     * @throws IllegalArgumentException if the TextInputValidator has already been initialized
     * Not a singleton, will replace the existing instance
     */
    public TextInputValidator(String languageFilePath) throws IllegalArgumentException {
        keys = new ArrayList<>();
        translations = new ArrayList<>();
        
        try {
            readLanguageFile(languageFilePath);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Returns the key for a given translation
     * @param translation
     * @return key
     */
    public String translateToKey(String translation) {
        if(translations.contains(translation)){
            return keys.get(translations.indexOf(translation));
        }else if (translations.contains(translation.toLowerCase())) {
            return keys.get(translations.indexOf(translation.toLowerCase()));
        }
        else{
            return null;
        }
    }

    /**
     * Returns the translation for a given key
     * @param key
     * @return translation
     */
    public String translateToLanguage(String key) {
        if(key == null){
            return null;
        }
        key = key.replaceAll(" ", "_");
        if(keys.contains(key)){
            return translations.get(keys.indexOf(key));
        }else{
            return null;
        }
    }

    /**
     * Checks if the date format is valid
     * @param date
     * @return true if valid
     */
    public static boolean isValidDateFormat(String date) {
        if(date == null) {
            return true;
        }

        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || date.equalsIgnoreCase("null");
    }

    /**
     * Checks if the gender is valid
     * @param gender
     * @return
     */
    public static boolean isValidGender(String gender) {
        if(gender == null) {
            return true;
        }

        gender = gender.toLowerCase();
        if (gender.equals("male") || gender.equals("female") || gender.equals("non_binary") || gender.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the phone number is valid
     * @param phoneNum
     * @return true if valid
     */
    public static boolean isValidPhoneNum(String phoneNum) {
        if(phoneNum == null) {
            return true;
        }

        return phoneNum.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$") || phoneNum.equalsIgnoreCase("null") || phoneNum.matches("^[0-9]{3}-[0-9]{4}$");
    }

    /**
     * Checks if the quantity is valid
     * @param quantity
     * @return true if valid
     */
    public static boolean isValidQuantity(String quantity){
        if(!quantity.matches("^\\d+$")){
            return false;
        }
        return Integer.parseInt(quantity) >= 0; // technically redundant
    }

    /**
     * Checks if the grid is valid
     * @param grid
     * @return true if valid
     */
    public static boolean isValidGrid(String grid) {
        Pattern pattern = Pattern.compile("^[A-Za-z]\\d+$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    /**
     * Checks if the room is valid
     * @param grid
     * @return true if valid
     */
    public static boolean isValidRoom(String grid) {
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    /**
     * Checks if the number is valid
     * @param number
     * @return true if valid
     */
    public static boolean isNumeric(String number) {
        return number.matches("^-?\\d+$");
    }

    /**
     * parses language file to get key value pairs
     * @param languageFilePath
     * @throws Exception if file is not valid
     */
    private void readLanguageFile(String languageFilePath) throws Exception {
        Scanner scanner = new Scanner(new FileInputStream(languageFilePath), "UTF-8");
        boolean inTranslation = false;
        String line;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            if(line.matches("") || line.matches("<!--.*-->")){
                continue;
            }else if(line.matches("<!--.*")){
                while(scanner.hasNextLine() && !line.matches("-->"));
                
            }
            if(line.matches("</?translation>")){
                inTranslation = !inTranslation;
            }
            else if(inTranslation){
                if(line.matches("<key>[a-zA-Z0-9_-]+</key>")){
                    keys.add(line.replaceAll("<key>|</key>", ""));
                }
                else if(line.matches("<value>.*</value>")){
                    translations.add(line.replaceAll("<value>|</value>", ""));
                }else{
                    throw new Exception("Error in Translation File");
                }
            }else{
                throw new Exception("Unrecognized line");
            }
        }

        if(keys.size() != translations.size()){
            throw new Exception("Keys and Translations are not the same size");
        }

        
    }
}
