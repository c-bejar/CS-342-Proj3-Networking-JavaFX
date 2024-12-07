import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class GamePlayGUIController implements Initializable {
    @FXML Label totalWinningsLabel;
    @FXML TextField anteInputTextField;
    @FXML Label playBetLabel;
    @FXML TextField playPlusInputTextField;
    @FXML VBox rightSide;
    @FXML HBox outerMostHBox;
    @FXML VBox dealButtonContainer;
    @FXML VBox playAndFoldHandButtonsContainer;
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

    Client clientSocket;
    Consumer<Serializable> callback;
    int portNum;

    public void initialize(URL location, ResourceBundle resources) {
        //TODO add server stuff
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
        // System.out.println("clientSocket: " + clientSocket.port);
    }


    @FXML //TODO Implement the handle for pushed the deal card button
    public void handleDealCards() {
        //hiding the deal button
        dealButtonContainer.setVisible(false);
        dealButtonContainer.setManaged(false);
        playAndFoldHandButtonsContainer.setVisible(true);
        playAndFoldHandButtonsContainer.setManaged(true);
        anteInputTextField.setDisable(true);
        playPlusInputTextField.setDisable(true);

        System.out.println("Deal Cards Pressed");
        //clientSocket.send(new PokerInfo('D',Short.valueOf(anteInputTextField.getText()),Short.valueOf(playPlusInputTextField.getText())));

        clientSocket.send(new PokerInfo('D'));
        // Wait for clientSocket to have a hand
        while(true) {
            System.out.println("Client Hand Dealt: "+clientSocket.dealtHand);
            if(clientSocket.dealtHand) {
                clientSocket.dealtHand = false;
                break;
            }
        }
        displayHands();
    }

    public void displayHands() {
        // dC1.setImage(new Image(parseCardName(clientSocket.dealersHand.get(0))));
        // dC2.setImage(new Image(parseCardName(clientSocket.dealersHand.get(1))));
        // dC3.setImage(new Image(parseCardName(clientSocket.dealersHand.get(2))));
        pC1.setImage(new Image(parseCardName(clientSocket.playersHand.get(0))));
        pC2.setImage(new Image(parseCardName(clientSocket.playersHand.get(1))));
        pC3.setImage(new Image(parseCardName(clientSocket.playersHand.get(2))));

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


    @FXML //TODO Implement the handle for fresh start through the menu
    public void handleFreshStartMenuItem() {

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
        //TODO handling the exit with the server
        System.exit(0);
    }

    @FXML
    public void handlePlayHand() {
        //TODO implement playing the hand
        pC1.setImage(new Image("/images/4ofH.png"));
    }

    @FXML
    public void handleFoldHand() {
        System.out.println("Fold hand Pressed");
        //TODO implement folding the hand

        clientSocket.send(new PokerInfo('F'));
        // Wait for clientSocket to have a hand
//        while(true) {
//            System.out.println("Client Hand Dealt: "+clientSocket.dealtHand);
//            if(clientSocket.dealtHand) {
//                clientSocket.dealtHand = false;
//                break;
//            }
        //}
    }


}