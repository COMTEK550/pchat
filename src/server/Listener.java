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

    public void register_client(SocketHandler h, String username) {
        this.sockets.put(username, h);
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
            User u;

            if(this.store.check(rmsg.name)) {
                u = this.store.get_user(rmsg.name);

                this.store.replay_all_for_user(sock, u);
            } else {
                u = new User(rmsg.name, rmsg.key);
                this.store.add_user(u);
            }
            register_client(sock, u.name);

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

class SocketHandler extends Thread implements MessageSender {
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
                try {
                    this.listener.handle_msg(m, this);
                } catch (ClientException e) {
                    System.out.printf("Client ERR: %s%n", e);
                }
            }
        } catch (EOFException e) {
            // Client disconnected do nothing
            System.out.println("Client disconnected");
            this.listener.unregister_client(this.me);
        } catch (Exception e) {
            System.out.printf("ERR: %s%n", e);
            e.printStackTrace();
        }
    }

    public void send(Message msg) throws IOException {
        if (this.me != null) {
            System.out.printf("Sending message to %s%n", this.me.name);
        }

        this.out.writeObject(msg);
    }
}
