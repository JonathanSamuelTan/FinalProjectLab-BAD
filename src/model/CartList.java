package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CartList extends Cart{
    private StringProperty productName = new SimpleStringProperty();
    private IntegerProperty productPrice = new SimpleIntegerProperty();
    private StringProperty productDesc = new SimpleStringProperty();
    public CartList(String productID, String userID, String productName, int product_price, int quantity, String productDesc) {
        super(productID, userID, quantity);
        this.productName.set(productName);
        this.productPrice.set(product_price);
        this.productDesc.set(productDesc);
    }

    public String getProductDesc() {
        return productDesc.get();
    }

    public int getProductPrice() {
        return productPrice.get();
    }

    public String getProductName() {
        return productName.get();
    }

    @Override
    public String toString() {
        return getQuantity() + "x " + getProductName() + " (Rp." + getProductPrice() + ")";
    }
}
