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

    public User register_client(SocketHandler h, String username,String pkey) {
        this.sockets.put(username, h);
        return new User(username,pkey);
    }

    public void unregister_client(User user) {
        this.sockets.remove(user);
    }

    public boolean send_to_user(Message msg, String to) throws IOException {
        SocketHandler h = this.sockets.get(to);
        if (h == null) {
            return false;
        }

        h.send(msg);
        return true;
    }

    public void handle_msg(Message msg, SocketHandler sock) throws Exception {
        if (msg.getClass() == TextMessage.class) {
            // Text message, redirect to conversation
            TextMessage tmsg = (TextMessage) msg;
            Conversation conv = this.store.get_conversation(tmsg.conversation);
            conv.send_here(this, tmsg);
        }
        else if(msg.getClass() == RegisterMessage.class){
            RegisterMessage rmsg = (RegisterMessage) msg;
            System.out.printf("Got public key from %s: %s%n", rmsg.name, rmsg.key);
            if(!this.store.check(rmsg.name)) {
                sock.me = register_client(sock, rmsg.name, rmsg.key);
                this.store.add_user(sock.me);
            }
        } else if(msg.getClass() == ConversationMessage.class) {
            ConversationMessage cmsg = (ConversationMessage) msg;

            cmsg.id = this.store.register_conversation(cmsg);
            System.out.printf("Created conversation with id %d%n", cmsg.id);

            for (String user : cmsg.users) {
                this.send_to_user(cmsg, user);

            }
        }
    }
}

class SocketHandler extends Thread {
    private Socket conn;
    private Listener listener;
    public User me;
    private ObjectOutputStream out;

    public SocketHandler(Socket conn, Listener listener) {
        this.conn = conn;
        this.listener = listener;

    }

    public void run() {
        try (
        ObjectInputStream in = new ObjectInputStream(this.conn.getInputStream());
        ) {
            this.out = new ObjectOutputStream(this.conn.getOutputStream());

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

        this.out.writeObject(msg);
    }
}
