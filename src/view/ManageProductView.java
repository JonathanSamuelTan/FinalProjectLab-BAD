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
import model.Product;

public class ManageProductView {
	//private ObservableList<Product> products = FXCollections.observableArrayList();
	private Database db;
    private Stage primaryStage;
    //table
	private TableView<Product> tableView = new TableView<>();
	//input new product
	private ToggleButton toggleButton = new ToggleButton("Add Product");
	private Button addButton = new Button("Add Product");
	Label labelName = new Label("Input Product Name:");
    TextField fieldName = new TextField();
    Label labelPrice = new Label("Input Product Price:");
    TextField fieldPrice = new TextField();
    TextArea fieldDescription = new TextArea();
    Connection connection;  


	public ManageProductView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.db = new Database();
        connection=db.getConnection();
	}
	
	public void show() {
		//text area cuman bisa di taro disini., gabisa diatas
	    Label labelDescription = new Label("Enter Product Description:");
		fieldDescription.setPromptText("Enter text here");
		fieldDescription.setPrefColumnCount(20);
		fieldDescription.setPrefRowCount(5);
		 
	   
	   primaryStage.setTitle("Product CRUD App");
	   createTable();
        
        //input box
        VBox inputBox = new VBox(10); // 10 is the spacing between elements
        inputBox.getChildren().addAll(labelName, fieldName, labelPrice, fieldPrice,labelDescription,fieldDescription);
        
        // vbox.getChildren().add(button);
        //vbox.getChildren().remove(button);
        
        addButton.setOnAction(event-> {
        	try {
				insertProduct();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
        //sideBar
        VBox sideBar = new VBox(10); // 10 is the spacing between elements
        sideBar.getChildren().addAll(
    		new Label("Welcome, Admin!"),
            new Label("Select a product to update"),
            toggleButton
        );
        
        toggleButton.setOnAction(event -> {
        	if(toggleButton.isSelected()) {
        		sideBar.getChildren().add(inputBox);
        		inputBox.getChildren().add(addButton);
                toggleButton.setText("Back");
        	}else {
        		sideBar.getChildren().remove(inputBox);
        		inputBox.getChildren().remove(addButton);
                toggleButton.setText("Add Product");
        	}
        
        });
        
        //main bar
        HBox root = new HBox(10);
        root.getChildren().addAll(tableView, sideBar);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	void createTable() {
		TableView<Product> productTable = new TableView<>();
	       productTable.setEditable(true);

	       TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
	       TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
    
	        
	       productTable.getColumns().addAll(nameColumn, priceColumn);
	       addButton.setOnAction(e -> {
				try {
					insertProduct();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
	}
	
	void insertProduct() throws SQLException {
		  //belom masukin productID
		    String name = fieldName.getText();
	        double price = Double.parseDouble(fieldPrice.getText());
	        String description= fieldDescription.getText();
	        
	        String insertSQL = "INSERT INTO product (product_name,  product_price,product_description) VALUES (?, ?, ?)";
     
	        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
	            preparedStatement.setString(1, name);
	            preparedStatement.setDouble(2, price);
	            preparedStatement.setString(3, description);

	            // Execute the insert statement
	            preparedStatement.executeUpdate();

	            System.out.println("Product added successfully!");
	        }
	        
	        //clear the input
	        fieldName.clear();
	        fieldPrice.clear();
	        fieldDescription.clear();
}

}
