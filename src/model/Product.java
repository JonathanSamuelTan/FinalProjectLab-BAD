package model;
import javafx.beans.property.*;

public class Product {
	 private final StringProperty productID;
	 private final StringProperty productName;
	 private final IntegerProperty productPrice;
	 private final StringProperty productDesc;
	 Database db;

    public Product(String productID, String productName, Integer productPrice, String productDesc) {
        super();
        this.productID = new SimpleStringProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.productPrice = new SimpleIntegerProperty(productPrice);
        this.productDesc = new SimpleStringProperty(productDesc);
        this.db=new Database();
    }
    
    public Product() {
        this("", "", 0, "");
    }
    
 // Getter methods for properties
    public String getProductID() {
        return productID.get();
    }

    public StringProperty productIDProperty() {
        return productID;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice.get();
    }

    public IntegerProperty productPriceProperty() {
        return productPrice;
    }

    public String getProductDesc() {
        return productDesc.get();
    }

    public StringProperty productDescProperty() {
        return productDesc;
    }

    // Setter methods for properties
    public void setProductID(String productID) {
        this.productID.set(productID);
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public void setProductPrice(int productPrice) {
        this.productPrice.set(productPrice);
    }

    public void setProductDesc(String productDesc) {
        this.productDesc.set(productDesc);
    }
}
