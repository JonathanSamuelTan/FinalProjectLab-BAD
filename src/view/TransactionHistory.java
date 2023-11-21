package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

public class TransactionHistory {
	
    public void start(Stage primaryStage) {
    	
    	Navbar navbar = new Navbar();
    	User user = new User("qwer", "asd", "ert", "asdr", "wrhgwg", "nicholas", "aegfaqegf");
    	
    	
        primaryStage.setTitle("Stefanie's purchase history");

        BorderPane borderPane = new BorderPane();
        Text titleText = new Text("Stefanie's purchase history");
        titleText.setFont(Font.font("Arial Black", 24));
        borderPane.setPadding(new Insets(20));
        borderPane.setTop(titleText);

        TableView<Object> tableView = new TableView<>();
        TableColumn<Object, Object> transactionIDColumn = new TableColumn<>("TransactionID");
        transactionIDColumn.setPrefWidth(200);
        tableView.getColumns().add(transactionIDColumn);
        tableView.setPrefSize(200, 200);
        borderPane.setLeft(tableView);

        VBox centerVBox = new VBox();
        Text noHistoryText = new Text("There's No History");
        noHistoryText.setFont(Font.font("Arial Black", 12));
        Text considerText = new Text("Consider Purchasing Our Products");
        considerText.setFont(Font.font("Arial", 12));
        centerVBox.getChildren().addAll(noHistoryText, considerText);
        centerVBox.setPadding(new Insets(13));
        borderPane.setCenter(centerVBox);

        MenuBar menuBar = navbar.userNavbar(primaryStage, user);
        
        
        VBox root = new VBox(menuBar, borderPane);
        Scene scene = new Scene(root, 640, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    }

