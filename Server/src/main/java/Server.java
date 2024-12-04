import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {
    int count;
    ArrayList<ClientThread> clients;
    TheServer server;
    private Consumer<Serializable> callback;

    Server(Consumer<Serializable> call) {
        callback = call;
        count = 1;
        clients = new ArrayList<>();
        server = new TheServer();
        server.start();
    }

    class TheServer extends Thread{
        @Override
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(5555)) {
                System.out.println("Server is waiting for client!");

                while(true) {
                    ClientThread c = new ClientThread(mySocket.accept(), count);
                    callback.accept("Client has connected to server:\n\tClient #"+count);
                    clients.add(c);
                    
                    count++;
                }
            } catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }
    }

    class ClientThread extends Thread{
        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count) {
            this.connection = s;
            this.count = count;
        }

        public void updateClients(PokerInfo data) {
            System.out.println("updateClients() called");
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) {
                try {
                    String data = in.readObject().toString();
                    System.out.println("Server received: "+data);
                    out.writeObject(data.toUpperCase());
                } catch(Exception e) {
                    System.out.println("Something went wrong");
                    break;
                }
            }
        }
    }
}
