package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
        Navbar navbar = new Navbar();

        // Create center content
        BorderPane borderPane = createCenterContent();

        // Create layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(navbar.userNavbar(primaryStage,userSession), borderPane);

        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private BorderPane createCenterContent() {
        BorderPane borderPane = new BorderPane();

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
        productColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        productColumn.setPrefWidth(290.0);
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

        // Add event listener to update the right side when a product is selected
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayProductDetails(newSelection, vBox);
            }
        });

        grid.add(vBox, 1, 1);

        borderPane.setCenter(grid);
        return borderPane;
    }

    private void displayProductDetails(Product product, VBox vBox) {
        // Clear the existing content
        vBox.getChildren().clear();

        // Display product details
        Label nameLabel = new Label(product.getProductName());
        nameLabel.setFont(Font.font("Arial Black", 12));
        Text descLabel = new Text(product.getProductDesc());
        descLabel.setWrappingWidth(200); // Set wrapping width
        descLabel.setLineSpacing(5); // Set line spacing
        descLabel.setTextAlignment(TextAlignment.LEFT); 
        Text priceLabel = new Text("Price: Rp." + product.getProductPrice());
        
        // quantity
        HBox qtc = new HBox();
        Label qtcLabel = new Label("Quantity: ");
     	// Create a SpinnerValueFactory with custom min, max, and initial values
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100, 0);

        // Create a Spinner with the custom value factory
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(valueFactory);
        
        qtc.getChildren().addAll(qtcLabel,spinner);

        Button addCartBTN = new Button("Add to Cart");
        
        if(userSession.getUserRole().equalsIgnoreCase("admin")) {
        	vBox.getChildren().addAll(nameLabel, descLabel,priceLabel);
        }else {
        	vBox.getChildren().addAll(nameLabel, descLabel,priceLabel,qtc,addCartBTN);
        }
        
    }

    public List<Product> populateTableView() {
        List<Product> products = new ArrayList<>();
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
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
