import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class TextMessage extends Message implements Serializable {
    public byte[] msg;
    public int conversation;

    public TextMessage(byte[] msg, int conversation) {
        this.msg = msg;
        this.conversation = conversation;
    }

    public String toString() {
        try {
            return new String(this.msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    return null;}
}
