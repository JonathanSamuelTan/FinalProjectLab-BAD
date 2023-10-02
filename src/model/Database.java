package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Java";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void connect() {
        try {
            // Load the MySQL JDBC driver (if not already loaded)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Initialize the database connection
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

    // Method to insert or update data in the database
    public boolean insertOrUpdateData(String query, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set parameters (if any)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            // Execute the query
            int rowCount = preparedStatement.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            System.err.println("Error: Failed to insert/update data.");
            e.printStackTrace();
            return false;
        }
    }

    // Method to fetch data from the database
    public ResultSet fetchData(String query, Object... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Set parameters (if any)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            // Execute the query and return the result set
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error: Failed to fetch data.");
            e.printStackTrace();
            return null;
        }
    }
}

