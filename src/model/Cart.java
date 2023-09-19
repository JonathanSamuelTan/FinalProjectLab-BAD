package model;

public class Cart {
	private String productID;
	private String userID;
	private int quantity;

	public Cart(String productID, String userID, int quantity) {
		this.productID = productID;
		this.userID = userID;
		this.quantity = quantity;
	}
		
	public String getProductID() {
		return productID;
	}

	public String getUserID() {
		return userID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
