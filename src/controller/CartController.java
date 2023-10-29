package controller;

import javafx.stage.Stage;
import model.Database;
import model.User;
import view.CartView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartController {

    private CartView cartView;
    private User UserSession;
    private Stage stage;
    private Database db;

    public CartController( Stage stage,User userSession) {
        this.cartView = new CartView(stage, userSession);
        UserSession = userSession;
        db = new Database();
        this.stage = stage;
        //populateList();
    }

//    private void populateList() {
//
//        db.connect();
//        Connection connection = db.getConnection();
//        ArrayList<User> users = new ArrayList<>();
//
//        if (connection != null) {
//            try {
//                // Prepare a SQL query to check if the email and password match
//                String query = "SELECT quantity, product.product_name,product.product_price FROM cart JOIN product ON cart.productID = product.productID";
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                while (resultSet.next()) {
//                    users.add(new User(
//                            resultSet.getString("userID"),
//                            resultSet.getString("username"),
//                            resultSet.getString("password"),
//                            resultSet.getString("role"),
//                            resultSet.getString("address"),
//                            resultSet.getString("phone_num"),
//                            resultSet.getString("gender")
//                    ));
//                }
//                preparedStatement.close();
//                resultSet.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } finally {
//                db.closeConnection();
//            }
//        }
//
//    }


}
