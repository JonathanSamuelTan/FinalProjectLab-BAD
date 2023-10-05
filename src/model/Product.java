package model;

import javafx.beans.property.*;

public class Product {
    private final StringProperty productID = new SimpleStringProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private final IntegerProperty productPrice = new SimpleIntegerProperty();
    private final StringProperty productDesc = new SimpleStringProperty();

    public Product(String productID, String productName, int productPrice, String productDesc) {
        setProductID(productID);
        setProductName(productName);
        setProductPrice(productPrice);
        setProductDesc(productDesc);
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
