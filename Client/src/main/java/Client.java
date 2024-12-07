import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
    public Socket socketClient;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public PokerInfo info;
    public int port;
    private Consumer<Serializable> callback;

    Client(int port, Consumer<Serializable> call) {
        this.port = port;
        callback = call;
    }

    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", port);
            System.out.println("Client using port "+port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch(Exception e) {}

        while(true) {
            try {
                String message = in.readObject().toString();
                System.out.println("Received: "+message);
            } catch(Exception e) {}
        }
    }

    public void send(String data) {
        try {
            out.writeObject(data);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
