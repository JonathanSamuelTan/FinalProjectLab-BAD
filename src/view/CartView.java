package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Cart;
import model.Database;
import model.Product;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartView {
    private Stage primaryStage;
    private User userSession;
    private Label titleLabel, prodNameLabel, prodDetailLabel, prodPriceLabel, quantityLabel, totalLabel, totalPriceLabel, orderInfoLabel, userInfoLabel, phoneInfoLabel, addressInfoLabel;
    private Spinner quantitySpinner;
    private Button updateButton, removeButton, makePurchaseButton;
    private ObservableList<Cart> cartItems;
    private ListView listView;

    public CartView(Stage stage, User userSession) {
        this.primaryStage = stage;
        this.userSession = userSession;
        showCartView();
    }

    private void showCartView() {
        primaryStage.setTitle("Cart");

        // Create menu bar
        Navbar navbar = new Navbar();

        // Create center content
        BorderPane borderPane = createCenterContent();

        // Create layout
        VBox vbox = new VBox();
        vbox.getChildren().addAll(navbar.userNavbar(primaryStage, userSession), borderPane);


        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createCenterContent() {
        BorderPane borderPane = new BorderPane();
        String name = userSession.getUserName();

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
        Text text = new Text(name + "'s Cart");
        text.setFont(Font.font("Arial Black", 27));
        GridPane.setMargin(text, new Insets(0, 0, 0, 10));
        grid.add(text, 0, 0);

        ListView<Cart> listView = new ListView<>();
        listView.setMaxHeight(300.0);
        GridPane.setMargin(listView, new Insets(0, 0, 0, 10));
        GridPane.setRowIndex(listView, 1);
        GridPane.setValignment(listView, VPos.TOP);
        grid.add(listView, 0, 1);

        cartItems = FXCollections.observableArrayList(populateListView());
        listView.setItems(cartItems);

        VBox vBox = new VBox();
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(100.0);
        vBox.setSpacing(10.0);
        GridPane.setColumnIndex(vBox, 1);
        GridPane.setRowIndex(vBox, 1);

        Text welcomeText = new Text("Welcome, " + name);
        welcomeText.setFont(Font.font("Arial Black", 15));
        Text selectProductText = new Text("Select Product to add and remove");

        vBox.getChildren().addAll(welcomeText, selectProductText);
        GridPane.setMargin(vBox, new Insets(0, 0, 0, 10));

        // Add event listener to update the right side when a product is selected
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayProductDetails(newSelection, vBox);
            }
        });

        grid.add(vBox, 1, 1);

        borderPane.setCenter(grid);
        return borderPane;
    }

    private void displayProductDetails(Cart cart, VBox vBox) {
        // Clear the existing content
        vBox.getChildren().clear();

        // Display product details
        Label nameLabel = new Label(cart.getProductID().get());
        nameLabel.setFont(Font.font("Arial Black", 12));
        Text descLabel = new Text(cart.getProductID().get());
        descLabel.setWrappingWidth(200); // Set wrapping width
        descLabel.setLineSpacing(5); // Set line spacing
        descLabel.setTextAlignment(TextAlignment.LEFT);
        Text priceLabel = new Text("Price: Rp." + cart.getQuantity().get());

        // quantity
        HBox qtc = new HBox();
        Label qtcLabel = new Label("Quantity: ");
        // Create a SpinnerValueFactory with custom min, max, and initial values
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

        // Create a Spinner with the custom value factory
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(valueFactory);

        qtc.getChildren().addAll(qtcLabel, spinner);

        Button addCartBTN = new Button("Add to Cart");

        if (userSession.getUserRole().equalsIgnoreCase("admin")) {
            vBox.getChildren().addAll(nameLabel, descLabel, priceLabel);
        } else {
            vBox.getChildren().addAll(nameLabel, descLabel, priceLabel, qtc, addCartBTN);
        }

    }

    public List<Cart> populateListView() {
        List<Cart> cartItems = new ArrayList<>();
        try {
            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM product";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Cart cart = new Cart(resultSet.getString("productID"),resultSet.getString("product_name"),resultSet.getInt("product_price"));
                cartItems.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }
}
