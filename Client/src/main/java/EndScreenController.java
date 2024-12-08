import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndScreenController {
    @FXML VBox root;

    Client clientSocket;
    int gameNum;
    ArrayList<ArrayList<String>> logs;

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
            controller.setGameNum(++gameNum);
            controller.setArray(logs);
            stage.setScene(gameScene);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleExit() {
        System.exit(0);
    }

    // used outside of this file
    public void setClient(Client clientSocket) {
        this.clientSocket = clientSocket;
    }

    // used outside of this file
    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    // used outside of this file
    public void setArray(ArrayList<ArrayList<String>> logs) {
        this.logs = logs;
    }
}
