import java.io.Serializable;

public class RegisterMessage extends Message implements Serializable {
    public String key;
    public String name;

    public RegisterMessage(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String toString() {
        return this.key;
    }

}
