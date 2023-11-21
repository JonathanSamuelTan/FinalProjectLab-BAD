import javafx.application.Application;
import javafx.stage.Stage;
import view.TransactionHistory;

public class MainTest extends Application {

	public MainTest() {
	}

	public static void main(String[] args) {
		
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		TransactionHistory Th = new TransactionHistory();
		Th.start(primaryStage);
	}

}
