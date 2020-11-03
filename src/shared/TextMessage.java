import java.io.Serializable;
import javax.crypto.Cipher;

public class TextMessage extends Message implements Serializable {
    public byte[] msg;
    public int conversation;

    public TextMessage(byte[] msg, int conversation) {
        this.msg = msg;
        this.conversation = conversation;
    }

    public String decrypt(Cipher cipher) throws Exception {
        byte[] dec = cipher.doFinal(this.msg);
        return new String(dec);
    }
}
