import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

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
                    Object some = in.readObject();
                    dataRead = true;
                    if (some instanceof PokerInfo) {
                        data = (PokerInfo) some;
                        handleInput(data);
                        System.out.println("client: sent data:" + data.command);
                    }
                }
        } catch (Exception e) {
            System.out.println("exception in run");
            fail = true;
        }
    }
}