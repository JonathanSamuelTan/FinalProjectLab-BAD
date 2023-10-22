package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.*;

public class Product {
	 private final StringProperty productID;
	 private final StringProperty productName;
	 private final DoubleProperty productPrice;
	 private final StringProperty productDesc;

    public Product(String productID, String productName, Double productPrice, String productDesc) {
        super();
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.productDesc = new SimpleStringProperty(productDesc);
    }    
    
    public static ArrayList<Product> retreiveRecord() {
		ArrayList<Product> products=new ArrayList<>();
		
		String query = "SELECT productID,product_name,product_price,product_description FROM Product";
		try (PreparedStatement preparedStatement = Database.connection.prepareStatement(query)) {
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
    
    public static void updateProduct(String productID,String name,double price,String description) {
    	String updateSQL = "UPDATE Product SET product_name = ?, product_price = ?, product_description = ? WHERE productID = ?";
        try (PreparedStatement preparedStatement = Database.connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, productID);
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
    
    public static void insertProduct(String name,double price,String description) throws SQLException {
		  //belom masukin productID
	        String insertSQL = "INSERT INTO product (product_name,  product_price,product_description) VALUES (?, ?, ?)";
   
	        try (PreparedStatement preparedStatement = Database.connection.prepareStatement(insertSQL)) {
	            preparedStatement.setString(1, name);
	            preparedStatement.setDouble(2, price);
	            preparedStatement.setString(3, description);

	            // Execute the insert statement
	            preparedStatement.executeUpdate();

	            System.out.println("Product added successfully!");
	        }
    	}
    
    public static void removeProduct(String productID) {
    	 
        String deleteSQL = "DELETE FROM Product WHERE productID = ?";
        try ( PreparedStatement preparedStatement = Database.connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, productID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product with productId " + productID + " deleted successfully.");
            } else {
                System.out.println("Product with productId " + productID + " not found or not deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    public static void updateProduct(String productID) {
    	System.out.println("update product id: "+productID);
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public DoubleProperty productPriceProperty() {
        return productPrice;
    }

    public StringProperty productDescProperty() {
        return productDesc;
    }
    
 // Getter methods (you can keep these)
    public String getProductID() {
        return productID.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public Double getProductPrice() {
        return productPrice.get();
    }

    public String getProductDesc() {
        return productDesc.get();
    }
    
    
}
