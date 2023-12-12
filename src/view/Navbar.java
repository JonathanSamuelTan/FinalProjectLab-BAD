package view;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.User;

public class Navbar {

    public MenuBar userNavbar(Stage primaryStage, User userSession) {
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

        // Event Listener
        homePageItem.setOnAction(event -> {
        	HomePageView homePage = new HomePageView(primaryStage,userSession);
            homePage.show();
        });

        myCartItem.setOnAction(event -> {
            CartView cartView = new CartView(primaryStage, userSession);
        });

        transactionHistoryItem.setOnAction(event -> {
            TransactionScene transactionScene = new TransactionScene(primaryStage, userSession);
            transactionScene.show();
        });

        logOutItem.setOnAction(event -> {
        	LoginView loginView = new LoginView(primaryStage);
        	loginView.show();
        });

        return menuBar;
    }

    public MenuBar adminNavbar(Stage primaryStage, User userSession) {
        MenuBar menuBar = new MenuBar();

        Menu menuHome = new Menu("Home");
        MenuItem homePageItem = new MenuItem("Home Page");
        menuHome.getItems().add(homePageItem);

        Menu menuProduct = new Menu("Manage Product");
        MenuItem ProductItem = new MenuItem("Manage Product");
        menuProduct.getItems().add(ProductItem);

        Menu menuAccount = new Menu("Account");
        MenuItem logOutItem = new MenuItem("Log Out");
        menuAccount.getItems().addAll(logOutItem);

        menuBar.getMenus().addAll(menuHome, menuProduct, menuAccount);

        // Event Listener
        homePageItem.setOnAction(event -> {
        	HomePageView homePage = new HomePageView(primaryStage,userSession);
            homePage.show();
        });

        ProductItem.setOnAction(event -> {
            EditProductScene product = new EditProductScene(primaryStage, userSession);
            product.show();
        });

        logOutItem.setOnAction(event -> {
        	LoginView loginView = new LoginView(primaryStage);
        	loginView.show();
        });

        return menuBar;
    }
}
