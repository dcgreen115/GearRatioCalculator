package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
	@Override
	public void start(Stage primaryStage) {
	    @SuppressWarnings("unused")
        GUIWindow window = new GUIWindow(primaryStage);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
