package model;

public class TransactionDetail {

	private String transactionID;
	private String productID;
	private int quantity;


	public TransactionDetail(String transactionID, String productID, int quantity) {
		this.transactionID = transactionID;
		this.productID = productID;
		this.quantity = quantity;
	}
		
	public String getTransactionID() {
		return transactionID;
	}

	public String getProductID() {
		return productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
