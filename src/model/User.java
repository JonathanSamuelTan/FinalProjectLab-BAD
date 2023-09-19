package model;

public class User {

	private String userID;
	private String userName;
	private String userPassword;
	private String userRole;
	private String userAddress;
	private String userPhone;
	private String userGender;

	public User(String userID, String userName, String userPassword, String userRole, String userAddress,
			String userPhone, String userGender) {

		this.userID = userID;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRole = userRole;
		this.userAddress = userAddress;
		this.userPhone = userPhone;
		this.userGender = userGender;
	}

	public String getUserID() {
		return userID;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserRole() {
		return userRole;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

}
