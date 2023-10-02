package view;

import controller.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView {
    private Label usernameLabel, passwordLabel, loginLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Hyperlink registerLink;
    private Stage primaryStage;
    private LoginController loginController;

    public LoginView(Stage primaryStage, LoginController loginController) {
        this.primaryStage = primaryStage;
        this.loginController = loginController; // Initialize the LoginController
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

        grid.add(loginLabel, 1, 0);
        grid.add(usernameLabel, 0, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(usernameField, 1, 1);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(event -> handleLoginButtonClick());

        return grid;
    }

    private void handleLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        

        if (loginController.isValid(username, password)) {
            // Valid credentials
            System.out.println("Login Berhasil");
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
}
