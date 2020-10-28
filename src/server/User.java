import java.security.PublicKey;

public class User {
    public String name;
    public PublicKey pkey;

    public User(String name, PublicKey pkey) {
        this.name = name;
        this.pkey = pkey;
    }
}
