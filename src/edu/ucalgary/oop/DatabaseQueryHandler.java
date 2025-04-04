package edu.ucalgary.oop;

import java.sql.*;

public interface DatabaseQueryHandler {
    public String returnQuery(String query, String[] data, String[] types) throws SQLException, IllegalArgumentException;
    public void deadEndQuery(String Query, String[] data, String[] types) throws SQLException, IllegalArgumentException;
    public default String getEntries(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        DbConnector db = DbConnector.getInstance();
        String results = db.returnQuery(query, new String[]{}, new String[]{});
        return results;
    }
    public default String getEntries(String tableName, String order) throws SQLException {
        String query = "SELECT * FROM " + tableName + " ORDER BY ? ASC";
        DbConnector db = DbConnector.getInstance();
        String results = db.returnQuery(query, new String[]{order}, new String[]{"string"});
        return results;
    }
}
