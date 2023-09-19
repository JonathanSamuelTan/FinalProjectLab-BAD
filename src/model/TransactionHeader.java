package model;

public class TransactionHeader {

	private String transactionID;
	private String userID;

	public TransactionHeader(String transactionID, String userID) {
		this.transactionID = transactionID;
		this.userID = userID;
	}

	public String getTransactionIDtransactionID() {
		return transactionID;
	}

	public String getUserID() {
		return userID;
	}

	public void setTransactionIDtransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
