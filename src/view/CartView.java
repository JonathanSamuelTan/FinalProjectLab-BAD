package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartView {
    private Stage primaryStage;
    private User userSession;

    private ObservableList<CartList> cartItems;
    private ListView<CartList> listView;


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

    private VBox CreateUserDetailContent() {
        VBox vBox = new VBox();
        int total = 0;
        if (cartItems != null)
        for (CartList cart : cartItems) {
            total += cart.getProductPrice() * cart.getQuantity();
        }
        Text totalText = new Text("Total: Rp." + total);
        Text orderText = new Text("Order Information");
        orderText.setFont(Font.font("Arial Black", 15));
        Text userNameText = new Text("Name: " + userSession.getUserName());
        Text userPhoneNumber = new Text("Phone Number: " + userSession.getUserPhone());
        Text userAddressText = new Text("Address: " + userSession.getUserAddress());
        Button purchase = new Button("Make Purchase");
        vBox.setSpacing(8);
        vBox.getChildren().addAll(totalText, orderText, userNameText, userPhoneNumber, userAddressText, purchase);
        return vBox;
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

        listView = new ListView<>();
        listView.setMaxHeight(300.0);




        cartItems = FXCollections.observableArrayList(populateListView());
        listView.setItems(cartItems);

        VBox userDetail = CreateUserDetailContent();
        VBox listAndDetail = new VBox();
        listAndDetail.getChildren().addAll(listView, userDetail);

        grid.add(listAndDetail, 0, 1);
        GridPane.setMargin(listAndDetail, new Insets(0, 0, 0, 10));
        VBox vBox = new VBox();
        vBox.setPrefHeight(200.0);
        vBox.setPrefWidth(100.0);
        vBox.setSpacing(10.0);
        GridPane.setColumnIndex(vBox, 1);
        GridPane.setRowIndex(vBox, 1);
        GridPane.setColumnSpan(vBox, 2);


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

    private void displayProductDetails(CartList cart, VBox vBox) {
        // Clear the existing content
        vBox.getChildren().clear();
        if (cartItems.isEmpty()) {
            Label emptyLabel = new Label("No Item in Cart!");
            Text emptyText = new Text("Consider adding one!");
            vBox.getChildren().addAll(emptyLabel, emptyText);
            return;
        }
        // Display product details
        Label nameLabel = new Label(cart.getProductName());
        nameLabel.setFont(Font.font("Arial Black", 12));
        Text descLabel = new Text(cart.getProductDesc());
        descLabel.setWrappingWidth(200); // Set wrapping width
        descLabel.setLineSpacing(5); // Set line spacing
        descLabel.setTextAlignment(TextAlignment.LEFT);
        Text priceLabel = new Text("Price: Rp." + cart.getProductPrice() * cart.getQuantity());


        // quantity
        HBox qtc = new HBox();
        Label qtcLabel = new Label("Quantity: ");
        // Create a SpinnerValueFactory with custom min, max, and initial values
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, cart.getQuantity());

        // Create a Spinner with the custom value factory
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(valueFactory);
        spinner.valueProperty().addListener((obs, oldValue, newValue) ->
                priceLabel.setText("Price: Rp." + cart.getProductPrice() * Integer.valueOf(newValue)));

        qtc.getChildren().addAll(qtcLabel, spinner);
        FlowPane flowPane = new FlowPane();
        Button updateCartBtn = new Button("Update Cart");
        Button removeCartBtn = new Button("Remove from Cart");
        flowPane.getChildren().addAll(updateCartBtn, removeCartBtn);

        vBox.getChildren().addAll(nameLabel, descLabel, priceLabel, qtc, flowPane);


    }

    public List<CartList> populateListView() {
        List<CartList> cartItems = new ArrayList<>();
        try {
            Database db = new Database();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM Cart JOIN Product ON Cart.productID = Product.productID WHERE userID = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userSession.getUserID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CartList cartItem = new CartList(
                        resultSet.getString("productID"),
                        resultSet.getString("userID"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("product_price"),
                        resultSet.getInt("quantity"),
                        resultSet.getString("product_des")
                );
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public ListView getListView() {
        return listView;
    }


}
