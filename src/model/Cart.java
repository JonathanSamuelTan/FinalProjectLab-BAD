package model;
import javafx.beans.property.*;
public class Cart {
	private final StringProperty productID = new SimpleStringProperty();

	private final StringProperty userID = new SimpleStringProperty();
	private final IntegerProperty quantity = new SimpleIntegerProperty();




	public Cart(String productID, String userID, int quantity) {
		this.productID.set(productID);
		this.userID.set(userID);
		this.quantity.set(quantity);

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


}
