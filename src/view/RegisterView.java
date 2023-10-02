package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class RegisterView {

    private VBox column;
    private BorderPane view;

    public RegisterView(){
        view = new BorderPane();
        createVBox();
        createLayout();
    }
    public void createVBox() {
        column = new VBox();
        column.setSpacing(16);
        column.setAlignment(Pos.CENTER);

        //Header
        Label header = new Label("Register");
        Font headerFont = new Font(30);
        Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,30);
        header.setFont(headerFont);

        //Fields
        GridPane fields = new GridPane();
        fields.setAlignment(Pos.CENTER);
        fields.setVgap(8);
        fields.setHgap(16);
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Input Username...");
        fields.setGridLinesVisible(true);
        fields.add(usernameLabel,0,0);
        fields.add(usernameField,1,0);


        //Arrange column
        column.getChildren().add(header);
        column.getChildren().add(fields);
    }

    public void createLayout() {
        view.setCenter(column);
        view.setPadding(new Insets(10,50,10,50));
    }

    public Parent getView() {
        return view;
    }
}
