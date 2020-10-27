import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class Conversation {
    private ArrayList<TextMessage> msgs;
    private HashSet<String> users;

    public Conversation(String[] users) {
        this.msgs = new ArrayList<>();
        this.users = new HashSet<>();

        for (String user : users) {
            this.users.add(user);
        }
    }

    public void send_here(Listener listener, TextMessage msg) throws IOException {
        System.out.printf("Sending message to conversation: %s%n", msg.conversation);
        msgs.add(msg);

        for (String user : this.users) {
            System.out.printf("SENDING MESSAGE to %s%n", user);
            listener.send_to_user(msg, user);
        }
    }

    public boolean has_user(String user) {
        return this.users.contains(user);
    }
    public String[] get_users() {
        return (String []) this.users.toArray();
    }

    public void write_messages(ObjectOutputStream out) throws IOException {
        for (Message msg : this.msgs) {
            out.writeObject(msg);
        }
    }
}
