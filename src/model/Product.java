package model;

import java.sql.Connection;
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
	 
	  Database db;

    public Product(String productID, String productName, Double productPrice, String productDesc) {
        super();
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
        this.productDesc = new SimpleStringProperty(productDesc);
        
        this.db=new Database();
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
