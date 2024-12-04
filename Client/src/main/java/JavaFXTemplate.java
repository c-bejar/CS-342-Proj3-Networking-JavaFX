import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class JavaFXTemplate extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Three Card Poker");
		Parent root = FXMLLoader.load(getClass().getResource("/FXML/StartMenu.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add("/styles/startStyle.css");

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
