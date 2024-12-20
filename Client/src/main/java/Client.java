import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Client extends Thread{
    ArrayList<String> playersHand;
    ArrayList<String> dealersHand;
    PokerInfo info = new PokerInfo('X');
    boolean started = false;
    boolean dealtHand = false;
    boolean receivedPP = false;
    public Socket socketClient;
    public int port;
    private Consumer<Serializable> callback;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean dataRead = false;
    boolean fail = false;
    boolean success = false;

    Client(int port, Consumer<Serializable> call) {
        System.out.println("inside client constructor: port" + port);
        this.port = port;
        callback = call;
    }

    public void send(PokerInfo data) {
        try {
            System.out.println("inside send: "+data.command);
            out.writeObject(data);
        } catch(Exception e) {
            //e.printStackTrace();
            System.console().printf("Error: %s%n", e.getMessage());
        }
    }

    private void receiveHands(String data) {
        ArrayList<String> dealerHand = new ArrayList<>();
        ArrayList<String> playerHand = new ArrayList<>();

        String[] parts = data.split("#");
        for(int i = 0; i < parts.length; i++) {
            String card1 = parts[i].substring(0, 2);
            String card2 = parts[i].substring(2, 4);
            String card3 = parts[i].substring(4, 6);
            if(i == 0) {
                dealerHand.add(card1);
                dealerHand.add(card2);
                dealerHand.add(card3);
            } else {
                playerHand.add(card1);
                playerHand.add(card2);
                playerHand.add(card3);
            }
        }
        System.out.println("Before dealing dealer: "+dealersHand);
        System.out.println("Before dealing player: "+playersHand);
        info.playerHand = playersHand;
        info.dealerHand = dealersHand;
        dealersHand = dealerHand;
        playersHand = playerHand;
        dealtHand = true;
        started = true;
        System.out.println("Finished creating arrays for hands");
    }

    public void determineWinner(String data) {
        System.out.println("in determineWinner()");
        info.winResult = Integer.parseInt(data);
        System.out.println("out of determineWinner()");
    }

    // Call different functions depending on what command is sent to Client
    public void receiveString(Object in) {
        try {
            String data = in.toString();
            System.out.println("data size: "+data.length());
            if(data.length() == 13) {
                receiveHands(data);
            } else if(data.length() == 1) {
                determineWinner(data);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveInt(Object in) {
        int data = (int)in;
        info.ppWinnings = data;
        receivedPP = true;
        System.out.println("PPWinnings: "+data);
    }

    public void handleInput(PokerInfo data) {
        dataRead = false;
        System.out.println("handlinging input command: " + data.command);
    }

    public void run() {
        try {
            System.out.println("inside client port:" + port);
            socketClient = new Socket("127.0.0.1", port);
            //success = true;
            System.out.println("Client using port " + port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            System.out.println("after streams");
            socketClient.setTcpNoDelay(true);

//                out.writeObject(new PokerInfo('S'));
//                fail = false;
//                System.out.println("Wrote command s successfully");

            while(true) {
                PokerInfo data = null;
                System.out.println("Starting to read something new!");
                Object some = in.readObject();
                dataRead = true;
                if (some instanceof PokerInfo) {
                    data = (PokerInfo) some;
                    handleInput(data);
                    System.out.println("client: sent data:" + data.command);
                } else if(some instanceof String) {
                    System.out.println("String sent!");
                    receiveString(some);
                } else if(some instanceof Integer) {
                    System.out.println("Integer sent!");
                    receiveInt(some);
                }
            }
        } catch (Exception e) {
            System.out.println("exception in run");
            fail = true;
        }
    }
}