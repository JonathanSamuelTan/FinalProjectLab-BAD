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


	public StringProperty getProductID() {
		return productID;
	}

	public StringProperty getUserID() {
		return userID;
	}

	public IntegerProperty getQuantity() {
		return quantity;
	}

}
