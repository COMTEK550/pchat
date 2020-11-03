import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class Conversation {
    private ArrayList<Message> msgs;
    private HashSet<String> users;
    private ConversationMessage cmsg;

    public Conversation(ConversationMessage cmsg) {
        this.msgs = new ArrayList<>();
        this.users = new HashSet<>();
        this.cmsg = cmsg;

        for (String user : cmsg.users) {
            this.users.add(user);
        }
    }

    public void replay_init(MessageSender out) throws IOException {
        out.send(this.cmsg);
    }

    public void send_here(Listener listener, TextMessage msg) throws IOException {
        System.out.printf("Sending message to conversation: %s%n", msg.toString());
        msgs.add(msg);

        for (String user : this.users) {
            listener.send_to_user(msg, user);
        }
    }

    public boolean has_user(String user) {
        return this.users.contains(user);
    }
    public String[] get_users() {
        String[] users = new String[this.users.size()];
        this.users.toArray(users);
        return users;
    }

    public void replay(MessageSender out) throws IOException {
        for (Message msg : this.msgs) {
            out.send(msg);
        }
    }
}
