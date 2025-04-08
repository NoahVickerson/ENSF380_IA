/**
 * @author Noah Vickerson
 * Supply.java 
 * @version 1.1
 * @date Apr 1 2025
 */

package edu.ucalgary.oop;

import java.sql.*;

public class Supply implements Posession {
    protected final String type;
    protected final int id;
    protected String comments = null;
    protected int quantity;
    protected static int counter = 0;

    /**
     * Constructor
     * @param type must be one of "blanket", "water", "cot", "personal_belonging"
     * @param quantity must be integer >= 0
     * @throws IllegalArgumentException if type or quantity is invalid
     */
    public Supply(String type, int quantity) throws IllegalArgumentException {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("inv_supply_type");
        }
        if(quantity < 0){
            throw new IllegalArgumentException("inv_quantity");
        }
        this.type = type;
        this.quantity = quantity;
        counter++;
        this.id = counter;
    }

    /**
     * Constructor
     * @param id must be unique
     * @param type must be one of "blanket", "water", "cot", "personal_belonging"
     * @param quantity must be integer >= 0
     * @throws IllegalArgumentException if type or quantity or id is invalid
     */
    public Supply(int id, String type, int quantity) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("id may not be unique");
		}
        
        if (!isValidType(type)) {
            throw new IllegalArgumentException("inv_supply_type");
        }
        if(quantity < 0){
            throw new IllegalArgumentException("inv_quantity");
        }
        this.type = type;
        this.quantity = quantity;
        this.id = id;
        if(counter < id){
            counter = id;
        }
    }

    /**
     * Returns the type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the comments
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Returns the quantity
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity
     * @param quantity must be integer >= 0
     * @throws IllegalArgumentException if quantity is invalid
     */
    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    /**
     * Checks if the type is valid
     * @param type must be one of "blanket", "water", "cot", "personal_belonging"
     * @return true if valid, false if not
     */
    private static boolean isValidType(String type) {
        return (type.equalsIgnoreCase("blanket") || type.equalsIgnoreCase("water") || type.equalsIgnoreCase("personal belonging") || type.equalsIgnoreCase("cot"));
    }

    /**
     * Adds the supply to the database
     * @throws SQLException if database query fails
     */
    public void addEntry() throws SQLException {
        String query = "INSERT INTO Supply (supply_id, type, comments) VALUES (?, ?, ?)";
        String[] values = {String.valueOf(id), type, comments};
        String[] types = {"int", "string", "string"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    /**
     * Updates the supply in the database
     * @throws SQLException if database query fails
     */
    public void updateEntry() throws SQLException {
        String query = "UPDATE Supply SET type = ?, comments = ? WHERE supply_id = ?";
        String[] values = {type, comments, String.valueOf(id)};
        String[] types = {"string", "string", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }
}
