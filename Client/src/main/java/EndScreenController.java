import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndScreenController{
    @FXML VBox root;
    @FXML Label gameStatus;
    @FXML Button nextButton;
    @FXML Button exitButton;

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
    public void setClient(Client clientSocket, char Type) {
        this.clientSocket = clientSocket;
        if(status == 'W') {
            root.getStyleClass().remove("basic_background");
            root.getStyleClass().add("win_background");
            nextButton.getStyleClass().add("win_button");
            exitButton.getStyleClass().add("win_button");
            gameStatus.getStyleClass().add("win_label");
            gameStatus.setText("You Win!");
        }
        else if(status == 'L') {
            root.getStyleClass().add("lose_background");
            nextButton.getStyleClass().add("lose_button");
            exitButton.getStyleClass().add("lose_button");
            gameStatus.getStyleClass().add("lose_label");
            gameStatus.setText("You Lose!");
        }
        else{
            gameStatus.setText("You Draw!");
            root.getStyleClass().add("basic_background");
            nextButton.getStyleClass().add("basic_button");
            exitButton.getStyleClass().add("basic_button");
            gameStatus.getStyleClass().add("basic_label");
        }
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
