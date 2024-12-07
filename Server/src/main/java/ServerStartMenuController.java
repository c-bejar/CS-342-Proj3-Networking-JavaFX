import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

//acts as a way to start server and input port number
public class ServerStartMenuController implements Initializable {
    @FXML VBox sceneReference;
    @FXML TextField portNumberTF;
    public static int portNumber = 0;
    // public Server serverConnection = ServerMainMenuController.serverConnection;
    
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        // if(serverConnection != null) {
        //     portNumberTF.setDisable(true);
        //     portNumberTF.setText(String.valueOf(serverConnection.server.portNum));
        // }
    }

    //when server admin presses the start server button in the server start menu
    @FXML
    public void handleStartServer() {
        try {
            portNumber = Integer.parseInt(portNumberTF.getText());
            System.out.println("Parsed: "+portNumber);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/server.fxml"));
            Scene serverScene = new Scene(loader.load());
            Stage stage = (Stage)(sceneReference.getScene().getWindow());
            stage.setScene(serverScene);

        } catch (IOException e) {
            e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }
}