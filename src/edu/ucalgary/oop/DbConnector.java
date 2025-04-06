package edu.ucalgary.oop;

import java.sql.*;
import java.time.*;

public class DbConnector implements DatabaseQueryHandler {
    private Connection db;
    private ResultSet rs;

    private static DbConnector instance = null;

    private DbConnector(String url, String user, String pw) throws SQLException {
        db = DriverManager.getConnection(url, user, pw);
    }

    public static DbConnector getInstance(String url, String user, String pw) throws SQLException {
        if(instance == null) {
            instance = new DbConnector(url, user, pw);
        }

        return instance;
    }

    public static DbConnector getInstance() {
        return instance;
    }

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

    public void deadEndQuery(String query, String[] data, String types[]) throws SQLException, IllegalArgumentException {
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
        stmt.executeUpdate();
        stmt.close();
    }

    public void close() throws SQLException {
        db.close();
    }
}
