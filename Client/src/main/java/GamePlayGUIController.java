import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

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

    //for changing style sheets
    boolean s1 = true;

    public void initialize(URL location, ResourceBundle resources) {
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


    @FXML //TODO Implement the handle for pushed the deal card button
    public void handleDealCards() {
        //hiding the deal button
        dealButtonContainer.setVisible(false);
        dealButtonContainer.setManaged(false);
        playAndFoldHandButtonsContainer.setVisible(true);
        playAndFoldHandButtonsContainer.setManaged(true);
        anteInputTextField.setDisable(true);
        playPlusInputTextField.setDisable(true);
        //


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
        //TODO implement folding the hand
    }


}
