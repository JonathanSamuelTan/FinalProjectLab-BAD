package view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Navbar {
	
	public MenuBar userNavbar() {
		MenuBar menuBar = new MenuBar();

        Menu menuHome = new Menu("Home");
        MenuItem homePageItem = new MenuItem("Home Page");
        menuHome.getItems().add(homePageItem);

        Menu menuCart = new Menu("Cart");
        MenuItem myCartItem = new MenuItem("My Cart");
        menuCart.getItems().add(myCartItem);

        Menu menuAccount = new Menu("Account");
        MenuItem transactionHistoryItem = new MenuItem("Transaction History");
        MenuItem logOutItem = new MenuItem("Log Out");
        menuAccount.getItems().addAll(transactionHistoryItem, logOutItem);

        menuBar.getMenus().addAll(menuHome, menuCart, menuAccount);

        return menuBar;
	}
}
