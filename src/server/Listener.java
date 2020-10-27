import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Listener {
    private int port;
    private HashMap<String, SocketHandler> sockets;
    private Store store;

    public Listener(int port, Store store) {
        this.port = port;
        this.sockets = new HashMap<>();
        this.store = store;
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);

        while (true) {
            Socket conn = serverSocket.accept();
            SocketHandler h = new SocketHandler(conn, this);
            h.start();
        }
    }

    public User register_client(SocketHandler h, String username) {
        this.sockets.put(username, h);
        return new User(username);
    }

    public void unregister_client(User user) {
        this.sockets.remove(user);
    }

    public boolean send_to_user(Message msg, User to) throws IOException {
        SocketHandler h = this.sockets.get(to.name);
        if (h == null) {
            return false;
        }

        h.send(msg);
        return true;
    }

    public void handle_msg(Message msg, SocketHandler sock) throws IOException {
        if (msg.getClass() == TextMessage.class) {
            // Text message, redirect to conversation
            TextMessage tmsg = (TextMessage) msg;
            Conversation conv = this.store.get_conversation(tmsg.conversation);
            conv.send_here(this, tmsg);
        }
        else if(msg.getClass() == RegisterMessage.class){

            RegisterMessage rmsg = (RegisterMessage) msg;
            System.out.printf("Got public key from %s: %s%n", rmsg.name, rmsg.key);

            sock.me = register_client(sock,rmsg.name);

        }
    }

}

class SocketHandler extends Thread {
    private Socket conn;
    private Listener listener;
    public User me;

    public SocketHandler(Socket conn, Listener listener) {
        this.conn = conn;
        this.listener = listener;
    }

    public void run() {
        try (
            ObjectInputStream in = new ObjectInputStream(this.conn.getInputStream());
        ) {
            System.out.println("got a client");
            while (true) {

                Message m = (Message) in.readObject();
                System.out.println("client message");
                this.listener.handle_msg(m, this);
            }
        } catch (EOFException e) {
            // Client disconnected do nothing
            System.out.println("Client disconnected");
            this.listener.unregister_client(this.me);
        } catch (Exception e) {
            System.out.printf("Fejl fra client: %s%n", e);
        }
    }

    public void send(Message msg) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(this.conn.getOutputStream());
        out.writeObject(msg);
    }
}
