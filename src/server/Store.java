import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

public class Store {
    private ArrayList<Conversation> conversations;

    private HashMap<String, User> users;

    public Store() {

        this.conversations = new ArrayList<>();
        this.users = new HashMap<>();
    }
    public boolean check(String name){
       return users.containsKey(name);
    }

    public User get_user(String name) throws NoSuchUserException {
        User u = users.get(name);
        if (u == null) {
            throw new NoSuchUserException(name);
        }

        return u;
    }
    public void add_user(User user){
        this.users.put(user.name, user);
    }
    public void remove_user(User user){
        this.users.remove(user.name);
    }

    public void add_to_conversation(User user, Conversation conv){

    }

    public Conversation get_conversation(int id) {
        return this.conversations.get(id);
    }

    public int register_conversation(ConversationMessage cmsg) throws Exception {
        // Check if users exist
        for (String user : cmsg.users) {
            if (this.check(user)) {
                throw new NoSuchUserException(user);
            }
        }

        Conversation conv = new Conversation(cmsg.users);
        conversations.add(conv);
        return conversations.size() - 1;
    }

    public void write_conv_for_user(ObjectOutputStream out, User user) throws IOException {
        // Not efficient, should probably have a map with users and conversations
        for (int i = 0; i < this.conversations.size(); i++) {
            Conversation conv = this.conversations.get(i);
            if (conv.has_user(user.name)) {
                ConversationMessage cmsg = new ConversationMessage(conv.get_users());
                cmsg.id = i;
                out.writeObject(cmsg);

                conv.write_messages(out);
            }
        }
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String user) {
        super(String.format("no such user: %s", user));
    }
}