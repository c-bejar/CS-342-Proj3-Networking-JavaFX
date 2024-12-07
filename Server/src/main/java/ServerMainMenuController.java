import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

//second scene in the client UI. tracks the client data
public class ServerMainMenuController implements Initializable {

    @FXML private VBox individualClientDataVBox;
    @FXML private Button onOffServer;
    @FXML private HBox sceneReference;
    @FXML private ListView clientList;
    @FXML private ListView clientJoinLog;
    @FXML private Label clientsLabel;
    public static Server serverConnection;
    public static int portNum = ServerStartMenuController.portNumber;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Intitialized port: "+portNum);
        serverConnection = new Server(portNum, data -> {
            Platform.runLater(() -> {
                System.out.println("Server Received: "+data.toString());
                clientJoinLog.getItems().add(new Label(data.toString()));
                clientsLabel.setText("Clients Connected: "+serverConnection.count);
            });
        });
    }


    @FXML
    public void TESTBUTTON() {
        //REMOVE LATER: TESTING PURPOSES
        individualClientDataVBox.setVisible(true);
        individualClientDataVBox.setManaged(true);
        clientList.setVisible(false);
        clientList.setManaged(false);
    }

    @FXML
    public void handleBackToClientList() {
        //TODO: implement unfocusing the client
        individualClientDataVBox.setVisible(false);
        individualClientDataVBox.setManaged(false);
        clientList.setVisible(true);
        clientList.setManaged(true);
    }
    @FXML
    public void handleDeactivateServerButton() {
        serverConnection.acceptingClients = !serverConnection.acceptingClients;
        if(onOffServer.getText().equals("Deactivate Server")) {
            onOffServer.setText("Activate Server");
        } else {
            onOffServer.setText("Deactivate Server");
        }

    }
    @FXML
    public void handleShutdownServerAndMenu() {
        // try {
        //     for(int i = 0; i < serverConnection.clients.size(); i++) {
        //         serverConnection.clients.get(i).out.writeObject("Test");
        //     }
        // } catch(Exception e) {

        // }
        serverConnection.shutdown();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/start.fxml"));
            Scene startScene = new Scene(loader.load());
            Stage stage = (Stage)(sceneReference.getScene().getWindow());
            stage.setScene(startScene);
        } catch(IOException e) {
            e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }
    }
}