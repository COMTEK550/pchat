import java.io.Serializable;
import java.security.PublicKey;

public class RegisterMessage extends Message implements Serializable {
    public PublicKey key;
    public String name;

    public RegisterMessage(PublicKey key, String name) {
        this.key = key;
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

}
