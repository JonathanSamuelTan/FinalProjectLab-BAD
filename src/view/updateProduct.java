package view;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Product;

public class updateProduct {

	private Button addButton = new Button("Add Product");
	Label labelName = new Label("Input Product Name:");
    TextField fieldName = new TextField();
    Label labelPrice = new Label("Input Product Price:");
    TextField fieldPrice = new TextField();
    TextArea fieldDescription = new TextArea();
    Connection connection;  
	public updateProduct() {
		// TODO Auto-generated constructor stub
	}
	
	public Scene createUpdateScene(String productID) {
		BorderPane root = new BorderPane();
		 root.setPadding( new Insets(30));
		 //------------------CENTER LAYOUT-----------------//
		VBox inputBox = new VBox(10); // 10 is the spacing between elements
	    Label labelDescription = new Label("Enter Product Description:");
		fieldDescription.setPromptText("Enter text here");
		fieldDescription.setPrefColumnCount(20);
		fieldDescription.setPrefRowCount(5);
		
		addButton.setOnAction(event-> {
       	 String name = fieldName.getText();
	        double price = Double.parseDouble(fieldPrice.getText());
	        String description= fieldDescription.getText();
	        
	        Product.updateProduct(productID,name, price, description);
	        
	       //clear the input
	        fieldName.clear();
	        fieldPrice.clear();
	        fieldDescription.clear();
       });
		 
        inputBox.getChildren().addAll(labelName, fieldName, labelPrice, fieldPrice,labelDescription,fieldDescription,addButton);

    	//-----------------------------SETUP------------------------------
    	Scene scene = new Scene(root, 700, 500);
        root.setCenter(inputBox);
        return scene;
        
          
	}

}
