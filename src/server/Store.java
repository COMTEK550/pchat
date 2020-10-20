import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class Store {
    private Connection sql;
    private ArrayList<Conversation> conversations;

    public Store(String username, String password) {
        //sql = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pchatdb", username, password);

        this.conversations = new ArrayList<>();

        ArrayList<User> test_users = new ArrayList<>();
        test_users.add(new User("nicholas"));
        this.conversations.add(new Conversation(test_users));
    }

    public Conversation get_conversation(int id) {
        return this.conversations.get(id);
    }
}
