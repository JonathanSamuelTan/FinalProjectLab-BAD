package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.binding.Bindings;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.Database;
import model.Product;

public class EditProductScene {
 	private Database db;
    private Stage primaryStage;
 	User userSession;
    ArrayList<Product> productList;
    boolean formDetails=false;

	public EditProductScene(Stage primaryStage,User userSession) {
        this.primaryStage = primaryStage;
        this.db = new Database();
        this.userSession=userSession;
	}
	
	public void show() {
		primaryStage.setTitle("Edit Product Page");
        Navbar navbar = new Navbar();
        BorderPane borderPane = new BorderPane();
        VBox root=new VBox(10);
		HBox mainContent = new HBox(15);
        root.setPadding( new Insets(20));
        
        productList=retreiveRecord();

		if(!userSession.getUserRole().equals("admin")) {
			new HpUserView(primaryStage,userSession).show();
			return;
		}
		
        Label title=new Label("Manage Products");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28)); // You can adjust the font and size

	
        VBox sideBar = new VBox(10);  
        Label productNameLabel= new Label("welcome, "+userSession.getUserName());
        Text descriptionLabel = new Text("select product to update");
        descriptionLabel.wrappingWidthProperty().bind(sideBar.widthProperty());
 

        Label priceLabel = new Label("");

        
        VBox buttons=new VBox(10);
        ToggleButton addToggleButton = new ToggleButton("Add Product");
         Button removeButton=new Button("Remove");
        ToggleButton updateToggleButton=new ToggleButton("Update");
        buttons.getChildren().addAll(addToggleButton,new HBox(10,updateToggleButton,removeButton));
        double buttonWidth = 120; 
        addToggleButton.setMinWidth(buttonWidth);
        removeButton.setMinWidth(buttonWidth);
        updateToggleButton.setMinWidth(buttonWidth);
        
         VBox addProductForm=addProductForm();
           
         sideBar.setMinWidth(400);
         sideBar.setMaxWidth(400);
        
        sideBar.getChildren().addAll(
            	productNameLabel,
            	descriptionLabel,
            	priceLabel,
            	addToggleButton
        		);
        
        
        addToggleButton.setOnAction(event -> {
            if (addToggleButton.isSelected()) {
            	formDetails=true;
            	sideBar.getChildren().remove(addToggleButton);
            	sideBar.getChildren().remove(buttons);
            	sideBar.getChildren().add(addProductForm);
            }  
        });
        
        
        TableView<Product> tableView = new TableView<>();
 
        TableColumn<Product, String> firstNameColumn = new TableColumn<>("product");
        firstNameColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        firstNameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.99));
        tableView.getColumns().addAll(firstNameColumn);
        
        ObservableList<Product> data = FXCollections.observableArrayList();
        for (Product product : productList) {
            data.add(product);  
        }
        tableView.setItems(data);

         tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           VBox updateProductForm=updateProductForm(newValue.productIDProperty().get());
           if(!formDetails) {
        	   sideBar.getChildren().remove(buttons);
               sideBar.getChildren().add(buttons);

           }
		  
 			productNameLabel.setText(newValue.productNameProperty().get());
			descriptionLabel.setText(newValue.productDescProperty().get());
			priceLabel.setText("Price: Rp."+ newValue.productPriceProperty().get() );
			
			removeButton.setOnAction(event-> {
				formDetails=true;
				sideBar.getChildren().remove(addToggleButton);
				sideBar.getChildren().remove(buttons);
				VBox removeConfirmation=removeConfirmation(newValue.productIDProperty().get());
				sideBar.getChildren().add(removeConfirmation);
			  });
			
			updateToggleButton.setOnAction(event-> {
				formDetails=true;
				sideBar.getChildren().remove(addToggleButton);
				sideBar.getChildren().add(updateProductForm);
				sideBar.getChildren().remove(buttons);
			  });
        });
       
         
        //main bar
        mainContent.getChildren().addAll( tableView,sideBar);
        root.getChildren().addAll(title,mainContent);
        borderPane.setTop(navbar.adminNavbar(primaryStage,userSession));
        borderPane.setCenter(root);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	VBox removeConfirmation(String productID) {
		VBox confirmation=new VBox(10);
		HBox buttons=new HBox(10);
		Button removeButton=new Button("Remove Product");
		Button backButton=new Button("Back");
		Label confirmationLabel=new Label("Are you sure, you want to remove this product?");
        confirmationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // You can adjust the font and size

		
		buttons.getChildren().addAll(removeButton,backButton);
		
		backButton.setOnAction(event-> {  
	        new EditProductScene(primaryStage,userSession).show();
       });
		
		confirmation.getChildren().addAll(confirmationLabel,buttons);
		
		removeButton.setOnAction(event-> {
			removeProduct(productID);
			new EditProductScene(primaryStage,userSession).show();
		  });
		

		return confirmation;
	}
	
	VBox updateProductForm(String productID) {
		VBox inputBox = new VBox(10); // 10 is the spacing between elements
	    Label labelPrice = new Label("Update Product");
	    TextField fieldPrice = new TextField();
	    fieldPrice.setPromptText("Input new price");
		 
	    HBox buttons=new HBox(10);
		Button updateButton = new Button("Update Button");
		Button backButton=new Button("Back");
		buttons.getChildren().addAll(updateButton,backButton);
		updateButton.setOnAction(event-> {
	        String price = fieldPrice.getText();
 	        
	        updateProduct(productID,price);
	        new EditProductScene(primaryStage,userSession).show();
       });
		backButton.setOnAction(event-> {  
	        new EditProductScene(primaryStage,userSession).show();
       });
		
		
		 
        inputBox.getChildren().addAll(labelPrice, fieldPrice,buttons);

        return inputBox;
	}
	
	VBox addProductForm() {
		VBox inputBox = new VBox(10);  
		
		Label labelName = new Label("Input Product Name:");
	    TextField fieldName = new TextField();
	    fieldName.setPromptText("Input product name");
	    
	    Label labelPrice = new Label("Input Product Price:");
	    TextField fieldPrice = new TextField();
 		fieldPrice.setPromptText("Input product price");

	    
	    Label labelDescription= new Label("Input product description");
	    TextArea fieldDescription = new TextArea();
 		fieldDescription.setPromptText("Input product description");
		fieldDescription.setPrefColumnCount(20);
		fieldDescription.setPrefRowCount(5);
		fieldDescription.setWrapText(true);


		HBox buttons=new HBox(10);
		Button addButton = new Button("Add Product");
		Button backButton=new Button("Back");
		buttons.getChildren().addAll(addButton,backButton);
		addButton.setOnAction(event-> {
       	    String name = fieldName.getText();
	        String price = fieldPrice.getText();
	        String description= fieldDescription.getText();
	        
	        try {
				insertProduct(name, price, description);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        fieldName.clear();
	        fieldPrice.clear();
	        fieldDescription.clear();
	        
	        new EditProductScene(primaryStage,userSession).show();
       });
		
		backButton.setOnAction(event-> {  
	        new EditProductScene(primaryStage,userSession).show();
       });
		
		 
        inputBox.getChildren().addAll(labelName, fieldName, labelPrice, fieldPrice,labelDescription,fieldDescription,buttons);
        
        return inputBox;
	}
	
	 public void updateProduct(String productID,String priceText) {
		 if(priceText.trim().isEmpty()) {
			 System.out.println("please fill the require price text");
			 return;
		 }
		 
	     double price;
	     try {
	        price= Double.parseDouble(priceText);
	     } catch (NumberFormatException e) {
	    	System.out.println("input only number in price");
	        return;  
	     }
	    
	    if(price<=0) {
	    	return;
	    }
		    
		  
    	String updateSQL = "UPDATE Product SET product_price = ? WHERE productID = ?";
        Connection conn = db.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSQL)) {
            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, productID);  

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product with productID " + productID + " updated successfully.");
            } else {
                System.out.println("Product with productID " + productID + " not found or not updated.");
            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	  void insertProduct(String name,String priceText,String description) throws SQLException {
		  if (name.isEmpty() && priceText.trim().isEmpty() && description.isEmpty()) {
			    System.out.println("please fill the required text field");
	            return;
	        }
		  
 		  double price;
		    try {
		        price= Double.parseDouble(priceText);
		    } catch (NumberFormatException e) {
		    	System.out.println("input only number in price");
 		        return;  
		    }
		    
		    if(price<=0) {
		    	return;
		    }
		    
 		  for(Product product: productList) {
			  if(name == product.getProductName()) {
				  System.out.println("product name cannot be same");
				  return;
			  }
		  }
	        
		  
		  String insertSQL = "INSERT INTO product (product_name,  product_price,product_description) VALUES (?, ?, ?)";
           Connection conn = db.getConnection();
	        try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
	            preparedStatement.setString(1, name);
	            preparedStatement.setDouble(2, price);
	            preparedStatement.setString(3, description);

	            // Execute the insert statement
	            preparedStatement.executeUpdate();

	            System.out.println("Product added successfully!");
	        }
	  }
	  
	 public void removeProduct(String productID) {
	        String deleteSQL = "DELETE FROM Product WHERE productID = ?";
	           Connection conn = db.getConnection();
	        try ( PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
	            preparedStatement.setString(1, productID);
	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Product with productId " + productID + " deleted successfully.");
	            } else {
	                System.out.println("Product with productId " + productID + " not found or not deleted.");
	            }
	            
	           // new EditProductScene(primaryStage,userSession);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    }
	 
	  public ArrayList<Product> retreiveRecord() {
			ArrayList<Product> products=new ArrayList<>();
	        Connection conn = db.getConnection();
			
			String query = "SELECT productID,product_name,product_price,product_description FROM Product";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			    ResultSet resultSet = preparedStatement.executeQuery();

			    while (resultSet.next()) {
			    	String productID = resultSet.getString("productID");
			    	String productName = resultSet.getString("product_name");
			    	Double productPrice = resultSet.getDouble("product_price");
			    	String productDesc = resultSet.getString("product_description");
			        
			        // Create a new ArrayList to store the data
			    	Product product=new Product(productID,productName,productPrice,productDesc);
			    	products.add(product);
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
			
			return products;
		}
	 
}
