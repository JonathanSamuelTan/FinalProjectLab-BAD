package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Database;

public class LoginController {

    private Database db;

    public LoginController() {
        this.db = new Database();
    }

    public boolean isValid(String username, String password) {
        // Check if the provided email and password are valid
        Connection connection = db.getConnection();

        if (connection != null) {
            try {
                // Prepare a SQL query to check if the email and password match
                String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
        }
        return false;
    }
}
