package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/seruput_teh";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public Database() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error: Failed to close the database connection.");
                e.printStackTrace();
            }
        }
    }
}
