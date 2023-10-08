package controller;

import javafx.stage.Stage;
import model.User;
import view.CartView;

public class CartController {

    private CartView cartView;
    private User UserSession;
    private Stage stage;

    public CartController(User userSession, Stage stage) {
        this.cartView = new CartView(stage, userSession);
        UserSession = userSession;
        this.stage = stage;
    }


}
