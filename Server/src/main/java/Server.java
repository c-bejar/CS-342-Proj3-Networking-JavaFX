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

    Server(int port, Consumer<Serializable> call) {
        callback = call;
        server = new TheServer(port);
        server.start();
    }

    public void shutdown() {
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

    public class TheServer extends Thread {
        int portNum = 0;

        TheServer(int port) {
            portNum = port;
        }
        
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(portNum);) {
                acceptingClients = true;
                System.out.println("Server using port "+portNum);
                System.out.println("Server is waiting for a client!");

                while(true) {
                    ClientThread c = new ClientThread(mySocket.accept(), count);
                    if(!acceptingClients) {
                        shutdown();
                        continue;
                    }
                    count++;
                    callback.accept("Client "+count+" joined the Server");
                    clients.add(c);
                    c.start();
                }
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Server socket did not launch");
            }
        }

    }

    public class ClientThread extends Thread {
        Socket connection;
        int clientCount;
        ObjectOutputStream out;
        ObjectInputStream in;

        ClientThread(Socket s, int clientCount) {
            this.connection = s;
            this.clientCount = clientCount;
        }

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
                    callback.accept("client: "+count+" sent: "+data);
                } catch(Exception e) {
                    callback.accept("Client "+count+" left the Server");
                    clients.remove(this);
                    count--;
                    break;
                }
            }
        }
    }
}