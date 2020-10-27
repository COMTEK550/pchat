import java.io.Serializable;

public class ConversationMessage extends Message implements Serializable {
    // Set by server when sending to clients
    public int id;
    public String[] users;

    public ConversationMessage(String[] users) {
        this.users = users;
    }
}
