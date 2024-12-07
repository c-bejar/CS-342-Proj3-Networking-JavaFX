// import javafx.fxml.FXML;
// import javafx.event.ActionEvent;
// import javafx.application.Platform;
// import javafx.scene.layout.VBox;
// import javafx.scene.control.Label;
// import javafx.scene.control.Button;
// import javafx.scene.control.ListView;

// public class ServerController {
//     @FXML Label clientsLabel;
//     @FXML Button activateServer;
//     @FXML ListView clientJoinLog;
//     Server serverConnection;

//     public void activateServerButton(ActionEvent e) {
//         activateServer.setDisable(true);
//         System.out.println("Pressed!");

//         serverConnection = new Server(data -> {
//             Platform.runLater(() -> {
//                 System.out.println("Server Received: "+data.toString());
//                 clientJoinLog.getChildren().add(new Label(data.toString()));
//                 clientsLabel.setText("Clients Connected: "+serverConnection.count);
//             });
//         });
//     }
// }
