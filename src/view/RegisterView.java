package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class RegisterView {
    private Stage primaryStage;
    private Label header;
    private Label usernameLabel;
    private TextField usernameField;
    private Label emailLabel;
    private TextField emailField;
    private Label passwordLabel;
    private PasswordField passwordField;
    private Label confirmLabel;
    private PasswordField confirmField;
    private Label phoneLabel;
    private TextField phoneField;
    private Label addressLabel;
    private TextArea addressField;
    private Label genderLabel;
    private RadioButton genderButtonMale;
    private RadioButton genderButtonFemale;
    private CheckBox tc;
    private Label loginLabel;
    private Hyperlink loginButton;
    private Button registerButton;

    private ToggleGroup tg;

    public RegisterView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showRegisterScene() {
        primaryStage.setTitle("Register");
        BorderPane bp = new BorderPane();
        bp.setCenter(createRegisterForm());
        bp.setPadding(new Insets(50));
        Scene scene = new Scene(bp);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createRegisterForm() {
        GridPane fields = new GridPane();

        //Header
        header = new Label("Register");
        header.setFont(Font.font("Arial Black", 32));

        //Fields
        fields.setAlignment(Pos.CENTER);
        fields.setVgap(10);
        fields.setHgap(10);

        usernameLabel = new Label("Username :");
        usernameField = new TextField();

        emailLabel = new Label("Email :");
        emailField = new TextField();

        passwordLabel = new Label("Password :");
        passwordField = new PasswordField();

        confirmLabel = new Label("Confirm Password :");
        confirmField = new PasswordField();

        phoneLabel = new Label("Phone Number :");
        phoneField = new TextField();

        addressLabel = new Label("Address :");
        addressField = new TextArea();

        genderLabel = new Label("Gender :");
        genderButtonMale = new RadioButton("Male");
        genderButtonFemale = new RadioButton("Female");
        tg = new ToggleGroup();
        genderButtonMale.setToggleGroup(tg);
        genderButtonFemale.setToggleGroup(tg);

        TilePane tp = new TilePane(genderButtonMale, genderButtonFemale);

        tc = new CheckBox("I agree to all terms and condition");

        loginLabel = new Label("Have an account? ");
        loginButton = new Hyperlink("login here");

        registerButton = new Button("Register");

        TilePane tp2 = new TilePane(loginLabel, loginButton);
        usernameField.setPromptText("Input username...");
        emailField.setPromptText("Input email...");
        passwordField.setPromptText("Input password...");
        confirmField.setPromptText("Input confirm password...");
        phoneField.setPromptText("Input phone number...");
        addressField.setPromptText("Input address...");


        fields.add(header, 1, 0);
        fields.add(usernameLabel, 0, 1);
        fields.add(usernameField, 1, 1);

        fields.add(emailLabel, 0, 2);
        fields.add(emailField, 1, 2);

        fields.add(passwordLabel, 0, 3);
        fields.add(passwordField, 1, 3);

        fields.add(confirmLabel, 0, 4);
        fields.add(confirmField, 1, 4);

        fields.add(phoneLabel, 0, 5);
        fields.add(phoneField, 1, 5);

        fields.add(addressLabel, 0, 6);
        fields.add(addressField, 1, 6);

        fields.add(genderLabel, 0, 7);
        fields.add(tp, 1, 7);

        fields.add(tc, 1, 8);

        fields.add(tp2, 1, 9);

        fields.add(registerButton, 1, 10);


        //Arrange column
        return fields;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmField() {
        return confirmField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public TextArea getAddressField() {
        return addressField;
    }

    public RadioButton getGenderButtonMale() {
        return genderButtonMale;
    }

    public RadioButton getGenderButtonFemale() {
        return genderButtonFemale;
    }

    public CheckBox getTc() {
        return tc;
    }

    public Hyperlink getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public ToggleGroup getTg() {
        return tg;
    }
}
