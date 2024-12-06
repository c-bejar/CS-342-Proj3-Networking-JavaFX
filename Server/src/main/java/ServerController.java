import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.control.Button;

public class ServerController {
    @FXML Button activateServer;
    Server serverConnection;

    public void activateServerButton(ActionEvent e) {
        activateServer.setDisable(true);
        // e.getSource().setDisable(true);
        System.out.println("Pressed!");

        serverConnection = new Server(data -> {
            Platform.runLater(() -> {
                //TODO do something with data
                System.out.println("Server Received: "+data.toString());
            });
        });
    }
}
