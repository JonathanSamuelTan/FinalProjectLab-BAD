package controller;

import com.mysql.cj.log.Log;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import model.Database;
import model.User;
import view.LoginView;
import view.RegisterView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterController {
    private RegisterView view;
    private Database db;
    private Stage primaryStage;

    public RegisterController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.view = new RegisterView(this.primaryStage);
        db = new Database();

        this.view.getRegisterButton().setOnAction(actionEvent -> {
            if(     validateConfirm()&&
                    validateEmail()&&
                    validateNumberStart()&&
                    validateNumberNumeric()&&
                    validateAllFieldsFilled()&&
                    validatePasswordChar()&&
                    validateUsernameLength()&&
                    validateUsernameUnique()&&validatePasswordAlphanumeric()) {
                completeRegister();
                showSuccessPopup();
                goToLogin();
            } else {
                showErrorPopup();
            }
        });

        this.view.getLoginButton().setOnAction(actionEvent -> {
            goToLogin();

        });
    }

    private void showSuccessPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Registered Successfully!");
        alert.showAndWait();
    }

    private  void goToLogin() {
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    private void completeRegister() {
        String userID =generateID();
        String username = view.getUsernameField().getText();
        String password = view.getPasswordField().getText();
        String role = "User";
        String address = view.getAddressField().getText();
        String phone = view.getPhoneField().getText();
        RadioButton genderRadio = (RadioButton) view.getTg().getSelectedToggle();
        String gender = genderRadio.getText();



        db.connect();
        Connection connection = db.getConnection();

        if (connection != null) {
            try {
                // Prepare a SQL query to check if the email and password match
                String query = "INSERT INTO user VALUES(?,?,?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,userID);
                preparedStatement.setString(2,username);
                preparedStatement.setString(3,password);
                preparedStatement.setString(4,role);
                preparedStatement.setString(5,address);
                preparedStatement.setString(6,phone);
                preparedStatement.setString(7,gender);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
        }


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
        for (User user : users
        ) {
            int temp2 = Integer.parseInt(user.getUserID().substring(2));
            if (temp < temp2 && user.getUserID().contains("CU")) {
                temp = temp2;

            }
        }
        return String.format("CU%03d", temp + 1);
    }

    private boolean validateEmail() {
        return this.view.getEmailField().getText().endsWith("@gmail.com");
    }

    private boolean validateUsernameUnique() {
        String username = this.view.getUsernameField().getText();
        ArrayList<User> users = getUser();
        for (User user : users
        ) {
            if (username.equals(user.getUserName())) {
                return false;
            }
        }
        return true;
    }

    private boolean validateUsernameLength() {
        String username = this.view.getUsernameField().getText();
        if (username.length() < 5 || username.length() > 20) {
            return false;
        }
        return true;
    }

    private boolean validatePasswordAlphanumeric() {
        String password = this.view.getPasswordField().getText();
        for (char c : password.toCharArray()) {
            if (!((c >= '0' && c <= '9') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= 'a' && c <= 'z'))) {
                return false;
            }
        }

        return true;
    }

    private boolean validatePasswordChar() {
        String password = this.view.getPasswordField().getText();
        if (password.length() < 5) return false;
        return true;
    }

    private boolean validateConfirm() {
        String password = this.view.getPasswordField().getText();
        String confirm = this.view.getConfirmField().getText();
        return password.equals(confirm);
    }

    private boolean validateNumberNumeric() {
        String number = this.view.getPhoneField().getText();
        for (char c : number.toCharArray()) {
            if (!((c >= '0' && c <= '9') || c=='+')) {
                return false;
            }
        }
        return true;
    }

    private boolean validateNumberStart() {
        String number = this.view.getPhoneField().getText();
        return number.startsWith("+62");
    }

    private boolean validateAllFieldsFilled() {
        String username = this.view.getUsernameField().getText();
        String email = this.view.getEmailField().getText();
        String password = this.view.getPasswordField().getText();
        String confirm = this.view.getConfirmField().getText();
        String number = this.view.getPhoneField().getText();
        String address = this.view.getAddressField().getText();
        boolean gender = this.view.getTg().getSelectedToggle() == null;
        boolean tc = this.view.getTc().isSelected();

        if (username.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirm.isEmpty() ||
                number.isEmpty() ||
                address.isEmpty() ||
                gender ||
                !tc
        ) return false;
        return true;
    }

    private void showErrorPopup() {
        ArrayList<String> errors = new ArrayList<>();
        if (!validateEmail()) errors.add("Email must end with ‘@gmail.com'");
        if (!validateUsernameUnique()) errors.add("Username must be unique.");
        if (!validateUsernameLength()) errors.add("Username must be 5-20 characters.");
        if (!validatePasswordAlphanumeric()) errors.add("Password must be alphanumeric.");
        if (!validatePasswordChar()) errors.add("Password must be at least 5 characters.");
        if (!validateConfirm()) errors.add("Confirm password must equals to password.");
        if (!validateNumberNumeric()) errors.add("Phone number must be numeric.");
        if (!validateNumberStart()) errors.add("Phone number must start with ‘+62’.");
        if (!validateAllFieldsFilled()) errors.add("All fields must be filled.");
        StringBuilder sb = new StringBuilder();

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to Register");
        for (String err:errors
             ) {
            sb.append(err+"\n");
        }
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }



}
