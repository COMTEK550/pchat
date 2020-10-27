import java.io.IOException;
import java.util.ArrayList;

public class Conversation {
    private ArrayList<TextMessage> msgs;
    private User[] users;

    public Conversation(User[] users) {
        this.msgs = new ArrayList<>();
        this.users = users;
    }

    public void send_here(Listener listener, TextMessage msg) throws IOException {
        System.out.printf("Sending message to conversation: %s%n", msg.conversation);
        msgs.add(msg);

        for (User user : this.users) {
            System.out.println("SENDING MESSAGE");
            listener.send_to_user(msg, user.name);
        }
    }
}
