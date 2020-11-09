import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;

public class AsymmetricEncryption{
    private Cipher cipher;

    public AsymmetricEncryption() throws Exception {
        this.cipher = Cipher.getInstance("RSA");
    }

    public byte[] decryptBytes(byte[] msg, PrivateKey key) throws Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(msg);
    }

    public byte[] encryptBytes(byte[] msg, PublicKey key) throws Exception {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(msg);
    }
}
