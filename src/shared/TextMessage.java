import java.io.Serializable;

public class TextMessage extends Message implements Serializable {
    public String msg;
    public int conversation;

    public TextMessage(String msg, int conversation) {
        this.msg = msg;
        this.conversation = conversation;
    }

    public String toString() {
        return this.msg;
    }
}
