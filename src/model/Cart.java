package model;
import javafx.beans.property.*;
public class Cart {
	private final StringProperty productID = new SimpleStringProperty();
	private final StringProperty productName = new SimpleStringProperty();
	private final StringProperty userID = new SimpleStringProperty();
	private final IntegerProperty quantity = new SimpleIntegerProperty();
	private final IntegerProperty price = new SimpleIntegerProperty();



	public Cart(String productID,String productName, String userID, int quantity, int price) {
		this.productID.set(productID);
		this.productName.set(productName);
		this.userID.set(userID);
		this.quantity.set(quantity);
		this.price.set(price);
	}


	public String getProductID() {
		return productID.get();
	}

	public String getUserID() {
		return userID.get();
	}

	public Integer getQuantity() {
		return quantity.get();
	}


	public String getProductName() {
		return productName.get();
	}

	public Integer getPrice() {
		return price.get();
	}
}
