import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class Store {
    private ArrayList<Conversation> conversations;
    private ArrayList<User> users;
    public Store(String username, String pkey) {

        this.conversations = new ArrayList<>();
        this.users = new ArrayList<>();
    }
    public boolean check(String pkey){
       return users.contains(pkey);
    }
    public void add_user(User user){
        this.users.add(user);
    }
    public void remove_user(User user){
        this.users.remove(user);
    }
 

    public void add_to_conversation(User user, Conversation conv){

    }


    public Conversation get_conversation(int id) {
        return this.conversations.get(id);
    }
}
