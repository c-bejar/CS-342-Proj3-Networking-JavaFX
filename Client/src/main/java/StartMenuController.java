import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class StartMenuController implements Initializable{
    @FXML Label reference;
    @FXML TextField portInput;
    Client clientSocket;
    static Consumer<Serializable> callback;
    int portNum = 0;

    public void handleSwitchToGamePlay() {
        //TODO need to handle the server stuff with this as well
        try {
            //get input
            portNum = Integer.parseInt(portInput.getText());

            //start client
            clientSocket = new Client(portNum,callback);
            clientSocket.start();

            //while(!clientSocket.fail) {
            //if (clientSocket.success) {
            System.out.println("switchScene");
            //loading the other scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/GamePlayGUI.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add("/styles/style1.css");
            Stage stage = (Stage) (reference.getScene().getWindow());

                    //reference to controller for sending portnum and client
                    GamePlayGUIController controller = loader.getController();
                    controller.setData(portNum,clientSocket);
                    controller.setAnteLabel();
                    stage.setScene(gameScene);
                    //break;
               // }
            //}
//            else {
//                print();
//            }

        } catch (IOException e) {
            e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }


        //TODO stop execution if cant connect
    }

    //Exits the game from the start button
    public void handleExitGame() {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //for controlling the port number input
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