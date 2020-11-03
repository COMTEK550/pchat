import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;

public class Store {
    private ArrayList<Conversation> conversations;

    public HashMap<String, User> users;

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
        System.out.printf("Added user %s%n", user.name);
        this.users.put(user.name, user);
    }
    public void remove_user(User user){
        this.users.remove(user.name);
    }
    public void send_back_users(MessageSender out) throws IOException{
        for(User user : this.users.values() ){
            RegisterMessage out_rmsg = new RegisterMessage(user.pkey,user.name);

            out.send(out_rmsg);

        }
    }

    public Conversation get_conversation(int id) throws NoSuchConversationException {
        try {
            return this.conversations.get(id);
        } catch(IndexOutOfBoundsException e) {
            throw new NoSuchConversationException(id);
        }
    }

    public int register_conversation(ConversationMessage cmsg) throws Exception {
        // Check if users exist
        for (String user : cmsg.users) {
            if (!this.check(user)) {
                throw new NoSuchUserException(user);
            }
        }

        Conversation conv = new Conversation(cmsg.users);
        conversations.add(conv);
        return conversations.size() - 1;
    }

    public void replay_msg_for_user(MessageSender out, User user) throws IOException {
        // Not efficient, should probably have a map with users and conversations
        for (int i = 0; i < this.conversations.size(); i++) {
            Conversation conv = this.conversations.get(i);
            if (conv.has_user(user.name)) {
                ConversationMessage cmsg = new ConversationMessage(conv.get_users());
                cmsg.id = i;
                out.send(cmsg);

                conv.replay(out);
            }
        }
    }
}

class NoSuchUserException extends ClientException {
    public NoSuchUserException(String user) {
        super(String.format("no such user: %s", user));
    }
}

class NoSuchConversationException extends ClientException {
   public NoSuchConversationException(int id)  {
       super(String.format("no such conversation: %d", id));
   }

}