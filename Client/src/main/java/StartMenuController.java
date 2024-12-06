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

public class StartMenuController implements Initializable {
    @FXML
    Label reference;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void handleSwitchToGamePlay() {
        //TODO need to handle the server stuff with this as well
        try {

            //get the ip address and port number
            //connect player client to server
            

           

            //check if player is connected to the server
            
            //need try and catch for loading the scene for the client.
            //loading the gameplay scene for the client after they have connected to the server.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/GamePlayGUI.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add(getClass().getResource("styles/style1.css").toExternalForm());
            Stage stage = (Stage)(reference.getScene().getWindow());
            stage.setScene(gameScene);

            

            

        } catch (IOException e) { //catch for server connection
            e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }

    }

    //Exits the game from the start button
    @FXML
    public void handleExitGame() {
        System.exit(0);

    }

}
