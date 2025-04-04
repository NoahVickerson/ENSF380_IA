package edu.ucalgary.oop;

import java.sql.*;

public interface DatabaseInterfaceable {
    public void addEntry() throws SQLException;
    public void updateEntry() throws SQLException;
    public int getId();
}