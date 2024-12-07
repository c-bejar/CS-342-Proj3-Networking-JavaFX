import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndScreenController {
    @FXML VBox root;

    Client clientSocket;

    public void handleRetry() {
        System.out.println("Attempting retry!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/GamePlayGUI.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add("/styles/style1.css");
            Stage stage = (Stage) (root.getScene().getWindow());

            GamePlayGUIController controller = loader.getController();
            controller.setClient(clientSocket);
            controller.setAnteLabel();
            controller.updateWinningsLabel();
            stage.setScene(gameScene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleExit() {
        System.exit(0);
    }

    public void setClient(Client clientSocket) {
        this.clientSocket = clientSocket;
    }
}