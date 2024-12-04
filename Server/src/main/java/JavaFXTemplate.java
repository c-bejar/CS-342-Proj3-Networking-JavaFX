import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class JavaFXTemplate extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("3 Card Poker Server Panel");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ServerStartMenu.fxml"));
		VBox root = loader.load();
		Scene scene = new Scene(root,400,400);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
