import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;

public class ConversationMessage extends Message implements Serializable {
    // Set by server when sending to clients
    public int id;
    public String[] users;
    public HashMap<String, byte[]> keys = new HashMap<>();

    public ConversationMessage(String[] users) {
        this.users = users;
    }
}
