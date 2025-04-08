package edu.ucalgary.oop;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.ArrayList;

public class UserInterface extends JFrame {
    private ReliefController controller;
    private ErrorLogger logger;
    private TextInputValidator validator;
    private String uname;
    private String pword;
    private boolean loggedIn = false; // dont try and run program until logged in

    public UserInterface(ErrorLogger logger, TextInputValidator validator) {
        super("Relief Services System");

        setSize(850,800);
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
                controller = ReliefController.getInstance(uname, pword, validator);
            }catch (IllegalArgumentException e) {
                displayError(validator.translateToLanguage("upass_incorrect") + "\n" + e.getMessage());
                loggedIn = false;
                continue;
            }catch (SQLException e) {
                logger.logError(e);
                exit(validator.translateToLanguage("db_err") + "\n" + e.getMessage(), 1);
            }catch (Exception e) {
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
        this.setLayout(new FlowLayout());

        JScrollPane scrollPane = new JScrollPane();

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

        JButton saveButton = new JButton(validator.translateToLanguage("refresh"));

        exitPanel.add(saveButton);
        exitPanel.add(exitButton);

        scrollPane.setViewportView(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(850, 675));
        this.add(comboBoxPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(exitPanel, BorderLayout.SOUTH);
        this.setVisible(true);

        menuDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, (String) e.getItem());
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //removeAll();
                repaint();
                revalidate();
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
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));

        String[] menuItems = {validator.translateToLanguage("victim"), validator.translateToLanguage("inquiry"), validator.translateToLanguage("location")};

        JLabel menuLabel = new JLabel(validator.translateToLanguage("choose_update"));
        JComboBox menuDropdown = new JComboBox(menuItems);

        JPanel updateContent = new JPanel(new CardLayout());

        updateContent.add(updatePerson(), menuItems[0]);
        updateContent.add(updateInquiry(), menuItems[1]);
        updateContent.add(updateLocation(), menuItems[2]);

        menuDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) updateContent.getLayout();
                cl.show(updateContent, (String) e.getItem());
            }
        });

        updatePanel.add(menuLabel);
        updatePanel.add(menuDropdown);
        updatePanel.add(updateContent);

        return updatePanel;
    }

    public JPanel dataViewMenu() {
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new FlowLayout());

        viewPanel.add(new JLabel(validator.translateToLanguage("view")));

        String[] menuItems = {validator.translateToLanguage("victim"), validator.translateToLanguage("inquiry"), validator.translateToLanguage("location")};

        JComboBox menuDropdown = new JComboBox(menuItems);
        menuDropdown.setEditable(false);
        
        viewPanel.add(menuDropdown);

        JPanel form = new JPanel(new CardLayout());

        form.add(viewPerson(), menuItems[0]);
        form.add(viewInquiry(), menuItems[1]);
        form.add(viewLocation(), menuItems[2]);

        menuDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) form.getLayout();
                cl.show(form, (String) e.getItem());
            }
        });

        viewPanel.add(form);

        return viewPanel;
    }

    public JPanel createInquiry() {
        JPanel inquiryPanel = new JPanel();
        inquiryPanel.setLayout(new BoxLayout(inquiryPanel, BoxLayout.Y_AXIS));

        JLabel createInquiry = new JLabel("Create Inquiry");
        inquiryPanel.add(createInquiry);

        JLabel inq_prompt = new JLabel(validator.translateToLanguage("inquirer"));
        Person[] inquirerPeople = controller.getPeople();
        String[] inquirers = new String[inquirerPeople.length];
        ArrayList<String> inquirersList = new ArrayList<String>();

        int num_victims = 0;

        for (int i = 0; i < inquirerPeople.length; i++) {
            inquirers[i] = (inquirerPeople[i].getFirstName() + " " + inquirerPeople[i].getLastName());
            inquirersList.add(inquirerPeople[i].getFirstName() + " " + inquirerPeople[i].getLastName());
        }
        JComboBox inquirerInput = new JComboBox(inquirers);

        JLabel victim_prompt = new JLabel(validator.translateToLanguage("victim") + ": ");
        DisasterVictim[] victims = controller.getVictims();
        String[] victimsNames = new String[victims.length];
        ArrayList<String> victimsNamesList = new ArrayList<String>();

        for (int i = 0; i < victims.length; i++) {
            victimsNames[i] = (victims[i].getFirstName() + " " + victims[i].getLastName());
            victimsNamesList.add(victims[i].getFirstName() + " " + victims[i].getLastName());
        }
        JComboBox victimInput = new JComboBox(victimsNames);

        inquiryPanel.add(inq_prompt);
        inquiryPanel.add(inquirerInput);
        inquiryPanel.add(victim_prompt);
        inquiryPanel.add(victimInput);
        
        JLabel doi = new JLabel(validator.translateToLanguage("doi"));
        JTextField doiInput = new JTextField(validator.translateToLanguage("ex_date"), 15);

        inquiryPanel.add(doi);
        inquiryPanel.add(doiInput);

        JLabel infoProvided = new JLabel(validator.translateToLanguage("comments"));
        JTextField infoProvidedInput = new JTextField(15);

        inquiryPanel.add(infoProvided);
        inquiryPanel.add(infoProvidedInput);

        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];
        ArrayList<String> locationNamesList = new ArrayList<String>();

        for (int i = 0; i < locations.length; i++) {
            locationNames[i] = locations[i].getName();
            locationNamesList.add(locations[i].getName());
        }

        JLabel location_prompt = new JLabel(validator.translateToLanguage("location"));
        JComboBox locationInput = new JComboBox(locationNames);

        inquiryPanel.add(location_prompt);
        inquiryPanel.add(locationInput);

        JButton submit = new JButton(validator.translateToLanguage("submit"));
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person inquirer = controller.fetchPerson(inquirerInput.getSelectedItem().toString().split(" ")[0], inquirerInput.getSelectedItem().toString().split(" ")[1]);
                DisasterVictim victim = controller.fetchVictim(victimInput.getSelectedItem().toString().split(" ")[0], victimInput.getSelectedItem().toString().split(" ")[1]);
                String dateOfInquiry = doiInput.getText();
                String comments = infoProvidedInput.getText();
                Location location = controller.fetchLocation(locationInput.getSelectedItem().toString());

                try{
                    controller.addInquiry(inquirer, victim, dateOfInquiry, comments, location);
                    controller.fetchInquiry(inquirer, victim, dateOfInquiry).addEntry();
                    displayError(validator.translateToLanguage("create_inquiry_success"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("create_inquiry_err") +validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        inquiryPanel.add(submit);

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

        JLabel currentLocation = new JLabel(validator.translateToLanguage("location") + ": ");
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
                                displayError(validator.translateToLanguage("create_location_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                                return;
                            }
                            controller.addLocation(location);
                            location.addEntry();
                            displayError(validator.translateToLanguage("create_location_success"));
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
                    displayError(validator.translateToLanguage("create_person_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
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

        JButton submitButton = new JButton(validator.translateToLanguage("validate"));
        locationPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setLength(0);
                address.setLength(0);
                name.append(locationNameInput.getText());
                address.append(locationAddressInput.getText());
                displayError(validator.translateToLanguage("loc_val"));
            }
        });

        return locationPanel;
    }

    public JPanel createMedicalRecord(DisasterVictim person) {
        JPanel medicalRecordPanel = new JPanel();
        medicalRecordPanel.setLayout(new BoxLayout(medicalRecordPanel, BoxLayout.Y_AXIS));

        JLabel createMedicalRecord = new JLabel(validator.translateToLanguage("add_record"));
        medicalRecordPanel.add(createMedicalRecord);

        medicalRecordPanel.add(new JLabel(validator.translateToLanguage("location_prompt")));

        if(controller.getLocations() == null){
            throw new IllegalArgumentException(validator.translateToLanguage("no_locations"));
        }
        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];

        for(int i = 0; i < locations.length; i++){
            locationNames[i] = locations[i].getName();
        }

        JComboBox locationInput = new JComboBox(locationNames);
        medicalRecordPanel.add(locationInput);

        medicalRecordPanel.add(new JLabel(validator.translateToLanguage("details_prompt")));
        JTextField detailsInput = new JTextField(15);
        medicalRecordPanel.add(detailsInput);

        medicalRecordPanel.add(new JLabel(validator.translateToLanguage("date_prompt")));
        JTextField dateInput = new JTextField(validator.translateToLanguage("ex_date"), 15);
        medicalRecordPanel.add(dateInput);

        JButton submitButton = new JButton(validator.translateToLanguage("add_record"));
        medicalRecordPanel.add(submitButton);
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Location location = controller.fetchLocation(locationInput.getSelectedItem().toString());
                String details = detailsInput.getText();
                String date = dateInput.getText();

                try {
                    MedicalRecord medicalRecord = new MedicalRecord(location, person, details, date);
                    person.addMedicalRecord(medicalRecord);
                    displayError(validator.translateToLanguage("update_medical_record_success"));
                    medicalRecord.addEntry();
                } catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                    displayError(validator.translateToLanguage("update_medical_record_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

                

        return medicalRecordPanel;
    }

    public JPanel createSupply() {
        JPanel supplyPanel = new JPanel();
        supplyPanel.setLayout(new BoxLayout(supplyPanel, BoxLayout.Y_AXIS));

        JLabel createSupply = new JLabel(validator.translateToLanguage("create_supply"));
        supplyPanel.add(createSupply);

        supplyPanel.setLayout(new BoxLayout(supplyPanel, BoxLayout.Y_AXIS));

        JLabel type = new JLabel(validator.translateToLanguage("type_prompt"));
        String[] types = {validator.translateToLanguage("water"), validator.translateToLanguage("blanket"), validator.translateToLanguage("cot"), validator.translateToLanguage("personal_belonging")};

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

        JLabel roomLabel = new JLabel(validator.translateToLanguage("room"));
        JTextField roomInput = new JTextField(15);
        JLabel gridLabel = new JLabel(validator.translateToLanguage("grid"));
        JTextField gridInput = new JTextField(15);

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
        DisasterVictim[] validPersons = controller.getVictims();
        for (int i = 0; i < validPersons.length; i++) {
            supplyHolders[i] = controller.getPeople()[i].getFirstName() + " " + controller.getPeople()[i].getLastName();   
            supplyHolderNames.add(controller.getPeople()[i].getFirstName() + " " + controller.getPeople()[i].getLastName());
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
                String supplyHolderString = (String) supplyHolderInput.getSelectedItem();
                int quantity = 0;
                String comments = "";
                SupplyHolder supplyHolder = null;

                if(supplyHolderNames.indexOf(supplyHolderString) < validPersons.length){
                    supplyHolder = validPersons[supplyHolderNames.indexOf(supplyHolderString)];
                }else {
                    supplyHolder = controller.getLocations()[supplyHolderNames.indexOf(supplyHolderString) - controller.getPeople().length];
                }
                try {
                    if (type.equals(types[0])) {
                        if(!validator.isValidQuantity(quantityInputWater.getText())) {
                            throw new IllegalArgumentException("inv_quantity");
                        }
                        quantity = Integer.parseInt(quantityInputWater.getText());
                        comments = commentsInputWater.getText();

                        Water water = new Water(comments, quantity);

                        supplyHolder.addSupply(water);
                        water.addEntry();
                        supplyHolder.updateEntry();
                    } else if (type.equals(types[1])) {
                        if(!validator.isValidQuantity(quantityInputWater.getText())) {
                            throw new IllegalArgumentException("inv_quantity");
                        }
                        quantity = Integer.parseInt(quantityInputBlanket.getText());
                        comments = commentsInputBlanket.getText();

                        Supply blanket = new Supply("blanket", quantity);

                        blanket.setComments(comments);

                        supplyHolder.addSupply(blanket);
                        blanket.addEntry();
                        supplyHolder.updateEntry();
                    } else if (type.equals(types[2])) {
                        String room = roomInput.getText();
                        String grid = gridInput.getText();

                        if(!validator.isValidRoom(room) || !validator.isValidGrid(grid)) {
                            throw new IllegalArgumentException("inv_room_grid");
                        }

                        Cot cot = new Cot(room, grid, room + grid);

                        supplyHolder.addSupply(cot);
                        cot.addEntry();
                        supplyHolder.updateEntry();
                    } else if (type.equals(types[3])) {
                        if(!validator.isValidQuantity(quantityInput.getText())) {
                            throw new IllegalArgumentException("inv_quantity");
                        }
                        quantity = Integer.parseInt(quantityInput.getText());
                        comments = commentsInput.getText();
                        String desc = descInput.getText();

                        PersonalBelonging belonging = new PersonalBelonging(desc, comments, quantity);

                        if(!(supplyHolder instanceof DisasterVictim)) {
                            throw new IllegalArgumentException("pb_only_person");
                        }else{
                            supplyHolder.addSupply(belonging);
                            belonging.addEntry();
                            supplyHolder.updateEntry();
                        }
                    }
                    displayError(validator.translateToLanguage("create_supply_success"));
                }catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("create_supply_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        return supplyPanel;
    }

    public JPanel updatePerson() {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));

        DisasterVictim[] people = controller.getVictims();
        String[] peopleNames = new String[people.length];
        ArrayList<String> peopleList = new ArrayList<String>();
        
        for (int i = 0; i < people.length; i++) {
            peopleNames[i] = people[i].getFirstName() + " " + people[i].getLastName();
            peopleList.add(peopleNames[i]);
        }

        JComboBox personInput = new JComboBox(peopleNames);
        JPanel personContent = new JPanel(new CardLayout());

        personPanel.add(personInput);
        personPanel.add(personContent);

        for (int i = 0; i < people.length; i++) {
            personContent.add(people[i].getFirstName() + " " + people[i].getLastName(), updateIndividualPerson(people[i]));
        }

        personInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) personContent.getLayout();
                cl.show(personContent, (String) personInput.getSelectedItem());
            }   
        });

        return personPanel;
    }

    public JPanel updateIndividualPerson(DisasterVictim person) {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));

        JLabel firstName = new JLabel(validator.translateToLanguage("fname"));
        JTextField firstNameInput = new JTextField(person.getFirstName(), 15);
        JLabel lastName = new JLabel(validator.translateToLanguage("lname"));
        JTextField lastNameInput = new JTextField(person.getLastName(), 15);
        JLabel dateOfBirth = new JLabel(validator.translateToLanguage("dob"));
        JTextField dateOfBirthInput = new JTextField(person.getDateOfBirth(), 15);
        JLabel gender = new JLabel(validator.translateToLanguage("gender"));

        String[] genderOptions = {validator.translateToLanguage("male"), validator.translateToLanguage("female"), validator.translateToLanguage("non_binary")};
        JComboBox genderInput = new JComboBox(genderOptions);
        if(person.getGender() != null) {
            genderInput.setSelectedItem(validator.translateToLanguage(person.getGender()));
        }
        JLabel phoneNum = new JLabel(validator.translateToLanguage("phone"));
        JTextField phoneNumInput = new JTextField(person.getPhoneNumber(), 15);

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

        // allow adding family members
        personPanel.add(new JLabel(validator.translateToLanguage("add_family")));

        Person[] possibleFamily = controller.getPeople();
        String[] familyNames = new String[possibleFamily.length];

        for (int i = 0; i < possibleFamily.length; i++) {
            familyNames[i] = possibleFamily[i].getFirstName() + " " + possibleFamily[i].getLastName();
        }

        JComboBox familyInput = new JComboBox(familyNames);
        personPanel.add(familyInput);

        JButton addFamily = new JButton(validator.translateToLanguage("add_family"));
        personPanel.add(addFamily);

        addFamily.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    person.addFamilyMember(possibleFamily[familyInput.getSelectedIndex()]);
                    possibleFamily[familyInput.getSelectedIndex()].updateEntry();
                    person.updateEntry();
                    displayError(validator.translateToLanguage("add_family_success"));
                } catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                    displayError(validator.translateToLanguage("add_family_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        // allow moving locations
        personPanel.add(new JLabel(validator.translateToLanguage("move_location")));
        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];

        for (int i = 0; i < locations.length; i++) {
            locationNames[i] = locations[i].getName();
        }

        JComboBox locationInput = new JComboBox(locationNames);
        if(person.getCurrentLocation() != null) {
            locationInput.setSelectedItem(person.getCurrentLocation().getName());
        }
        personPanel.add(locationInput);

        // allow adding medical records
        personPanel.add(new JLabel(validator.translateToLanguage("hr")));
        personPanel.add(new JLabel(validator.translateToLanguage("medical")));

        if(person.getMedicalRecords() != null) {
            JPanel medicalPanel = new JPanel(new CardLayout());

            MedicalRecord[] medicalRecords = person.getMedicalRecords();
            String[] medicalRecordNames = new String[medicalRecords.length + 1];

            for (int i = 0; i < medicalRecords.length; i++) {
                medicalRecordNames[i] = medicalRecords[i].getDateOfTreatment();
            }

            medicalRecordNames[medicalRecords.length] = validator.translateToLanguage("add_record");

            JComboBox medicalInput = new JComboBox(medicalRecordNames);

            JPanel[] medicalRecordPanels = new JPanel[medicalRecords.length + 1];

            for (int i = 0; i < medicalRecords.length; i++) {
                medicalRecordPanels[i] = updateMedicalRecord(medicalRecords[i]);
            }

            medicalRecordPanels[medicalRecords.length] = createMedicalRecord(person);

            for (int i = 0; i < medicalRecordPanels.length; i++) {
                medicalPanel.add(medicalRecordPanels[i], medicalRecordNames[i]);
            }

            medicalInput.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    CardLayout cl = (CardLayout) medicalPanel.getLayout();
                    cl.show(medicalPanel, (String) medicalInput.getSelectedItem());
                }
            });
            

            personPanel.add(medicalInput);
            personPanel.add(medicalPanel);
        }else{
            personPanel.add(createMedicalRecord(person));
        }


        // allow adding supplies
        if(person.getCurrentLocation() != null) {
            Posession[] locationSupplies = person.getCurrentLocation().getSupplies();
            personPanel.add(new JLabel(validator.translateToLanguage("hr")));
            personPanel.add(new JLabel(validator.translateToLanguage("supplies")));

            if(locationSupplies != null) {

                ArrayList<String> supplyNames = new ArrayList<>();

                for (int i = 0; i < locationSupplies.length; i++) {
                    supplyNames.add(locationSupplies[i].getType() + " (" + locationSupplies[i].getId() + ")");
                }

                JComboBox supplyInput = new JComboBox(supplyNames.toArray());

                JButton addSupply = new JButton(validator.translateToLanguage("add_supply"));

                personPanel.add(supplyInput);
                personPanel.add(addSupply);
                addSupply.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            controller.reflectSupplyTransfer(person, locationSupplies[supplyInput.getSelectedIndex()]);
                            person.updateEntry();
                            displayError(validator.translateToLanguage("supply_transfer_success"));
                        } catch (IllegalArgumentException e1) {
                            displayError(validator.translateToLanguage("supply_transfer_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                        } catch (SQLException e1) {
                            logger.logError(e1);
                            exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                        }
                    }
                });
            }   
        }
        

        JButton updatePerson = new JButton(validator.translateToLanguage("update") + " " + validator.translateToLanguage("person"));

        updatePerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    person.setFirstName(firstNameInput.getText());
                    person.setLastName(lastNameInput.getText());
                    person.setDateOfBirth(dateOfBirthInput.getText());
                    person.setGender(validator.translateToKey((String) genderInput.getSelectedItem()));
                    person.setPhoneNumber(phoneNumInput.getText());
                    person.setCurrentLocation(controller.fetchLocation(locationInput.getSelectedItem().toString()));

                    person.updateEntry();
                    displayError(validator.translateToLanguage("update_person_success"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("update_person_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });
        personPanel.add(updatePerson);

        return personPanel;
    }

    public JPanel updateSingleLocation(Location location) {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));

        JLabel location_prompt = new JLabel(validator.translateToLanguage("name"));
        JTextField nameInput = new JTextField(location.getName(), 15);
        JLabel address_prompt = new JLabel(validator.translateToLanguage("address"));
        JTextField addressInput = new JTextField(location.getAddress(), 15);

        locationPanel.add(location_prompt);
        locationPanel.add(nameInput);
        locationPanel.add(address_prompt);
        locationPanel.add(addressInput);

        JButton updateLocation = new JButton(validator.translateToLanguage("update"));

        updateLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    location.setName(nameInput.getText());
                    location.setAddress(addressInput.getText());
                    location.updateEntry();
                    displayError(validator.translateToLanguage("update_location_success"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("update_location_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });
        locationPanel.add(updateLocation);

        return locationPanel;
    }

    public JPanel updateInquiry() {
        JPanel inquiryPanel = new JPanel();
        inquiryPanel.setLayout(new BoxLayout(inquiryPanel, BoxLayout.Y_AXIS));

        inquiryPanel.add(new JLabel(validator.translateToLanguage("update_inquiry")));

        Inquiry[] inquiries = controller.getInquiries();

        if(inquiries == null) {
            inquiryPanel.add(new JLabel(validator.translateToLanguage("no_inquiries")));
            return inquiryPanel;
        }

        String[] inquiryNames = new String[inquiries.length];

        for (int i = 0; i < inquiries.length; i++) {
            inquiryNames[i] = inquiries[i].getInquirer().getFirstName() + " " + inquiries[i].getInquirer().getLastName() + " - " + inquiries[i].getMissingPerson().getFirstName() + " " + inquiries[i].getMissingPerson().getLastName() + " - " + inquiries[i].getDateOfInquiry();
        }

        JComboBox inquiryInput = new JComboBox(inquiryNames);

        inquiryPanel.add(inquiryInput);

        JPanel inquiryContent = new JPanel(new CardLayout());

        JPanel[] inquiryPanels = new JPanel[inquiries.length];

        for (int i = 0; i < inquiries.length; i++) {
            inquiryPanels[i] = updateSingleInquiry(inquiries[i]);
            inquiryContent.add(inquiryPanels[i], inquiryNames[i]);
        }

        inquiryInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) inquiryContent.getLayout();
                cl.show(inquiryContent, (String) inquiryInput.getSelectedItem());
            }
        });
        inquiryPanel.add(inquiryContent);

        return inquiryPanel;
    }

    public JPanel updateSingleInquiry(Inquiry inquiry) {
        JPanel inquiryPanel = new JPanel();
        inquiryPanel.setLayout(new BoxLayout(inquiryPanel, BoxLayout.Y_AXIS));

        Person[] persons = controller.getPeople();
        DisasterVictim[] victims = controller.getVictims();
        String[] personNames = new String[persons.length];

        for (int i = 0; i < persons.length; i++) {
            personNames[i] = persons[i].getFirstName() + " " + persons[i].getLastName();
        }

        JComboBox inquirerInput = new JComboBox(personNames);

        if(inquiry.getInquirer() != null) {
            inquirerInput.setSelectedItem(inquiry.getInquirer().getFirstName() + " " + inquiry.getInquirer().getLastName());
        }

        JLabel treatmentDetails = new JLabel(validator.translateToLanguage("treatment_details"));
        JTextField treatmentDetailsInput = new JTextField(inquiry.getInfoProvided(), 15);


        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];

        for (int i = 0; i < locations.length; i++) {
            locationNames[i] = locations[i].getName();
        }

        JComboBox locationInput = new JComboBox(locationNames);

        if(inquiry.getLastKnownLocation() != null) {
            locationInput.setSelectedItem(inquiry.getLastKnownLocation().getName());
        }

        inquiryPanel.add(new JLabel(validator.translateToLanguage("inquiry_immutable")));

        JButton updateInquiry = new JButton(validator.translateToLanguage("update"));

        inquiryPanel.add(new JLabel(validator.translateToLanguage("inquirer")));
        inquiryPanel.add(inquirerInput);
        inquiryPanel.add(treatmentDetails);
        inquiryPanel.add(treatmentDetailsInput);
        inquiryPanel.add(new JLabel(validator.translateToLanguage("location")));
        inquiryPanel.add(locationInput);
        inquiryPanel.add(updateInquiry);

        updateInquiry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Person inquirer = controller.fetchPerson(inquirerInput.getSelectedItem().toString().split(" ")[0], inquirerInput.getSelectedItem().toString().split(" ")[1]);
                    Location location = controller.fetchLocation(locationInput.getSelectedItem().toString());

                    inquiry.setInquirer(inquirer);
                    inquiry.setInfoProvided(treatmentDetailsInput.getText());
                    inquiry.setLastKnownLocation(location);
                    inquiry.updateEntry();
                    displayError(validator.translateToLanguage("update_inquiry_success"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("update_inquiry_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });

        return inquiryPanel;
    }
        
    public JPanel updateMedicalRecord(MedicalRecord medicalRecord) {
        JPanel medicalPanel = new JPanel();
        medicalPanel.setLayout(new BoxLayout(medicalPanel, BoxLayout.Y_AXIS));

        JLabel location_prompt = new JLabel(validator.translateToLanguage("location"));
        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];
        ArrayList<String> locationList = new ArrayList<String>();

        for (int i = 0; i < locations.length; i++) {
            locationNames[i] = locations[i].getName();
            locationList.add(locationNames[i]);
        }

        JComboBox locationInput = new JComboBox(locationNames);

        if(medicalRecord.getLocation() != null) {
            locationInput.setSelectedItem(medicalRecord.getLocation().getName());
        }

        medicalPanel.add(locationInput);
        JLabel dateOfTreatment = new JLabel(validator.translateToLanguage("date_of_treatment"));
        JTextField dateOfTreatmentInput = new JTextField(medicalRecord.getDateOfTreatment(), 15);

        JLabel treatmentDetails = new JLabel(validator.translateToLanguage("treatment_details"));
        JTextArea treatmentDetailsInput = new JTextArea(medicalRecord.getTreatmentDetails(), 3, 20);


        medicalPanel.add(location_prompt);
        medicalPanel.add(locationInput);
        medicalPanel.add(dateOfTreatment);
        medicalPanel.add(dateOfTreatmentInput);
        medicalPanel.add(treatmentDetails);
        medicalPanel.add(treatmentDetailsInput);

        JButton updateMedicalRecord = new JButton(validator.translateToLanguage("data_update"));
        updateMedicalRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    medicalRecord.setLocation(controller.fetchLocation(locationInput.getSelectedItem().toString()));
                    medicalRecord.setDateOfTreatment(dateOfTreatmentInput.getText());
                    medicalRecord.setTreatmentDetails(treatmentDetailsInput.getText());
                    medicalRecord.updateEntry();
                    displayError(validator.translateToLanguage("update_medical_record_success"));
                } catch (IllegalArgumentException e1) {
                    displayError(validator.translateToLanguage("update_medical_record_err") + validator.translateToLanguage(e1.getMessage()) + "\n" + validator.translateToLanguage("try_again"));
                } catch (SQLException e1) {
                    logger.logError(e1);
                    exit(validator.translateToLanguage("db_err") + "\n" + e1.getMessage(), 1);
                }
            }
        });
        medicalPanel.add(updateMedicalRecord);
        return medicalPanel;
    }

    public JPanel updateLocation() {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));

        Location[] locations = controller.getLocations();
        String[] locationNames = new String[locations.length];
        JPanel[] locationPanels = new JPanel[locations.length];
        JPanel locationContent = new JPanel(new CardLayout());

        for (int i = 0; i < locations.length; i++) {
            locationNames[i] = locations[i].getName();
            locationPanels[i] = updateSingleLocation(locations[i]);
        }

        JComboBox locationInput = new JComboBox(locationNames);

        for(int i = 0; i < locations.length; i++) {
            locationContent.add(locationPanels[i], locationNames[i]);
        }

        locationInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) locationContent.getLayout();
                cl.show(locationContent, locationInput.getSelectedItem().toString());
            }
        });

        locationPanel.add(locationInput);
        locationPanel.add(locationContent);

        return locationPanel;
    }

    public JPanel viewPerson() {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BoxLayout(personPanel, BoxLayout.Y_AXIS));

        Person[] people = controller.getPeople();
        String[] peopleNames = new String[people.length];
        int numVictims = 0;

        for (int i = 0; i < people.length; i++) {
            if(people[i] instanceof DisasterVictim) {
                peopleNames[i] = people[i].getFirstName() + " " + people[i].getLastName();
                numVictims++;
            }
        }

        String[] namesTruncated = new String[numVictims];

        for(int i = 0; i < numVictims; i++) {
            if(peopleNames[i] != null) {
                namesTruncated[i] = peopleNames[i];
            }
        }

        JComboBox personInput = new JComboBox(namesTruncated);
        JPanel personContent = new JPanel(new CardLayout());

        JPanel[] personPanels = new JPanel[people.length];

        for(int i = 0; i < people.length; i++) {
            if(people[i] instanceof DisasterVictim) {
                personPanels[i] = new JPanel();
                personPanels[i].setLayout(new BoxLayout(personPanels[i], BoxLayout.Y_AXIS));
                DisasterVictim person = (DisasterVictim) people[i];
                peopleNames[i] = person.getFirstName() + " " + person.getLastName();
                personPanels[i].add(new JLabel(validator.translateToLanguage("id") + person.getId()));
                personPanels[i].add(new JLabel(validator.translateToLanguage("name") + person.getFirstName() + " " + person.getLastName()));

                if(person.getGender() != null){
                    personPanels[i].add(new JLabel(validator.translateToLanguage("gender") + validator.translateToLanguage(person.getGender())));
                }
                if(person.getDateOfBirth() != null){
                    personPanels[i].add(new JLabel(validator.translateToLanguage("dob") + person.getDateOfBirth()));
                }
                if(person.getPhoneNumber() != null){
                    personPanels[i].add(new JLabel(validator.translateToLanguage("phone") + person.getPhoneNumber()));
                }
                if(person.getEntryDate() != null){
                    personPanels[i].add(new JLabel(validator.translateToLanguage("entry") + person.getEntryDate()));
                }
                if(person.getFamilyGroup() != null){
                    FamilyGroup familyGroup = person.getFamilyGroup();
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("family_group")));
                    for(Person familyMember : familyGroup.getFamilyMembers()){
                        personPanels[i].add(new JLabel("- " + familyMember.getFirstName() + " " + familyMember.getLastName()));
                    }
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                }

                personPanels[i].add(new JLabel("\n\n" + validator.translateToLanguage("pb")));

                if(person.getPersonalBelongings() != null){
                    Posession[] supplies = person.getPersonalBelongings();
                for(int j = 0; j < supplies.length; j++){
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    if(supplies[j] instanceof Cot){
                        Cot cot = (Cot) supplies[j];
                        personPanels[i].add(new JLabel(validator.translateToLanguage("cot")));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("room") + cot.getRoom()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("grid") + cot.getGrid()));
                    }else if(supplies[j] instanceof Water){
                        Water water = (Water) supplies[j];
                        personPanels[i].add(new JLabel(validator.translateToLanguage("water")));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("quantity") + water.getQuantity()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("comments") + water.getComments()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("alloc_date") + water.getAllocationDate()));
                    }else if(supplies[j] instanceof PersonalBelonging){
                        PersonalBelonging pb = (PersonalBelonging) supplies[j];
                        personPanels[i].add(new JLabel(validator.translateToLanguage("quantity") + pb.getQuantity()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("comments") + pb.getComments()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("desc") + pb.getDescription()));
                    }else if (supplies[j] instanceof Supply){
                        personPanels[i].add(new JLabel(validator.translateToLanguage(supplies[j].getType())));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("comments") + supplies[j].getComments()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("quantity") + supplies[j].getQuantity()));
                    }else{
                        continue;
                    }
                    personPanels[i].add(new JLabel(validator.translateToLanguage("id") + supplies[j].getId()));
                }
                }
                

                if(person.getCurrentLocation() != null){
                    Location location = person.getCurrentLocation();
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("location")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("name") + location.getName()));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("address") + location.getAddress()));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("id") + location.getId()));
                }
                personContent.add(peopleNames[i], personPanels[i]);

                if(person.getMedicalRecords() != null){
                    MedicalRecord[] medicalRecords = person.getMedicalRecords();
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("medical")));
                    personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    for(MedicalRecord medicalRecord : medicalRecords){
                        personPanels[i].add(new JLabel(validator.translateToLanguage("date_of_treatment") + medicalRecord.getDateOfTreatment()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("location") + ": " + medicalRecord.getLocation().getName()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("comments") + medicalRecord.getTreatmentDetails()));
                        personPanels[i].add(new JLabel(validator.translateToLanguage("hr")));
                    }
                }
            }
        }

        personPanel.add(personInput);
        personPanel.add(personContent);

        personInput.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) personContent.getLayout();
                cl.show(personContent, (String) e.getItem());
            }
        });

        return personPanel;
    }

    public JPanel viewInquiry() {
        JPanel inquiryPanel = new JPanel();
        inquiryPanel.setLayout(new BoxLayout(inquiryPanel, BoxLayout.Y_AXIS));

        Inquiry[] inquiries = controller.getInquiries();

        for(int i = 0; i < inquiries.length; i++) {

            inquiryPanel.add(new JLabel(validator.translateToLanguage("id") + inquiries[i].getId()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("inquirer") + inquiries[i].getInquirer().getFirstName() + " " + inquiries[i].getInquirer().getLastName()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("victim") + ": " + inquiries[i].getMissingPerson().getFirstName() + " " + inquiries[i].getMissingPerson().getLastName()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("doi") + inquiries[i].getDateOfInquiry()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("comments") + inquiries[i].getInfoProvided()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("last_loc") + inquiries[i].getLastKnownLocation().getName()));
            inquiryPanel.add(new JLabel(validator.translateToLanguage("hr") + "\n\n"));
        }



        return inquiryPanel;
    }

    public JPanel viewLocation() {
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS));

        Location[] locations = controller.getLocations();

        for(int i = 0; i < locations.length; i++) {

            locationPanel.add(new JLabel(validator.translateToLanguage("id") + locations[i].getId()));
            locationPanel.add(new JLabel(validator.translateToLanguage("name") + locations[i].getName()));
            locationPanel.add(new JLabel(validator.translateToLanguage("address") + locations[i].getAddress()));

            locationPanel.add(new JLabel(validator.translateToLanguage("supplies")));
            if(locations[i].getSupplies() != null){
                Posession[] supplies = locations[i].getSupplies();
                for(int j = 0; j < supplies.length; j++){
                    locationPanel.add(new JLabel(validator.translateToLanguage("hr")));
                    if(supplies[j] instanceof Cot){
                        Cot cot = (Cot) supplies[j];
                        locationPanel.add(new JLabel(validator.translateToLanguage("cot")));
                        locationPanel.add(new JLabel(validator.translateToLanguage("room") + cot.getRoom()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("grid") + cot.getGrid()));
                    }else if(supplies[j] instanceof Water){
                        Water water = (Water) supplies[j];
                        locationPanel.add(new JLabel(validator.translateToLanguage("water")));
                        locationPanel.add(new JLabel(validator.translateToLanguage("quantity") + water.getQuantity()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("comments") + water.getComments()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("alloc_date") + water.getAllocationDate()));
                    }else if(supplies[j] instanceof PersonalBelonging){
                        PersonalBelonging pb = (PersonalBelonging) supplies[j];
                        locationPanel.add(new JLabel(validator.translateToLanguage("quantity") + pb.getQuantity()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("comments") + pb.getComments()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("desc") + pb.getDescription()));
                    }else{
                        locationPanel.add(new JLabel(validator.translateToLanguage(supplies[j].getType())));
                        locationPanel.add(new JLabel(validator.translateToLanguage("comments") + supplies[j].getComments()));
                        locationPanel.add(new JLabel(validator.translateToLanguage("quantity") + supplies[j].getQuantity()));
                    }
                    locationPanel.add(new JLabel(validator.translateToLanguage("id") + supplies[j].getId()));
                }
            }
            locationPanel.add(new JLabel(validator.translateToLanguage("hr") + "\n\n"));
            locationPanel.add(new JLabel(validator.translateToLanguage("hr") + "\n\n"));
        }

        

        return locationPanel;
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
