package edu.ucalgary.oop;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

public class UserInterface extends JFrame{
    private ReliefController controller;
    private ErrorLogger logger;
    private TextInputValidator validator;
    private String uname;
    private String pword;
    private boolean loggedIn = false; // dont try and run program until logged in

    public UserInterface(ErrorLogger logger, TextInputValidator validator) {
        super("Relief Services System");

        setSize(500,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.logger = logger;
        this.validator = validator;

        while(true){
            try {
                runLoginSequence();
            }catch (Exception e) {
                logger.logError(e);
                exit("Error logging in", 1);
            }

            try {
                controller = ReliefController.getInstance(uname, pword);
            }catch (IllegalArgumentException e) {
                displayError(validator.translateToLanguage("upass_incorrect") + "\n" + e.getMessage());
                loggedIn = false;
                continue;
            }catch (SQLException e) {
                logger.logError(e);
                exit(validator.translateToLanguage("db_err") + "\n" + e.getMessage(), 1);
            }catch (Exception e) {
                e.printStackTrace();
                logger.logError(e);
                exit(validator.translateToLanguage("uncaught_exc") + e.getMessage(), 1);
            }

            break;

        }

        try{
            mainMenu();
        }catch (Exception e) {
            logger.logError(e);
            exit(validator.translateToLanguage("uncaught_exc") + e.getMessage(), 1);
        }
    }

    public void runLoginSequence() {
        JLabel login_prompt = new JLabel(validator.translateToLanguage("login_prompt"));
        JLabel unameLabel = new JLabel(validator.translateToLanguage("uname_prompt"));
        JLabel pwordLabel = new JLabel(validator.translateToLanguage("pword_prompt"));

        JTextField unameInput = new JTextField("oop", 15);
        JPasswordField pwordInput = new JPasswordField("ucalgary", 15);

        String login = validator.translateToLanguage("login");
        JButton loginButton = new JButton(login);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout());
        headerPanel.add(login_prompt);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());

