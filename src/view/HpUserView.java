package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.Database;
import model.Product;

public class HpUserView {

    private Stage primaryStage;
    private User userSession;
    private Database db;
    private List<Product> productList;
    

    public HpUserView(Stage primaryStage, User userSession) {
        this.primaryStage = primaryStage;
        this.userSession = userSession;
        this.db = new Database();
        this.productList = new ArrayList<>();
    }

    public void show() {
        primaryStage.setTitle("Home Page");

        // Create menu bar
        MenuBar menuBar = createMenuBar();

        // Create center content
        GridPane centerGrid = createCenterGrid();
        
        

        // Create layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(menuBar, centerGrid);

        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
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

    private GridPane createCenterGrid() {
        GridPane grid = new GridPane();
        grid.setPrefHeight(577);
        grid.setPrefWidth(598);
        grid.setHgap(10);
        grid.setVgap(10);

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.SOMETIMES);
        column2.setMinWidth(10.0);
        column2.setPrefWidth(100.0);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setMaxHeight(535.0);
        row2.setMinHeight(10.0);
        row2.setPrefHeight(531.0);

        grid.getColumnConstraints().addAll(column1, column2);
        grid.getRowConstraints().addAll(row1, row2);

        Text text = new Text("SeRuput Teh");
        text.setFont(Font.font("Arial Black", 27));
        GridPane.setMargin(text, new Insets(0, 0, 0, 10));
        grid.add(text, 0, 0);

        TableView<Product> tableView = new TableView<>();
        TableColumn<Product, String> productColumn = new TableColumn<>("Product");
        productColumn.setPrefWidth(290.0);
        productColumn.setCellValueFactory(data -> data.getValue().productNameProperty());
        tableView.getColumns().add(productColumn);
        tableView.setMaxHeight(300.0);
        GridPane.setMargin(tableView, new Insets(0, 0, 0, 10));
        GridPane.setRowIndex(tableView, 1);
        GridPane.setValignment(tableView, VPos.TOP);
        grid.add(tableView, 0, 1);

        ObservableList<Product> products = FXCollections.observableArrayList(populateTableView());
        tableView.setItems(products);
        

        VBox vBox = new VBox();
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(100.0);
        vBox.setSpacing(10.0);
        GridPane.setColumnIndex(vBox, 1);
        GridPane.setRowIndex(vBox, 1);

        Text welcomeText = new Text("Welcome, " + userSession.getUserName());
        welcomeText.setFont(Font.font("Arial Black", 15));
        Text selectProductText = new Text("Select Product to View");

        vBox.getChildren().addAll(welcomeText, selectProductText);
        GridPane.setMargin(vBox, new Insets(0, 0, 0, 10));
        grid.add(vBox, 1, 1);

        return grid;
    }
    
    public List<Product> populateTableView() {
//        List<String> productNames = new ArrayList<>();
    	try {
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM product";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getString("productID"));
                product.setProductName(resultSet.getString("product_name"));
                product.setProductPrice(resultSet.getInt("product_price"));
                product.setProductDesc(resultSet.getString("product_des"));
                productList.add(product);
//                productNames.add(product.getProductName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;    	
    }

}
