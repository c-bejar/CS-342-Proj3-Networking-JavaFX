import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class GamePlayGUIController implements Initializable {
    @FXML VBox root;
    @FXML Label totalWinningsLabel;
    @FXML TextField anteInputTextField;
    @FXML Label playBetLabel;
    @FXML TextField playPlusInputTextField;
    @FXML VBox rightSide;
    @FXML HBox outerMostHBox;
    @FXML VBox dealButtonContainer;
    @FXML VBox playAndFoldHandButtonsContainer;
    @FXML VBox continueContainer;
    @FXML VBox gameDescriptionBox;
    @FXML ImageView pC1;
    @FXML ImageView pC2;
    @FXML ImageView pC3;
    @FXML ImageView dC1;
    @FXML ImageView dC2;
    @FXML ImageView dC3;

    // Client clientSocket;
    // int portNum = StartMenuController.portNum;

    //for changing style sheets
    boolean s1 = true;

    static boolean firstInstanceStarted = false;
    static boolean receivedConfirmation = false;

    char endType = 'L';

    Client clientSocket;
    Consumer<Serializable> callback;
    int portNum;
    int gameNum;

    ArrayList<ArrayList<String>> logs;
    ArrayList<String> currGame = new ArrayList<>();

    public void initialize(URL location, ResourceBundle resources) {
        currGame.add("Game"+gameNum+" ===============");
        // updateLog();
        //limits the size of the game log scroll view
        rightSide.maxWidthProperty().bind(outerMostHBox.widthProperty().multiply(0.5));
        //for limiting the ante bet to two digits
        anteInputTextField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}")) { // Allow up to 2 digits
                return change;
            }
            else if (newText == "0" && anteInputTextField.getText().isEmpty()) {
                return change;
            }
            return null; // Reject change if invalid
        }));
        //for limiting the play plus bet to two digits
        playPlusInputTextField.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}")) { // Allow up to 2 digits
                return change;
            }
            else if (newText == "0" && playPlusInputTextField.getText().isEmpty()) {
                return change;
            }
            return null; // Reject change if invalid
        }));
    }


    @FXML
    public void handleDealCards() {
        short ante = Short.parseShort(anteInputTextField.getText());
        short pp = Short.parseShort(playPlusInputTextField.getText());
        if(ante > 25 || ante < 5 ||
             pp > 25 || (pp != 0 && pp < 5)) {
            return;
        }
        //hiding the deal button
        dealButtonContainer.setVisible(false);
        dealButtonContainer.setManaged(false);
        playAndFoldHandButtonsContainer.setVisible(true);
        playAndFoldHandButtonsContainer.setManaged(true);
        anteInputTextField.setDisable(true);
        playPlusInputTextField.setDisable(true);

        System.out.println("Deal Cards Pressed");
        //clientSocket.send(new PokerInfo('D',Short.valueOf(anteInputTextField.getText()),Short.valueOf(playPlusInputTextField.getText())));

        if(clientSocket == null)
            System.out.println("Client is null for some reason!");

        clientSocket.send(new PokerInfo('D'));
        System.out.println("Sent command D");
        // Wait for clientSocket to have a hand
        while(true) {
            System.out.println("Client dealt hand: "+clientSocket.dealtHand);
            if(clientSocket.dealtHand) {
                clientSocket.dealtHand = false;
                break;
            }
        }
        System.out.println("Attempting to display");
        displayPlayerHand();
        clientSocket.info.setValues(ante, pp);
    }

    public void displayPlayerHand() {
        pC1.setImage(new Image(parseCardName(clientSocket.playersHand.get(0))));
        pC2.setImage(new Image(parseCardName(clientSocket.playersHand.get(1))));
        pC3.setImage(new Image(parseCardName(clientSocket.playersHand.get(2))));
        
    }

    public void displayDealerHand() {
        dC1.setImage(new Image(parseCardName(clientSocket.dealersHand.get(0))));
        dC2.setImage(new Image(parseCardName(clientSocket.dealersHand.get(1))));
        dC3.setImage(new Image(parseCardName(clientSocket.dealersHand.get(2))));
    }

    public String parseCardName(String str) {
        String suit = str.substring(0, 1);
        String value = str.substring(1, 2);
        switch(value) {
            case "T": value = "10"; break;
            case "J": value = "11"; break;
            case "Q": value = "12"; break;
            case "K": value = "13"; break;
            case "A": value = "14"; break;
            default: break;
        }
        return "/images/"+value+"of"+suit+".png";
    }

    public void setData(int port, Client clientSocket) {
        System.out.println("inside of setData:" + port);
        this.portNum = port;
        this.clientSocket = clientSocket;
    }

    // used outside of this file
    public void setArray(ArrayList<ArrayList<String>> logs) {
        this.logs = logs;
    }

    public void setGameNum(int gameNum) {
        this.gameNum = gameNum;
    }

    public void updateLog() {
        gameDescriptionBox.getChildren().clear();
        for(ArrayList<String> game : logs) {
            for(String event : game) {
                gameDescriptionBox.getChildren().add(new Label(event));
            }
        }
    }


    @FXML //TODO Implement the handle for fresh start through the menu
    public void handleFreshStartMenuItem() {
        clientSocket.info.winnings = 0;
    }

    @FXML //TODO Implement the handle for new look through the menu
    public void handleNewLookMenuItem() {
        outerMostHBox.getScene().getStylesheets().clear();
        if(s1) {
            outerMostHBox.getScene().getStylesheets().add(getClass().getResource("styles/style2.css").toExternalForm());
            s1 = false;
        }
        else {
            outerMostHBox.getScene().getStylesheets().add(getClass().getResource("styles/style1.css").toExternalForm());
            s1 = true;
        }
    }

    @FXML
    public void handleExitMenuItem() {
        System.exit(0);
    }

    @FXML
    public void handlePlayHand() {
        //TODO implement playing the hand
        System.out.println("Entered handlePlayHand()");
        displayDealerHand();

        ArrayList<String> d = clientSocket.dealersHand;
        ArrayList<String> p = clientSocket.playersHand;
        determinePPWinnings(p);


        clientSocket.send(new PokerInfo('X', p, d));

        while(true) {
            if(clientSocket.info.winResult > -1)
                break;
        }

        switch(clientSocket.info.winResult) {
            case 1: clientSocket.info.winnings -= clientSocket.info.anteBet;endType = 'L'; break;
            case 2: clientSocket.info.winnings += clientSocket.info.anteBet;endType = 'W'; break;
            default: endType = 'T';break;
        }
        clientSocket.info.winResult = -1;

        playAndFoldHandButtonsContainer.setVisible(false);
        playAndFoldHandButtonsContainer.setManaged(false);
        continueContainer.setVisible(true);
        continueContainer.setManaged(true);
        updateWinningsLabel();
    }

    @FXML
    public void handleFoldHand() {
        //TODO implement folding the hand
        System.out.println("Entered handleFoldHand()");
        clientSocket.info.winnings -= clientSocket.info.anteBet;
        clientSocket.info.winnings -= clientSocket.info.PPbet;
        // clientSocket.info.winnings -= clientSocket.info.PPbet;

        clientSocket.send(new PokerInfo('F'));
        endType = 'L';

        System.out.println("Client winnings: "+clientSocket.info.winnings);
        playAndFoldHandButtonsContainer.setVisible(false);
        playAndFoldHandButtonsContainer.setManaged(false);
        continueContainer.setVisible(true);
        continueContainer.setManaged(true);
        updateWinningsLabel();
    }

    public void handleContinue() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EndScreen.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add("/styles/endScreen.css");
            Stage stage = (Stage) (root.getScene().getWindow());

            EndScreenController controller = loader.getController();
            controller.setClient(clientSocket,endType);
            controller.setArray(logs);
            stage.setScene(gameScene);
        } catch(Exception e) {}
    }

    // used outside of this file
    public void setClient(Client clientSocket) {
        this.clientSocket = clientSocket;
    }

    // used outside of this file
    public void setAnteLabel() {
        if(clientSocket.started) {
            anteInputTextField.setDisable(true);
            anteInputTextField.setText(Short.toString(clientSocket.info.anteBet));
        }
    }

    // used outside of this file as well
    public void updateWinningsLabel() {
        totalWinningsLabel.setText(Long.toString(clientSocket.info.winnings));
    }

    public void determinePPWinnings(ArrayList<String> p) {
        short pp = clientSocket.info.PPbet;
        if(pp == 0)
            return;

        clientSocket.send(new PokerInfo('B', p, pp));

        // Wait for clientSocket to receive response
        while(true) {
            System.out.println("This helps for some reason");
            if(clientSocket.receivedPP) {
                clientSocket.receivedPP = false;
                break;
            }
        }
        System.out.println("Received response to pp!");
        if(clientSocket.info.ppWinnings == 0) {
            clientSocket.info.winnings -= clientSocket.info.PPbet;
            return;
        }
        clientSocket.info.winnings -= clientSocket.info.ppWinnings;
    }
}
