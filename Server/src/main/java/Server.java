import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int count = 0;
    ArrayList<ClientThread> clients = new ArrayList<>();
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
                    ClientThread c = new ClientThread(mySocket.accept(), count);
                    if(!acceptingClients) {//stops accepting clients if the server is off
                        shutdown();
                        continue;
                    }
                    count++;
                    callback.accept("Client "+count+" joined the Server"); //TODO: modify to make non overlapping IDs
                    clients.add(c);
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

        ClientThread(Socket s, int clientCount) {
            this.connection = s;
            this.clientCount = clientCount;
        }

        public void deal() {//TODO
            System.out.println("command: Deal");
            dealer.dealersHand = dealer.dealHand();
            player.hand = dealer.dealHand();
            // Since the PokerInfos can't differ, we'll just parse
            // strings to get cards
            // Format: [Suit][Card]...#[Suit][Card]...
            // Ex: "D8 SA CT # H2 DJ HK" just imagine the spaces aren't there
            // Equates to: Dealer: 8 of Diamonds, Ace of Spaces, 10 of Clubs
            //             Player: 2 of Hearts, Jack of Diamonds, King of Hearts
            // 2-9 = 2...9     T=10, J=Jack, Q=Queen, K=King, A=Ace
            String respectiveHands = parseCards(dealer.dealersHand, player.hand);
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
            System.out.println("command: Fold");
        }
        public void play() {//TODO
            System.out.println("command: play");
        }

        void parseInputCommand(PokerInfo data) {
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
                case 'P':
                    play();
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
                    }
                    callback.accept("client "+count+" sent data: "+data.command);
                    parseInputCommand(data);

                } catch(Exception e) {
                    callback.accept("Client "+count+" left the Server");
                    clients.remove(this);
                    // count--; Should always rise to correctly distinguish clients
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