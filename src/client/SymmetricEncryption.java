import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class SymmetricEncryption {
    private static Cipher cipher = null;

    static byte[] encrypt(byte[] plainTextByte, SecretKey secretKey) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);
        return encryptedBytes;
    }

    static byte[] decrypt(byte[] encryptedBytes, SecretKey secretKey) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }
}
