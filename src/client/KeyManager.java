import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyManager {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public void loadKeys(String name) throws Exception {
        X509EncodedKeySpec pubspec = new X509EncodedKeySpec(loadFromFile(name + ".pub"));
        PKCS8EncodedKeySpec privspec = new PKCS8EncodedKeySpec(loadFromFile(name));

        KeyFactory kf = KeyFactory.getInstance("RSA");

        this.publicKey = kf.generatePublic(pubspec);
        this.privateKey = kf.generatePrivate(privspec);
    }

    public void genKeys(SecureRandom rnd) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public void save(String name) throws IOException {
        X509EncodedKeySpec pubspec = new X509EncodedKeySpec(this.publicKey.getEncoded());
        PKCS8EncodedKeySpec privspec = new PKCS8EncodedKeySpec(this.privateKey.getEncoded());

        writeToFile(name + ".pub", pubspec.getEncoded());
        writeToFile(name, privspec.getEncoded());
    }

    public void save_or_load(String name, SecureRandom rnd) {
        try {
            File f = new File(name);
            if (f.exists()) {
                this.loadKeys(name);
            } else {
                this.genKeys(rnd);
                this.save(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    static private void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    static private byte[] loadFromFile(String path) throws IOException {
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();

        return keyBytes;
    }
}
