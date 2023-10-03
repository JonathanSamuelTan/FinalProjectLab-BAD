package controller;

import javafx.stage.Stage;
import model.Database;
import model.User;
import view.RegisterView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterController {
    private RegisterView view;
    private Database db;

    public RegisterController(RegisterView view) {
        this.view = view;
        db = new Database();

        this.view.getRegisterButton().setOnAction(actionEvent -> {
            System.out.println(generateID());
        });
    }

    private ArrayList<User> getUser() {
        db.connect();
        Connection connection = db.getConnection();
        ArrayList<User> users = new ArrayList<>();

        if (connection != null) {
            try {
                // Prepare a SQL query to check if the email and password match
                String query = "SELECT * FROM User";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("userID"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_num"),
                            resultSet.getString("gender")
                    ));
                }
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
        }
        return users;
    }

    private String generateID() {
        ArrayList<User> users = getUser();
        int temp = Integer.MIN_VALUE;
        for (User user:users
             ) {
            int temp2 =Integer.parseInt(user.getUserID().substring(2));
            if (temp < temp2 && user.getUserID().contains("CU")){
                temp = temp2;

            }
        }
        return String.format("CU%03d", temp);
    }







}
