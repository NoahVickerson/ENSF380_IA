package edu.ucalgary.oop;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class TextInputValidator {
    private ArrayList<String> keys;
    private ArrayList<String> translations;
    private static TextInputValidator instance = null;

    public TextInputValidator(String languageFilePath) throws IllegalArgumentException {
        if (instance != null) {
            throw new IllegalArgumentException("TextInputValidator has already been initialized");
        }

        keys = new ArrayList<>();
        translations = new ArrayList<>();
        
        try {
            readLanguageFile(languageFilePath);
            instance = this;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static TextInputValidator getInstance() throws IllegalArgumentException {
        if (instance == null) {
            throw new IllegalArgumentException("TextInputValidator has not been initialized");
        }
        return instance;
    }

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

    public static boolean isValidDateFormat(String date) {
        if(date == null) {
            return true;
        }

        return date.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$") || date.equalsIgnoreCase("null");
    }

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

    public static boolean isValidPhoneNum(String phoneNum) {
        if(phoneNum == null) {
            return true;
        }

        return phoneNum.matches("^[0-9]{3}-[0-9]{3}-[0-9]{4}$") || phoneNum.equalsIgnoreCase("null") || phoneNum.matches("^[0-9]{3}-[0-9]{4}$");
    }

    
    public static boolean isValidQuantity(String quantity){
        if(!isNumeric(quantity)){
            return false;
        }
        return Integer.parseInt(quantity) >= 0;
    }

    public static boolean isValidGrid(String grid) {
        Pattern pattern = Pattern.compile("^[A-Za-z]\\d+$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    public static boolean isValidRoom(String grid) {
        Pattern pattern = Pattern.compile("^[A-Za-z]*$");
        Matcher matcher = pattern.matcher(grid);
        return matcher.matches();
    }

    public static boolean isNumeric(String number) {
        return number.matches("^-?\\d+$");
    }


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
