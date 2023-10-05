package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.User;
import model.Database;


public class LoginView {
    private Label usernameLabel, passwordLabel, loginLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Stage primaryStage;
    private User userSession;
    private Database db;
    private Hyperlink register;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.db = new Database();
    }

    public void show() {
        primaryStage.setTitle("Login Page");

        // Create center content
        GridPane centerGrid = createLoginForm();

        // Create layout
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(centerGrid);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createLoginForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        loginLabel = new Label("LOGIN");
        loginLabel.setFont(Font.font("Arial Black", 32));

        usernameLabel = new Label("Username:");
        passwordLabel = new Label("Password:");

        usernameField = new TextField();
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        
        register = new Hyperlink("Don't have account? Register");
        

        grid.add(loginLabel, 1, 0);
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(register, 1, 3);
        grid.add(loginButton, 1, 4);

        loginButton.setOnAction(event -> handleLoginButtonClick());
        register.setOnAction(event -> goToRegister());

        return grid;
    }

    private void goToRegister() {
    	// ini buat avner 	
    }
    private void handleLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        
        
        if (isValid(username, password) != null) {
        	User userSession = isValid(username, password);
        	HpUserView homePage = new HpUserView(primaryStage,userSession);
            homePage.show();
        } else {
            // Invalid credentials, show an error message
            showError("Failed to Login. Invalid Credential");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public User isValid(String username, String password) {
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
                	userSession = new User(
                			resultSet.getString("UserID"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("role"),
                            resultSet.getString("address"),
                            resultSet.getString("phone_num"),
                            resultSet.getString("gender")
                	);
                        return userSession;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } 
        }
        return null;
    }
}
