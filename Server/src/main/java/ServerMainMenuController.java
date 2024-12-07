import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

//second scene in the client UI. tracks the client data
public class ServerMainMenuController implements Initializable {

    @FXML private VBox individualClientDataVBox;
    @FXML private ListView clientList;
    @FXML private ListView clientJoinLog;
    @FXML private Label clientsLabel;
    public Server serverConnection;
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
        //TODO: implement
    }
    @FXML
    public void handleShutdownServerAndMenu() {

    }
}