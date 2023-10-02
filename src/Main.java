import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;
import controller.LoginController;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FreshFind Inventory Management System");
        
        // Create an instance of the LoginView and pass a new LoginController to it
        LoginView loginView = new LoginView(primaryStage, new LoginController());

        // Start the login view
        loginView.show();
    }
}
