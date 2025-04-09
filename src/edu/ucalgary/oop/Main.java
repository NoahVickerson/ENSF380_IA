/**
 * @author Noah Vickerson
 * Main.java 
 * @version 1.2
 * @date Mar 30 2025
 */

package edu.ucalgary.oop;

import java.util.Scanner;
import java.awt.*;
import java.io.File;

public class Main {
    private static final String[] languages = {"en-CA"};

    /**
     * Main entry point of program
     * @param args
     */
    public static void main(String[] args) {
        // select the proper language
         String language = "";
        // Scanner cin = new Scanner(System.in);
        
        // System.out.println("Enter a language or number from the list: ");
        // System.out.println("Languages: ");

        // for(int i = 0; i < languages.length; i++) {
        //     System.out.println((i + 1) + ": " + languages[i]);
        // }

        // language = cin.nextLine();

        // if(language.matches("^\\d+$") && Integer.parseInt(language) > 0 && Integer.parseInt(language) <= languages.length) {
        //     language = languages[Integer.parseInt(language) - 1];
        // }else if(language.matches("^[a-z]{2}-[A-Z]{2}$")) {
        //     boolean valid = false;
        //     for(int i = 0; i < languages.length; i++) {
        //         if(languages[i].equals(language)) {
        //             valid = true;
        //         }
        //     }

        //     if(!valid) {
        //         System.out.println( language + " is not a valid language.");
        //         language = "en-CA";
        //     }
        // }else{
        //     System.out.println( language + " is not a valid language.");
        //     language = "en-CA";
        // }

        // cin.close();

        TextInputValidator validator;

        try{
            language = args[0];
            validator = new TextInputValidator("../data/" + language + ".xml");  // get the text file from the data dir
        }catch(Exception e) {
            System.out.println("Invalid language file: " + language + ".xml");
            language = "en-CA";
            validator = new TextInputValidator("../data/" + language + ".xml");  // get the text file from the data dir
        }

        System.out.println(validator.translateToLanguage("using_lng") + language);

        UserInterface ui = new UserInterface(new ErrorLogger("errorlog.txt"), validator);

        EventQueue.invokeLater(() -> {
            ui.setVisible(true);        
        });
    }
}
