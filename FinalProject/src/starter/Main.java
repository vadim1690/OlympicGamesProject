package starter;

import controller.SystemController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.OlympicsManagementSystem;
import view.AbstractView;
import view.GUI;


public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		
		OlympicsManagementSystem managementSystem=new OlympicsManagementSystem();
		AbstractView theView = new GUI(primaryStage);
		SystemController controller = new SystemController(managementSystem, theView);
		
		
		
	}

}
