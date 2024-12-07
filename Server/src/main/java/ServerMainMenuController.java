import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

//second scene in the client UI. tracks the client data
public class ServerMainMenuController implements Initializable {

    @FXML private VBox individualClientDataVBox; @FXML private VBox clientListVBox;

    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    public void TESTBUTTON() {
        //REMOVE LATER: TESTING PURPOSES
        individualClientDataVBox.setVisible(true);
        individualClientDataVBox.setManaged(true);
        clientListVBox.setVisible(false);
        clientListVBox.setManaged(false);
    }

    @FXML
    public void handleBackToClientList() {
        //TODO: implement unfocusing the client
        individualClientDataVBox.setVisible(false);
        individualClientDataVBox.setManaged(false);
        clientListVBox.setVisible(true);
        clientListVBox.setManaged(true);
    }
    @FXML
    public void handleDeactivateServerButton() {
        //TODO: implement
    }
    @FXML
    public void handleShutdownServerAndMenu() {

    }
}
