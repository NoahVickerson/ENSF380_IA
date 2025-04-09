package edu.ucalgary.oop;

import java.sql.*;

public interface DatabaseQueryHandler {
    public String returnQuery(String query, String[] data, String[] types) throws SQLException, IllegalArgumentException;
    public int deadEndQuery(String Query, String[] data, String[] types) throws SQLException, IllegalArgumentException;
    public default String getEntries(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        String results = returnQuery(query, new String[]{}, new String[]{});
        return results;
    }
    public default String getEntries(String tableName, String order) throws SQLException {
        String query = "SELECT * FROM " + tableName + " ORDER BY " + order + " ASC";
        String results = returnQuery(query, new String[]{}, new String[]{});
        return results;
    }
}
