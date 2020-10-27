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
        User[] users = new User[cmsg.users.length];
        for (int i = 0; i < cmsg.users.length; i++) {
            users[i] = this.get_user(cmsg.users[i]);
        }

        Conversation conv = new Conversation(users);
        conversations.add(conv);
        return conversations.size() - 1;
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String user) {
        super(String.format("no such user: %s", user));
    }
}