import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class StartMenuController implements Initializable{
    @FXML Label reference;
    @FXML TextField portInput;
    static Client clientSocket;
    static int portNum = 0;

    public void handleSwitchToGamePlay() {
        //TODO need to handle the server stuff with this as well
        try {
            while(true) {
                System.out.println("New Loop");
                portNum = Integer.parseInt(portInput.getText());
                clientSocket = new Client(portNum, data -> {
                    Platform.runLater(() -> {
                        //TODO maybe something with data
                    });
                });
        
                clientSocket.start();
                
                System.out.println("Client connected? "+clientSocket.isConnected);
                if(!clientSocket.isConnected) {
                    break;
                }
            }
            System.out.println("Exited loop!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/GamePlayGUI.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add("/styles/style1.css");
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //for controlling the post number input
        portInput.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}")) { // Allow up to 4 digits
                return change;
            }
            else if (newText == "0" && portInput.getText().isEmpty()) {
                return change;
            }
            return null; // Reject change if invalid
        }));

    }

}