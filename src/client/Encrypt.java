import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface Encrypt {
    public String decryptText(String msg, PrivateKey key) throws Exception;
    public String encryptText(String msg, PublicKey key) throws Exception;
}
