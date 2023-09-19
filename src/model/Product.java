package model;

public class Product {
	private String productID;
	private String productName;
	private int productPrice;
	private String productDesc;


	public Product(String productID, String productName, int productPrice, String productDesc) {
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDesc = productDesc;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

}