        loginPanel.add(unameLabel);
        loginPanel.add(unameInput);
        loginPanel.add(pwordLabel);
        loginPanel.add(pwordInput);

        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new FlowLayout());
        submitPanel.add(loginButton);

        ActionListener loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                uname = unameInput.getText();
                pword = String.valueOf(pwordInput.getPassword());
                loggedIn = true;
            }
        };

        loginButton.addActionListener(loginListener);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(loginPanel, BorderLayout.CENTER);
        this.add(submitPanel, BorderLayout.SOUTH);
        this.setVisible(true);

        while(!loggedIn){               // stall until logged in
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.remove(headerPanel);
        this.remove(loginPanel);
        this.remove(submitPanel);
    }

    public void mainMenu() {

        String[] menuItems = {validator.translateToLanguage("data_view"), validator.translateToLanguage("data_update"), validator.translateToLanguage("data_entry")};

        JPanel comboBoxPanel = new JPanel(new FlowLayout());
        JComboBox menuDropdown = new JComboBox(menuItems);

        menuDropdown.setEditable(false);

        comboBoxPanel.add(new JLabel(validator.translateToLanguage("choose_action")));
        comboBoxPanel.add(menuDropdown);

        // create the content panel
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(dataViewMenu(), menuItems[0]);
        contentPanel.add(dataEntryMenu(), menuItems[2]);
        contentPanel.add(dataUpdateMenu(), menuItems[1]);

        JPanel exitPanel = new JPanel(new FlowLayout());
        JButton exitButton = new JButton(validator.translateToLanguage("exit"));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(validator.translateToLanguage("exit_success"), 0);
            }
        });
        exitPanel.add(exitButton);

        this.add(comboBoxPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.setVisible(true);

        menuDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, (String) e.getItem());
            }
        });

    }

    public JPanel dataEntryMenu() {
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));

        String[] menuItems = {validator.translateToLanguage("victim"), validator.translateToLanguage("inquiry"), validator.translateToLanguage("supply")};

        JComboBox menuDropdown = new JComboBox(menuItems);
        menuDropdown.setEditable(false);
        menuDropdown.setSize(50, 50);

        entryPanel.add(new JLabel(validator.translateToLanguage("choose_type")));
        entryPanel.add(menuDropdown);

        JPanel form = new JPanel(new CardLayout());

        form.add(createPerson(), menuItems[0]);
        form.add(createInquiry(), menuItems[1]);
        form.add(createSupply(), menuItems[2]);

        menuDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) form.getLayout();
                cl.show(form, (String) e.getItem());
            }
        });

        entryPanel.add(form);

        return entryPanel;
    }

    public JPanel dataUpdateMenu() {
        JPanel updatePanel = new JPanel();

        updatePanel.add(new JLabel("Update"));

        return updatePanel;
    }

    public JPanel dataViewMenu() {
        JPanel viewPanel = new JPanel();

        viewPanel.add(new JLabel("View"));

        return viewPanel;
    }

    public JPanel createInquiry() {
        JPanel inquiryPanel = new JPanel();

        JLabel createInquiry = new JLabel("Create Inquiry");
        inquiryPanel.add(createInquiry);

        return inquiryPanel;
    }

    public JPanel createPerson() {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));

        JLabel createPerson = new JLabel(validator.translateToLanguage("create_victim"));
        personPanel.add(createPerson);

        String[] genderOptions = {validator.translateToLanguage("male"), validator.translateToLanguage("female"), validator.translateToLanguage("non_binary")};

        JLabel firstName = new JLabel(validator.translateToLanguage("fname"));
        JTextField firstNameInput = new JTextField(15);
        JLabel lastName = new JLabel(validator.translateToLanguage("lname"));
        JTextField lastNameInput = new JTextField(15);
        JLabel dateOfBirth = new JLabel(validator.translateToLanguage("dob"));
        JTextField dateOfBirthInput = new JTextField(validator.translateToLanguage("ex_date"), 15);
        JLabel gender = new JLabel(validator.translateToLanguage("gender"));
        JComboBox genderInput = new JComboBox(genderOptions);
        JLabel phoneNum = new JLabel(validator.translateToLanguage("phone"));
        JTextField phoneNumInput = new JTextField(15);
        JLabel entryDate = new JLabel(validator.translateToLanguage("entry_date"));
        JTextField entryDateInput = new JTextField(validator.translateToLanguage("ex_date"), 15);

        JLabel currentLocation = new JLabel(validator.translateToLanguage("location"));
        JPanel locationPanel = new JPanel(new CardLayout());
        StringBuilder newLocationName = new StringBuilder();
        StringBuilder newLocationAddress = new StringBuilder();
        
        Location[] locations = controller.getLocations();
        ArrayList<String> locationNames = new ArrayList<String>();
        String[] locationNamesArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationNames.add(locations[i].getName());
            locationNamesArray[i] = locations[i].getName();
            locationPanel.add(new JPanel(), locationNames.get(i));
        }
        locationNamesArray[locations.length] = validator.translateToLanguage("new_location");
        locationPanel.add(createLocation(newLocationName, newLocationAddress), locationNamesArray[locations.length]);
        
        JComboBox currentLocationInput = new JComboBox(locationNamesArray);
        
        currentLocationInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) locationPanel.getLayout();
                cl.show(locationPanel, (String) e.getItem());
            }
        });

        personPanel.add(firstName);
        personPanel.add(firstNameInput);
        personPanel.add(lastName);
        personPanel.add(lastNameInput);
        personPanel.add(dateOfBirth);
        personPanel.add(dateOfBirthInput);
        personPanel.add(gender);
        personPanel.add(genderInput);
        personPanel.add(phoneNum);
        personPanel.add(phoneNumInput);
        personPanel.add(entryDate);
        personPanel.add(entryDateInput);
        personPanel.add(currentLocation);
        personPanel.add(currentLocationInput);
        personPanel.add(locationPanel);

        JButton submitButton = new JButton(validator.translateToLanguage("submit"));
        personPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameInput.getText();
                String lastName = lastNameInput.getText();
                String dateOfBirth = dateOfBirthInput.getText();
                String gender = genderInput.getSelectedItem().toString();
                String phoneNum = phoneNumInput.getText();
                String entryDate = entryDateInput.getText();
                String currentLocation = currentLocationInput.getSelectedItem().toString();

                Location location = null;
                try {
                    if(!validator.isValidPhoneNum(phoneNum)){
                        displayError(validator.translateToLanguage("create_victim_err") + "\n" + validator.translateToLanguage("try_again"));
                        return;
                    }

                    if (currentLocation.equals(validator.translateToLanguage("new_location"))) {
                        if(newLocationName.length() == 0 || newLocationAddress.length() == 0){
                            displayError(validator.translateToLanguage("create_location_err") + "\n" + validator.translateToLanguage("try_again"));
                            return;
                        }else if (controller.fetchLocation(newLocationName.toString()) != null){ 
                            location = controller.fetchLocation(newLocationName.toString());
                        }else{
                            try {
                                location = new Location(newLocationName.toString(), newLocationAddress.toString());
                            }catch(IllegalArgumentException e1){
                                displayError(validator.translateToLanguage("create_location_err") + e1.getMessage() + "\n" + validator.translateToLanguage("try_again"));
                                return;
                            }
                            controller.addLocation(location);
                            location.addEntry();
                        }
                    }else{
                        location = locations[locationNames.indexOf(currentLocation)];
                    }
                
                    DisasterVictim person = new DisasterVictim(firstName, lastName, dateOfBirth, gender, phoneNum, entryDate);
                    person.setCurrentLocation(location);
                    controller.addDisasterVictim(person);
                    person.addEntry();
                    displayError(validator.translateToLanguage("person_created"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("create_person_err") + e1.getMessage() + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        return personPanel;
    }

    public JPanel createLocation(StringBuilder name, StringBuilder address) {
        JPanel locationPanel = new JPanel();

        JLabel locationName = new JLabel(validator.translateToLanguage("loc_name"));
        JTextField locationNameInput = new JTextField(15);
        JLabel locationAddress = new JLabel(validator.translateToLanguage("loc_adr"));
        JTextField locationAddressInput = new JTextField(15);

        locationPanel.add(locationName);
        locationPanel.add(locationNameInput);
        locationPanel.add(locationAddress);
        locationPanel.add(locationAddressInput);

        JButton submitButton = new JButton(validator.translateToLanguage("add_loc"));
        locationPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setLength(0);
                address.setLength(0);
                name.append(locationNameInput.getText());
                address.append(locationAddressInput.getText());
            }
        });

        return locationPanel;
    }

    public JPanel createMedicalRecord() {
        JPanel medicalRecordPanel = new JPanel();

        JLabel createMedicalRecord = new JLabel(validator.translateToLanguage("create_record"));
        medicalRecordPanel.add(createMedicalRecord);

        return medicalRecordPanel;
    }

    public JPanel createSupply() {
        JPanel supplyPanel = new JPanel();
        supplyPanel.setLayout(new BoxLayout(supplyPanel, BoxLayout.Y_AXIS));

        JLabel createSupply = new JLabel(validator.translateToLanguage("create_supply"));
        supplyPanel.add(createSupply);

        supplyPanel.setLayout(new BoxLayout(supplyPanel, BoxLayout.Y_AXIS));

        JLabel type = new JLabel(validator.translateToLanguage("type_prompt"));
        String[] types = {validator.translateToLanguage("water"), validator.translateToLanguage("blanket"), validator.translateToLanguage("cot"), validator.translateToLanguage("belonging")};

        JComboBox typeInput = new JComboBox(types);
        supplyPanel.add(type);
        supplyPanel.add(typeInput);

        JPanel supplyInputs = new JPanel(new CardLayout());

        JLabel quantityWater = new JLabel(validator.translateToLanguage("quantity"));
        JTextField quantityInputWater = new JTextField(15);
        JLabel commentsWater = new JLabel(validator.translateToLanguage("comments"));
        JTextField commentsInputWater = new JTextField(15);

        JPanel waterPanel = new JPanel();
        waterPanel.add(quantityWater);
        waterPanel.add(quantityInputWater);
        waterPanel.add(commentsWater);
        waterPanel.add(commentsInputWater);

        supplyInputs.add(waterPanel, types[0]);

        JLabel quantityBlanket = new JLabel(validator.translateToLanguage("quantity"));
        JTextField quantityInputBlanket = new JTextField(15);
        JLabel commentsBlanket = new JLabel(validator.translateToLanguage("comments"));
        JTextField commentsInputBlanket = new JTextField(15);

        JPanel blanketPanel = new JPanel();
        blanketPanel.add(quantityBlanket);
        blanketPanel.add(quantityInputBlanket);
        blanketPanel.add(commentsBlanket);
        blanketPanel.add(commentsInputBlanket);

        supplyInputs.add(blanketPanel, types[1]);

        JPanel cotPanel = new JPanel();

        JLabel commentsCot = new JLabel(validator.translateToLanguage("comments"));
        JTextField commentsInputCot = new JTextField(15);
        JLabel roomLabel = new JLabel(validator.translateToLanguage("room"));
        JTextField roomInput = new JTextField(15);
        JLabel gridLabel = new JLabel(validator.translateToLanguage("grid"));
        JTextField gridInput = new JTextField(15);

        cotPanel.add(commentsCot);
        cotPanel.add(commentsInputCot);
        cotPanel.add(roomLabel);
        cotPanel.add(roomInput);
        cotPanel.add(gridLabel);
        cotPanel.add(gridInput);

        supplyInputs.add(cotPanel, types[2]);

        JPanel belongingPanel = new JPanel();
        JLabel descLabel = new JLabel(validator.translateToLanguage("desc"));
        JTextField descInput = new JTextField(15);
        JLabel quantity = new JLabel(validator.translateToLanguage("quantity"));
        JTextField quantityInput = new JTextField(15);
        JLabel comments = new JLabel(validator.translateToLanguage("comments"));
        JTextField commentsInput = new JTextField(15);

        belongingPanel.add(descLabel);
        belongingPanel.add(descInput);
        belongingPanel.add(quantity);
        belongingPanel.add(quantityInput);
        belongingPanel.add(comments);
        belongingPanel.add(commentsInput);

        supplyInputs.add(belongingPanel, types[3]);

        supplyPanel.add(supplyInputs);

        typeInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) supplyInputs.getLayout();
                cl.show(supplyInputs, (String) e.getItem());
            }
        });

        String[] supplyHolders = new String[controller.getPeople().length + controller.getLocations().length];
        ArrayList<String> supplyHolderNames = new ArrayList<String>();
        DisasterVictim[] validPersons = new DisasterVictim[controller.getPeople().length];
        for (int i = 0; i < controller.getPeople().length; i++) {
            if(!(controller.getPeople()[i] instanceof DisasterVictim)){
                continue;
            }
            supplyHolders[i] = controller.getPeople()[i].getFirstName() + " " + controller.getPeople()[i].getLastName();   
            supplyHolderNames.add(controller.getPeople()[i].getFirstName() + " " + controller.getPeople()[i].getLastName());
            validPersons[i] = (DisasterVictim) controller.getPeople()[i];
        }
        for (int i = 0; i < controller.getLocations().length; i++) {
            supplyHolders[i + controller.getPeople().length] = controller.getLocations()[i].getName();
            supplyHolderNames.add(controller.getLocations()[i].getName());
        }
        JComboBox supplyHolderInput = new JComboBox(supplyHolders);
        supplyPanel.add(supplyHolderInput);

        JButton submitButton = new JButton(validator.translateToLanguage("submit"));
        supplyPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = (String) typeInput.getSelectedItem();
                String supplyHolder = (String) supplyHolderInput.getSelectedItem();
                int quantity = 0;
                String comments = "";
                DisasterVictim supplyHolderPerson = null;
                Location supplyHolderLocation = null;

                if(supplyHolderNames.indexOf(supplyHolder) < controller.getPeople().length){
                    supplyHolderPerson = validPersons[supplyHolderNames.indexOf(supplyHolder)];
                }else {
                    supplyHolderLocation = controller.getLocations()[supplyHolderNames.indexOf(supplyHolder) - controller.getPeople().length];
                }
                try {
                    if (type.equals(types[0])) {
                        if(!validator.isValidQuantity(quantityInputWater.getText())) {
                            throw new IllegalArgumentException("inv_quantity");
                        }
                        quantity = Integer.parseInt(quantityInputWater.getText());
                        comments = commentsInputWater.getText();

                        Water water = new Water(comments, quantity);

                        if(supplyHolderPerson != null) {
                            supplyHolderPerson.addSupply(water);
                            supplyHolderPerson.updateEntry();
                        } else {
                            supplyHolderLocation.addSupply(water);
                            supplyHolderLocation.updateEntry();
                        }
                    } else if (type.equals(types[1])) {
                        //quantity = quantityInputBlanket.getText();
                        comments = commentsInputBlanket.getText();
                    } else if (type.equals(types[2])) {
                        comments = commentsInputCot.getText();
                    } else if (type.equals(types[3])) {
                        //quantity = quantityInput.getText();
                        comments = commentsInput.getText();
                    }
                }catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("create_person_err") + e1.getMessage() + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        return supplyPanel;
    }

    public void updatePerson() {
    }

    public void updateInquiry() {
    }

    public void updateLocation() {
    }

    public void exit(String message, int exitCode) {
        if (message != null) {
            JOptionPane.showMessageDialog(null, message);
        }
        System.exit(exitCode);
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
