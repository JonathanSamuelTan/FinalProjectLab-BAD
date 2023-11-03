package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
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
    private Database db;


    public CartView(Stage stage, User userSession) {
        this.primaryStage = stage;
        this.userSession = userSession;
        this.db = new Database();
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
        purchase.setOnAction(actionEvent -> purchasePopUp());
        vBox.setSpacing(8);
        vBox.getChildren().addAll(totalText, orderText, userNameText, userPhoneNumber, userAddressText, purchase);
        return vBox;
    }

    private void purchasePopUp() {
        Stage popup = new Stage();
        VBox center = new VBox();
        center.setSpacing(4);
        Text questionText = new Text("Are you sure you want to make purchase?");
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(4);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        yesButton.setPrefWidth(100);
        noButton.setPrefWidth(100);
        buttonBox.getChildren().addAll(yesButton, noButton);
        center.getChildren().addAll(questionText, buttonBox);

        noButton.setOnAction(actionEvent -> popup.close());
        yesButton.setOnAction(actionEvent -> {
            if (cartItems.isEmpty()) {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setTitle("Error");
                fail.setHeaderText("Failed to Make Transaction");
                fail.showAndWait();
                popup.close();
            } else
            purchase();
            popup.close();
        });

        Label header = new Label("Order Confirmation");
        header.setFont(Font.font("Arial Black",14));
        questionText.setFont(Font.font("Arial",14));
        header.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        header.setTextFill(Color.WHITE);

        BorderPane bp = new BorderPane(center);
        bp.setTop(header);
        bp.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        center.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        header.setAlignment(Pos.CENTER);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setMinWidth(300);
        Scene scene = new Scene(bp, 300, 200);
        popup.setResizable(false);
        popup.setScene(scene);
        popup.show();

    }

    private void purchase() {

        String transactionID = generateID();
        try {
            String sql = "SELECT * FROM Cart WHERE userID = ?";
            Connection conn = db.getConnection();

            String insertHeader = "INSERT INTO transaction_header (transactionID, userID) VALUES (?,?)";
            PreparedStatement insertStatement = conn.prepareStatement(insertHeader);
            insertStatement.setString(1, transactionID);
            insertStatement.setString(2, userSession.getUserID());
            insertStatement.executeUpdate();

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userSession.getUserID());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                int quantity = resultSet.getInt("quantity");


                String insertDetail = "INSERT INTO transaction_detail ( productID,quantity,transactionID) VALUES (?,?,?)";
                PreparedStatement insertDetailStatement = conn.prepareStatement(insertDetail);
                insertDetailStatement.setString(1, productID);
                insertDetailStatement.setInt(2, quantity);
                insertDetailStatement.setString(3, transactionID);
                insertDetailStatement.executeUpdate();
            }
            String deleteCart = "DELETE FROM cart WHERE userID = ?";
            PreparedStatement deleteCartStatement = conn.prepareStatement(deleteCart);
            deleteCartStatement.setString(1, userSession.getUserID());
            deleteCartStatement.executeUpdate();
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText("Successfully Purchased");
            success.showAndWait();
            CartView cartView = new CartView(primaryStage, userSession);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateID() {

        ArrayList<TransactionHeader> transactionHeaders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Transaction_header";
            Connection conn = db.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransactionHeader transactionHeader = new TransactionHeader(
                        resultSet.getString("transactionID"),
                        resultSet.getString("userID")
                );
                transactionHeaders.add(transactionHeader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int temp = Integer.MIN_VALUE;
        for (TransactionHeader th : transactionHeaders
        ) {
            int temp2 = Integer.parseInt(th.getTransactionIDtransactionID().substring(2));
            if (temp < temp2) {
                temp = temp2;

            }
        }
        return String.format("TR%03d", temp + 1);
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
        Text priceLabel = new Text("Price: Rp." + cart.getProductPrice());
        Text totalItemPrice = new Text("Total: Rp." + cart.getProductPrice() * cart.getQuantity());


        // quantity
        HBox qtc = new HBox();
        Label qtcLabel = new Label("Quantity: ");
        // Create a SpinnerValueFactory with custom min, max, and initial values
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, cart.getQuantity());

        // Create a Spinner with the custom value factory
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(valueFactory);
        spinner.valueProperty().addListener((obs, oldValue, newValue) ->
                totalItemPrice.setText("Total: Rp." + cart.getProductPrice() * Integer.valueOf(newValue)));

        qtc.getChildren().addAll(qtcLabel, spinner, totalItemPrice);
        FlowPane flowPane = new FlowPane();
        Button updateCartBtn = new Button("Update Cart");
        Button removeCartBtn = new Button("Remove from Cart");
        flowPane.getChildren().addAll(updateCartBtn, removeCartBtn);
        updateCartBtn.setOnAction(event -> updateCart(cart, spinner.getValue()));
        removeCartBtn.setOnAction(event -> updateCart(cart, -cart.getQuantity()));

        vBox.getChildren().addAll(nameLabel, descLabel, priceLabel, qtc, flowPane);


    }

    private void updateCart(CartList cart, Integer value) {
        Alert fail = new Alert(Alert.AlertType.ERROR);
        fail.setTitle("Error");
        fail.setHeaderText("Not a Valid Amount");
        String successMsg = "";
        try {
            String sql = "";
            Connection conn = db.getConnection();
            PreparedStatement preparedStatement;
            if (value != 0) {
                if (value + cart.getQuantity() == 0) {
                    sql = "DELETE FROM cart WHERE productID = ? AND userID = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, cart.getProductID());
                    preparedStatement.setString(2, userSession.getUserID());
                    successMsg = "Deleted from Cart";
                } else if (value + cart.getQuantity() > 0) {
                    sql = "UPDATE cart SET quantity = ? + quantity WHERE productID = ? AND userID = ?";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, value);
                    preparedStatement.setString(2, cart.getProductID());
                    preparedStatement.setString(3, userSession.getUserID());
                    successMsg = "Updated Cart";
                } else {

                    fail.showAndWait();
                    return;
                }
                preparedStatement.executeUpdate();
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(successMsg);
                success.showAndWait();
                CartView cartView = new CartView(primaryStage, userSession);
            } else {
                fail.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public List<CartList> populateListView() {
        List<CartList> cartItems = new ArrayList<>();
        try {

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
