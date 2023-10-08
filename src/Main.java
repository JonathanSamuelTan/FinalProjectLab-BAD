import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of the LoginView and pass a new LoginController to it
        LoginView loginView = new LoginView(primaryStage);

        // Start the login view
        loginView.show();
    }
}
