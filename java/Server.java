
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.lang.String;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int count = 0;
    int numClients = 0;
    int topClientID  = 0;
    ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;
    boolean acceptingClients = false;
    private Consumer<Serializable> callback;
    ObservableList<Label> displayedClients = FXCollections.observableArrayList();//array list for holding displayed client content

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
                topClientID = 0;
            }
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
                    ClientThread c = new ClientThread(mySocket.accept(), count,topClientID);
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
        ObjectOutputStream out;
        ObjectInputStream in;
        int clientID;
        int gameNum = 0;

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
                String tempString = "Ante Bet: " + player.anteBet +
                        "\nPairPlus Bet: 20" + player.pairPlusBet +
                        "\nAction: Fold" +
                        "\nOutCome: Lost" +
                        "\nTotal Winnings Change:" + (-player.anteBet - player.pairPlusBet) +
                        "\nNew Total Winnings:" + player.totalWinnings;
                System.out.println("before set user data");
                temp.setUserData(tempString);
                System.out.println("before add to displayGames");
                displayGames.add(temp);
                System.out.println("after add to displayGames");
            });
            System.out.println("end in fold()");
        }
        public void play(PokerInfo data) {
            System.out.println("command: play");
            ArrayList<String> dHand = data.dealerHand;
            ArrayList<String> pHand = data.playerHand;

            ArrayList<Card> dealHand = new ArrayList<>();
            ArrayList<Card> playHand = new ArrayList<>();

            for(int i = 0; i < 3; i++) {
                String cardD = dHand.get(i);
                String cardP = pHand.get(i);
                char sD = cardD.charAt(0);
                char sP = cardP.charAt(0);
                int valD = getVal(cardD.charAt(1));
                int valP = getVal(cardP.charAt(1));
                dealHand.add(new Card(sD, valD));
                playHand.add(new Card(sP, valP));
            }
            short pp = data.PPbet;
            int result = ThreeCardLogic.compareHands(dealHand, playHand);
            int betWinnings = ThreeCardLogic.evalPPWinnings(playHand, pp);
            Platform.runLater(() -> {
                System.out.println("in play run later");
                Label temp = new Label("Game: " + gameNum);
                String tempString = "Ante Bet: " + player.anteBet +
                        "\nPairPlus Bet: 20" + player.pairPlusBet  + "\nAction: Play" + "\nOutCome: ";
                if(result == 2) {
                    tempString += ("Lost\nTotal Winnings Change:" + (-data.anteBet + data.PPbet));
                }
                else if(result == 1) {
                    tempString += ("Won" + "\nTotal Winnings Change:" + (pp - data.anteBet));
                }
                else {
                    tempString += "Tied" + "\nTotal Winnings Change:" + pp + "\nNew Total Winnings:" + (data.anteBet + pp);
                }
                tempString += "\nNew Total Winnings:" + player.totalWinnings;
                System.out.println("before set user data");
                temp.setUserData(tempString);
                System.out.println("before add to displayGames");
                displayGames.add(temp);
                System.out.println("after add to displayGames");
                });


            try {
                out.writeObject(Integer.toString(result));
            } catch(Exception e) {}
        }

        public void ppWinnings(PokerInfo data) {//TODO
            System.out.println("command: ppWinnings");
            short pp = data.PPbet;
            ArrayList<String> pHand = data.playerHand;
            ArrayList<Card> playHand = new ArrayList<>();

            for(int i = 0; i < 3; i++) {
                String card = pHand.get(i);
                char s = card.charAt(0);
                int val = getVal(card.charAt(1));
                playHand.add(new Card(s, val));
            }
            int betWinnings = ThreeCardLogic.evalPPWinnings(playHand, pp);
            try {
                out.writeObject(betWinnings);
            } catch(Exception e) {}
        }

        public int getVal(char str) {
            switch(str) {
                case 'T': return 10;
                case 'J': return 11;
                case 'Q': return 12;
                case 'K': return 13;
                case 'A': return 14;
                default: return (int)(str - '0');
            }
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
                    fold();
                    break;
                case 'X':
                    play(data);
                    break;
                case 'B':
                    ppWinnings(data);
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
