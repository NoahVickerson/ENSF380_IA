package edu.ucalgary.oop;

import java.sql.*;

import org.w3c.dom.Text;

public class Supply implements DatabaseInterfaceable {
    protected final String type;
    protected final int id;
    protected String comments = null;
    protected int quantity;
    protected static int counter = 0;
    protected TextInputValidator validator;

    public Supply(String type, int quantity) throws IllegalArgumentException {
        if (quantity < 0 || !isValidType(type)) {
            throw new IllegalArgumentException("Invalid supply type: " + type + " or quantity: " + quantity);
        }
        this.type = type;
        this.quantity = quantity;
        counter++;
        this.id = counter;
        validator = TextInputValidator.getInstance();
    }

    public Supply(int id, String type, int quantity) throws IllegalArgumentException {
        if(id < counter){
			throw new IllegalArgumentException("id may not be unique");
		}
        
        if (quantity < 0 || !isValidType(type)) {
            throw new IllegalArgumentException("Invalid supply type: " + type + " or quantity: " + quantity);
        }
        this.type = type;
        this.quantity = quantity;
        this.id = id;
        if(counter < id){
            counter = id;
        }
        validator = TextInputValidator.getInstance();
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        this.quantity = quantity;
    }

    private static boolean isValidType(String type) {
        return (type.equalsIgnoreCase("blanket") || type.equalsIgnoreCase("water") || type.equalsIgnoreCase("personal belonging") || type.equalsIgnoreCase("cot"));
    }

    public void addEntry() throws SQLException {
        String query = "INSERT INTO Supply (type, comments) VALUES (?, ?)";
        String[] values = {type, comments};
        String[] types = {"string", "string"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }

    public void updateEntry() throws SQLException {
        String query = "UPDATE Supply SET type = ?, comments = ? WHERE supply_id = ?";
        String[] values = {type, comments, String.valueOf(id)};
        String[] types = {"string", "string", "int"};
        DbConnector db = DbConnector.getInstance();
        db.deadEndQuery(query, values, types);
    }
}
