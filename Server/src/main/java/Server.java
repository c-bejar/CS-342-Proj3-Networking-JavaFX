import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int count = 0;
    int topClientID = 0;
    int numClients = 0;
    ArrayList<ClientThread> clients = new ArrayList<>();
    ObservableList<Label> displayedClients = FXCollections.observableArrayList();//array list for holding displayed client content
    TheServer server;
    boolean acceptingClients = false;
    private Consumer<Serializable> callback;

    Server(int port, Consumer<Serializable> call) {//creates server thread and starts the server operation
        callback = call;
        server = new TheServer(port);
        server.start();
    }

    public void shutdown() {//closes all threads
        try {
            acceptingClients = false;
            for(ClientThread client: clients) {
                client.connection.close();
                client.in.close();
                client.out.close();
                client.interrupt();
            }
            topClientID = 0;
        } catch(Exception e) {}
    }

    public class TheServer extends Thread {//thread class of the server
        int portNum = 0;

        TheServer(int port) {
            portNum = port;
        }

        public void run() {
            try(ServerSocket mySocket = new ServerSocket(portNum);) {
                acceptingClients = true;
                System.out.println("Server using port "+portNum);
                System.out.println("Server is waiting for a client!");

                while(true) {//keeps creating client threads when clients join
                    ClientThread c = new ClientThread(mySocket.accept(), count, topClientID);
                    if(!acceptingClients) {//stops accepting clients if the server is off
                        shutdown();
                        continue;
                    }
                    count++;
                    numClients++;
                    topClientID++;
                    callback.accept("Client "+c.clientID+" joined the Server"); //TODO: modify to make non overlapping IDs
                    clients.add(c);
                    Platform.runLater(() -> {
                        c.displayedLabel = new Label("client: "+c.clientID);

                        //array list for holding displayed client content
                        c.displayedLabel.setUserData(c.displayGames);
                        displayedClients.add(c.displayedLabel);
                    });
                    c.start();

                }
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Server socket did not launch");
            }
        }

    }

    public class ClientThread extends Thread {//client thread

        //data for the individual client
        Player player = new Player();
        Dealer dealer = new Dealer();

        Socket connection;
        int clientCount;
        int clientID;
        int gameNum = 0;
        ObjectOutputStream out;
        ObjectInputStream in;
        Label displayedLabel;//holding client info
        ObservableList<Label> displayGames = FXCollections.observableArrayList();//holding game ui stuff

        ClientThread(Socket s, int clientCount, int topClientID) {
            this.connection = s;
            this.clientCount = clientCount;
            clientID = topClientID;
        }

        public void deal() {//TODO
            System.out.println("command: Deal");
            gameNum += 1;
            dealer.dealersHand = dealer.dealHand();
            player.hand = dealer.dealHand();
            System.out.println("dealt new hand");
            // Since the PokerInfos can't differ, we'll just parse
            // strings to get cards
            // Format: [Suit][Card]...#[Suit][Card]...
            // Ex: "D8 SA CT # H2 DJ HK" just imagine the spaces aren't there
            // Equates to: Dealer: 8 of Diamonds, Ace of Spaces, 10 of Clubs
            //             Player: 2 of Hearts, Jack of Diamonds, King of Hearts
            // 2-9 = 2...9     T=10, J=Jack, Q=Queen, K=King, A=Ace
            String respectiveHands = parseCards(dealer.dealersHand, player.hand);
            System.out.println("Created String for hands: "+respectiveHands);
            try {
                out.writeObject(respectiveHands);
            } catch(Exception e) {}
        }
        public void freshStart() {//TODO
            System.out.println("command: freshStart");
        }
        public void endGame() {//TODO
            System.out.println("command: endGame");
        }
        public void fold() {//TODO
            System.out.println("start in fold()");
            Platform.runLater(() -> {
                System.out.println("in fold run later");
                Label temp = new Label("Game: " + gameNum);

                String tempString = "Ante Bet: 5\n" +
                        "PairPlus Bet: 20\n" +
                        "Action: Fold\n" +
                        "OutCome: Lost\n" +
                        "Total Winnings Change: -25\n" +
                        "New Total Winnings: 40";
                temp.setUserData(tempString);

                displayGames.add(temp);
            });

            System.out.println("end in fold()");
        }
        public void play() {//TODO
            System.out.println("command: play");
        }
        public void ppWinnings() {//TODO
            System.out.println("command: ppWinnings");
            
        }

        void parseInputCommand(PokerInfo data) {
            System.out.println("Command sent to Server: "+data.command);
            switch(data.command) {
                case 'D':
                    deal();
                    break;
                case 'R':
                    freshStart();
                    break;
                case 'E':
                    endGame();
                    break;
                case 'F':
                    System.out.println("in F case");
                    fold();
                    break;
                case 'P':
                    play();
                    break;
                case 'B':
                    ppWinnings();
                    break;
                default: System.out.println("invalid command");
            }
        }

        public void run() {
            try {
                //sets streams
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) { //keeps looping to read in data from the client
                try {
                    PokerInfo data = null;
                    Object some = in.readObject();

                    if(some instanceof PokerInfo) {
                        data = (PokerInfo)some;
                    } else {
                        System.out.println("Unrecognized format");
                    }
                    callback.accept("client "+clientID+" sent data: "+data.command);
                    parseInputCommand(data);

                } catch(Exception e) {
                    callback.accept("Client "+clientID+" left the Server");
                    synchronized (this) {
                        // Critical section
                        Platform.runLater(() -> {
                            displayedClients.remove(displayedLabel);
                        });
                        clients.remove(this);
                        numClients--;
                    }
                    break;
                }
            }
        }
        private String parseCards(ArrayList<Card> playerHand, ArrayList<Card> dealerHand) {
            String parseHand = "";
            for(Card c : dealerHand) {
                String val = "?";
                parseHand += c.suit;
                switch(c.value) {
                    case 10: val = "T"; break;
                    case 11: val = "J"; break;
                    case 12: val = "Q"; break;
                    case 13: val = "K"; break;
                    case 14: val = "A"; break;
                    default: val = String.valueOf(c.value);
                }
                parseHand += val;
            }
            parseHand += "#";
            for(Card c : playerHand) {
                String val = "?";
                parseHand += c.suit;
                switch(c.value) {
                    case 10: val = "T"; break;
                    case 11: val = "J"; break;
                    case 12: val = "Q"; break;
                    case 13: val = "K"; break;
                    case 14: val = "A"; break;
                    default: val = String.valueOf(c.value);
                }
                parseHand += val;
            }
            return parseHand;
        }
    }
}