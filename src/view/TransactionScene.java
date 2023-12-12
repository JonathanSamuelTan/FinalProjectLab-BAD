package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Database;
import model.TransactionDetail;
import model.TransactionHeader;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionScene {

    private Stage primaryStage;
    private User userSession;
    private Database db;

    public TransactionScene(Stage primaryStage, User userSession) {
        this.primaryStage = primaryStage;
        this.userSession = userSession;
        this.db = new Database();
    }

    public void show() {
        primaryStage.setTitle(userSession.getUserName() + "'s Purchase History");
        BorderPane borderPane = createTransactionLayout();

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createTransactionLayout() {
        BorderPane borderPane = new BorderPane();
        VBox topVBox = createTopSection();
        borderPane.setTop(topVBox);
        VBox centerVBox = createCenterSection();
        borderPane.setCenter(centerVBox);
        
        return borderPane;
    }

    private VBox createTopSection() {
        VBox topVBox = new VBox();
        Navbar navbar = new Navbar();
        topVBox.getChildren().add(navbar.userNavbar(primaryStage, userSession)); // Use userNavbar or adminNavbar based on user role

        Text usernameLabel = new Text(userSession.getUserName() + "'s Purchase History");
        usernameLabel.setFont(Font.font("Arial Black", 27));
        topVBox.getChildren().add(usernameLabel);
        topVBox.setMargin(usernameLabel, new Insets(10));

        return topVBox;
    }

    private VBox createCenterSection() {
        VBox centerVBox = new VBox();
        centerVBox.setPadding(new Insets(10));
        centerVBox.setSpacing(10);

        ListView<String> transactionListView = new ListView<>();
        ObservableList<String> transactionItems = FXCollections.observableArrayList(fetchTransactionIDs());
        transactionListView.setItems(transactionItems);

        double screenHeight = primaryStage.getHeight();
        transactionListView.setMaxHeight(screenHeight * 0.6);

        Label instructionLabel = new Label("Select a transaction to view details");

        transactionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String transactionID = newSelection;
                String transactionDetails = fetchTransactionDetails(transactionID);
                instructionLabel.setText(transactionDetails);
            }
        });

        HBox hbox = new HBox(transactionListView, instructionLabel);
        hbox.setSpacing(10);

        centerVBox.getChildren().add(hbox);

        return centerVBox;
    }


    

    private List<String> fetchTransactionIDs() {
        try{
        	Connection connection = db.getConnection();
            String sql = "SELECT transactionID FROM transaction_header WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userSession.getUserID());

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> transactionIDs = FXCollections.observableArrayList();

            while (resultSet.next()) {
                transactionIDs.add(resultSet.getString("transactionID"));
            }

            return transactionIDs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchTransactionDetails(String transactionID) {
        try{
        	Connection connection = db.getConnection();
            String sql = "SELECT * FROM transaction_header WHERE transactionID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transactionID);

            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder details = new StringBuilder();

            if (resultSet.next()) {
                TransactionHeader header = new TransactionHeader(
                        resultSet.getString("transactionID"),
                        resultSet.getString("userID")
                );

                details.append("Transaction ID: ").append(header.getTransactionIDtransactionID()).append("\n");
                details.append("Username: ").append(userSession.getUserName()).append("\n");
                details.append("Phone Number: ").append(userSession.getUserPhone()).append("\n");
                details.append("Address: ").append(userSession.getUserAddress()).append("\n");

                List<TransactionDetail> transactionDetailsList = fetchTransactionDetailsList(transactionID);
                details.append("Transaction Details:\n");

                for (TransactionDetail transactionDetail : transactionDetailsList) {
                    String productName = fetchProductName(transactionDetail.getProductID());
                    long productPrice = fetchProductPrice(transactionDetail.getProductID());
                    long totalProductPrice = transactionDetail.getQuantity() * productPrice;

                    details.append(transactionDetail.getQuantity()).append("x ")
                            .append(productName).append(" (")
                            .append(totalProductPrice).append(")\n");
                }

                details.append("Total Price: Rp.").append(fetchTotalPrice(transactionID)).append("\n");
            }

            return details.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());
            return "Error fetching transaction details.";
        }
    }


   
    private List<TransactionDetail> fetchTransactionDetailsList(String transactionID) {
        try{
        	Connection connection = db.getConnection();
            String sql = "SELECT * FROM transaction_detail WHERE transactionID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transactionID);

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<TransactionDetail> transactionDetailsList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                TransactionDetail transactionDetail = new TransactionDetail(
                        resultSet.getString("transactionID"),
                        resultSet.getString("productID"),
                        resultSet.getInt("quantity")
                );
                transactionDetailsList.add(transactionDetail);
            }

            return transactionDetailsList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchProductName(String productID) {
 
        try{
        	Connection connection = db.getConnection();
            String sql = "SELECT product_name FROM product WHERE productID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, productID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("product_name");
            } else {
                return "Product Not Found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching product name.";
        }
    }

    private long fetchProductPrice(String productID) {
        try {
        	Connection connection = db.getConnection();
            String sql = "SELECT product_price FROM product WHERE productID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, productID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("product_price");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long fetchTotalPrice(String transactionID) {
        try {
        	Connection connection = db.getConnection();
            String sql = "SELECT SUM(product.product_price * transaction_detail.quantity) AS total_price " +
                         "FROM transaction_detail " +
                         "JOIN product ON transaction_detail.productID = product.productID " +
                         "WHERE transactionID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, transactionID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("total_price");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
