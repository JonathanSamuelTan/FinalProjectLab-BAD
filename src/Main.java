import javafx.application.Application;
import javafx.stage.Stage;
import model.Database;
import model.User;
import view.LoginView;
import view.Navbar;
import view.HpUserView;
import view.EditProductScene;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of the LoginView and pass a new LoginController to it
        //new LoginView(primaryStage).show();
        //new HpUserView(primaryStage,null).show();
        // Start the login view
    	EditProductScene test=new EditProductScene(primaryStage,new User("1", "admin", "admin123", "admin", "industri","0389921", "Male"));
        test.show();
    }
}
