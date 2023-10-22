package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.User;
import model.Database;
import model.Product;

public class ManageProductView {
	//private ObservableList<Product> products = FXCollections.observableArrayList();
	private Database db;
    private Stage primaryStage;
	//input new product
	private Button addButton = new Button("Add Product");
	 

	public ManageProductView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.db = new Database();
	}
	
	public void show() {
		primaryStage.setTitle("Product CRUD App");
        addButton.setOnAction(event-> {
        	moveAddProduct();
        });
        
        //---------------------------SIDE BAR--------------------------------//
        VBox sideBar = new VBox(5); // 10 is the spacing between elements
        Label productNameLabel= new Label("welcome,admin");
        Label descriptionLabel = new Label("select product to update");
        Button removeButton=new Button("Remove");
        Button updateButton=new Button("Update");
        sideBar.getChildren().addAll(
        	productNameLabel,
        	descriptionLabel,
            addButton,
            removeButton,
            updateButton
        );
        removeButton.setVisible(false);
        updateButton.setVisible(false);

        //------------------------View BAR-----------------------------//
        // Create a border with desired properties
        TableView<Product> tableView = new TableView<>();
 
        TableColumn<Product, String> firstNameColumn = new TableColumn<>("product");
        firstNameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        firstNameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.99));
        tableView.getColumns().addAll(firstNameColumn);
        
        // Create an ObservableList to hold the data
        ArrayList<Product> productList=Product.retreiveRecord();
        ObservableList<Product> data = FXCollections.observableArrayList();
        for (Product product : productList) {
            data.add(product); // Use get() to access the String value
        }
        tableView.setItems(data);
        

        // Add a selection listener to the TableView
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // When a new item is selected, update the descriptionLabel with the productDescProperty
            	productNameLabel.setText(newValue.productNameProperty().get());
            	descriptionLabel.setText(newValue.productDescProperty().get());
            	removeButton.setVisible(true);
                updateButton.setVisible(true);
                
                removeButton.setOnAction(event-> {
                	Product.removeProduct(newValue.productIDProperty().get());
                	updateTableView(tableView);
                  });
                updateButton.setOnAction(event-> {
                	Product.updateProduct(newValue.productIDProperty().get());
                	moveUpdateProduct(newValue.productIDProperty().get());
                  });
           		 
            } else {
                // If nothing is selected, clear the descriptionLabel
            	productNameLabel.setText("");
                descriptionLabel.setText("");
                removeButton.setVisible(false);
                updateButton.setVisible(false);
            }
        });
       
         

        //main bar
        HBox root = new HBox(15);
        root.setPadding( new Insets(20));
        root.getChildren().addAll( tableView,sideBar);
 

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public void updateTableView(TableView<Product> tableView) {
	    ArrayList<Product> productList = Product.retreiveRecord();
	    ObservableList<Product> data = FXCollections.observableArrayList(productList);
	    tableView.setItems(data);
	}
	
	void moveAddProduct() {
		Scene AddProductScene= new AddProduct().createAddScene();
        primaryStage.setScene(AddProductScene);
	}
	
	void moveUpdateProduct(String productID) {
		Scene updateProductScene= new updateProduct().createUpdateScene(productID);
        primaryStage.setScene(updateProductScene);
	}

}
