package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
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
		
        Label title=new Label("Manage Products");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28)); 

	
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
        confirmationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14)); 

		
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
		VBox inputBox = new VBox(10);
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
	        
	        insertProduct(name, price, description);
	        
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
	
	 public static Alert createErrorAlert(String contentText) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText(null);
	        alert.setContentText(contentText);
	        return alert;
		 }
		 
	 void insertProduct(String name, String priceText, String description) {
		    // Validate inputs
		    if (name.isEmpty() || priceText.trim().isEmpty() || description.isEmpty()) {
		        showAlert("Error", "Please fill in all the fields.", AlertType.ERROR);
		        return;
		    }
		    
		    for (Product product : productList) {
		        if (name.equals(product.getProductName())) {
		            showAlert("Error", "Product name must be unique.", AlertType.ERROR);
		            return;
		        }
		    }

		    Integer price = 0;
		    try {
		        price = Integer.parseInt(priceText);
		    } catch (NumberFormatException e) {
		        showAlert("Error", "Please enter a valid number for the price.", AlertType.ERROR);
		        return;
		    }

		    if (price <= 0) {
		        showAlert("Error", "Please enter a price greater than zero.", AlertType.ERROR);
		        return;
		    }

		    // Insert product into the database
		    String insertSQL = "INSERT INTO product (productID, product_name, product_price, product_des) VALUES (?, ?, ?, ?)";
		    Connection conn = db.getConnection();
		    try (PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
		        String index = Integer.toString(productList.size()+1);
		        String id = (productList.size() > 99) ? "TE" + index : (productList.size() > 9) ? "TE0" + index : "TE00" + index;

		        preparedStatement.setString(1, id);
		        preparedStatement.setString(2, name);
		        preparedStatement.setDouble(3, price);
		        preparedStatement.setString(4, description);
		        preparedStatement.executeUpdate();
		        showAlert("Success", "Product added successfully!", AlertType.INFORMATION);
		    } catch (SQLException e) {
		        e.printStackTrace();
		        showAlert("Error", "Error inserting product into the database.", AlertType.ERROR);
		    }
		}

		private void showAlert(String title, String content, AlertType alertType) {
		    Alert alert = new Alert(alertType);
		    alert.setTitle(title);
		    alert.setHeaderText(null);
		    alert.setContentText(content);
		    alert.showAndWait();
		}
	
	 public void updateProduct(String productID,String priceText) {
		 if(priceText.trim().isEmpty()) {
			 showAlert("Error", "Please fill the input text", AlertType.ERROR);
			 return;
		 }
		 
	     double price;
	     try {
	        price= Double.parseDouble(priceText);
	     } catch (NumberFormatException e) {
	    	showAlert("Error", "input only number in price", AlertType.ERROR);
	        return;  
	     }
	    
	    if(price<=0) {
	    	showAlert("Error", "price must be greater than 0", AlertType.ERROR);
	    	return;
	    }
		    
		  
    	String updateSQL = "UPDATE Product SET product_price = ? WHERE productID = ?";
        Connection conn = db.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSQL)) {
            preparedStatement.setDouble(1, price);
            preparedStatement.setString(2, productID);  

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
            	showAlert("Success", "Product with productID " + productID + " updated successfully.", AlertType.INFORMATION);
            } else {
            	showAlert("Error", "Product with productID " + productID + " not found or not updated.", AlertType.ERROR);
            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Error", "An error occurred while updating the product.", AlertType.ERROR);
	        }
	    }
	 
	
	  
	 public void removeProduct(String productID) {
	        String deleteSQL = "DELETE FROM Product WHERE productID = ?";
	           Connection conn = db.getConnection();
	        try ( PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL)) {
	            preparedStatement.setString(1, productID);
	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	            	showAlert("Success", "Product with productId " + productID + " deleted successfully.", AlertType.INFORMATION);
	            } else {
	            	showAlert("Error", "Product with productId " + productID + " not found or not deleted.", AlertType.ERROR);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	    }
	 
	  public ArrayList<Product> retreiveRecord() {
			ArrayList<Product> products=new ArrayList<>();
	        Connection conn = db.getConnection();
			
			String query = "SELECT productID,product_name,product_price,product_des FROM Product";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			    ResultSet resultSet = preparedStatement.executeQuery();

			    while (resultSet.next()) {
			    	String productID = resultSet.getString("productID");
			    	String productName = resultSet.getString("product_name");
			    	Integer productPrice = resultSet.getInt("product_price");
			    	String productDesc = resultSet.getString("product_des");
			    	Product product=new Product(productID,productName,productPrice,productDesc);
			    	products.add(product);
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
			
			return products;
		}
	 
}
