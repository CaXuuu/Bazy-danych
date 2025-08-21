package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
    private Connection connection;


    public Connection getConnection() {
        return connection;
    }

    public void connect(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+path);
    }
    public void disconnect() throws SQLException {
        connection.close();
    }
}