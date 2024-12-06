import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client extends Thread{
    public Socket socketClient;
    public ObjectOutputStream out;
    public ObjectInputStream in;
    private PokerInfo info;
    // private Consumer<Serializable> callback;

    Client(String s) {
        super(s);
    }

    @Override
    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch(Exception e) {}

        while(true) {
            try {
                String message = in.readObject().toString();
                callback.accept(message);
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