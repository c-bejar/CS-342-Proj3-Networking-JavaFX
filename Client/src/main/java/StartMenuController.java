import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StartMenuController {
    @FXML Label reference;

    public void handleSwitchToGamePlay() {
        //TODO need to handle the server stuff with this as well
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/GamePlayGUI.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add(getClass().getResource("styles/style1.css").toExternalForm());
            Stage stage = (Stage)(reference.getScene().getWindow());
            stage.setScene(gameScene);

        } catch (IOException e) {
            e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }

    }

    //Exits the game from the start button
    public void handleExitGame() {
        System.exit(0);
    }

}
