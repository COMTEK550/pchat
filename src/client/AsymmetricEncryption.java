import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AsymmetricEncryption{
    private Cipher cipher;

    public AsymmetricEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("RSA");
    }
    public String decryptText(String msg, PrivateKey key)
            throws Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] tmp = cipher.update(Base64.getDecoder().decode(msg));

        return new String(cipher.doFinal(tmp), "UTF-8");
    }

    public String encryptText(String msg, PublicKey key)
            throws Exception{
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
    }
}