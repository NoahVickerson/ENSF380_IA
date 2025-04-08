/**
 * @author Noah Vickerson
 * DbConnector.java 
 * @version 1.5
 * @date Apr 7 2025
 */

package edu.ucalgary.oop;

import java.sql.*;
import java.time.*;

public class DbConnector implements DatabaseQueryHandler {
    private Connection db;
    private ResultSet rs;

    private static DbConnector instance = null;

    /**
     * Constructor
     * @param url url of database
     * @param user username
     * @param pw password
     * @throws SQLException
     */
    private DbConnector(String url, String user, String pw) throws SQLException {
        db = DriverManager.getConnection(url, user, pw);
    }

    /**
     * Singleton instance
     * @param url
     * @param user
     * @param pw
     * @return the static object
     * @throws SQLException for connnection failures
     */
    public static DbConnector getInstance(String url, String user, String pw) throws SQLException {
        if(instance == null) {
            instance = new DbConnector(url, user, pw);
        }

        return instance;
    }

    /**
     * Singleton instance
     * @return the static object if instantiated
     */
    public static DbConnector getInstance() throws IllegalStateException {
        if(instance == null) {
            throw new IllegalStateException("db_not_init");
        }
        return instance;
    }

    /**
     * Returns the result of a query with a return (ie select queries)
     * @param query the query string
     * @param data any values to add in with prepared statement
     * @param types the types of the values
     * @return the result of the query
     * @throws SQLException for connection failures
     * @throws IllegalArgumentException if types and data arrays are not the same length
     */
    public String returnQuery(String query, String[] data, String types[]) throws SQLException, IllegalArgumentException {
        String result = "";

        if(types.length != data.length) {
            throw new IllegalArgumentException("Types and data arrays should be the same length");
        }

        PreparedStatement stmt = db.prepareStatement(query);
        for(int i = 0; i < data.length; i++) {
            if(types[i].equalsIgnoreCase("string")) {
                stmt.setString(i + 1, data[i]);
            }
            else if(types[i].equals("int")) {
                stmt.setInt(i + 1, Integer.parseInt(data[i]));
            }
            else if(types[i].equalsIgnoreCase("date")) {
                Date date = Date.valueOf(data[i]);
                stmt.setDate(i + 1, date);
            }
        }

        rs = stmt.executeQuery();

        int colCount = rs.getMetaData().getColumnCount();
        ResultSetMetaData metaData = rs.getMetaData();

        while(rs.next()) {
            for(int i = 1; i <= colCount; i++) {
                result += rs.getString(i) + "<\t>";
            }

            result += "\n";
        }

        stmt.close();

        return result;
    }

    /**
     * Returns the result of a query with no return (update, delete, etc)
     * @param query the query string
     * @param data any values to add in with prepared statement
     * @param types the types of the values
     * @return the result of the query
     * @throws SQLException for connection failures
     * @throws IllegalArgumentException if types and data arrays are not the same length
     */
    public int deadEndQuery(String query, String[] data, String types[]) throws SQLException, IllegalArgumentException {
        if(types.length != data.length) {
            throw new IllegalArgumentException("Types and data arrays should be the same length");
        }

        PreparedStatement stmt = db.prepareStatement(query);
        for(int i = 0; i < data.length; i++) {
            if(data[i] == null || data[i].equalsIgnoreCase("null")) {
                if(types[i].equalsIgnoreCase("String")) {
                    stmt.setNull(i + 1, Types.VARCHAR);
                }
                else if(types[i].equals("int")) {
                    stmt.setNull(i + 1, Types.INTEGER);
                }
                else if(types[i].equalsIgnoreCase("date")) {
                    stmt.setNull(i + 1, Types.DATE);
                }
            }else{
                if(types[i].equalsIgnoreCase("String")) {
                    stmt.setString(i + 1, data[i]);
                }
                else if(types[i].equals("int")) {
                    stmt.setInt(i + 1, Integer.parseInt(data[i]));
                }
                else if(types[i].equalsIgnoreCase("date")) {
                    Date date = Date.valueOf(data[i]);
                    stmt.setDate(i + 1, date);
                }
            }
        }
        int result = stmt.executeUpdate();
        stmt.close();

        return result;
    }

    /**
     * Closes the connection
     * @throws SQLException for close failures
     */
    public void close() throws SQLException {
        db.close();
    }
}
