import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

enum Status {
    NOTCONNECTED,
    CONNECTED
}

public class Client {
    private HashMap<Integer, String[]> conversations = new HashMap<>();
    private String user;
    private HashMap<Integer, byte[]> conv_keys;
    private HashMap<String, PublicKey> users;
    private SecureRandom rnd;
    private SecretKeyFactory keyFactory;
    private Frontend frontend;
    private ObjectOutputStream out;
    private Status status;
    private MessageListener listener;

    private static Client client_instance = null;

    private KeyManager km;

    public Client() throws Exception {
        this.conv_keys = new HashMap<>();
        this.users = new HashMap<>();

        this.keyFactory = SecretKeyFactory.getInstance("DES");
        this.rnd = new SecureRandom();
        this.status = Status.NOTCONNECTED;
    }
    public static Client getInstance(Frontend f) throws Exception {
        if (client_instance == null) {
            client_instance = new Client();

        }

        client_instance.frontend = f;
        return client_instance;
        }

    public void connect(String hostName, int portNumber, String username, KeyManager km) throws Exception {
        if (this.status == Status.CONNECTED) {
            throw new Exception("Already connected");
        }
        this.user = username;
        this.km = km;
        Socket echoSocket = new Socket(hostName, portNumber);

        RegisterMessage rmsg = new RegisterMessage(this.km.getPublicKey(), this.user);

        //START INPUT
        this.listener = new MessageListener(echoSocket, this);
        listener.start();

        //START OUTPUT
        this.out = new ObjectOutputStream(echoSocket.getOutputStream());
        out.writeObject(rmsg);

        this.status = Status.CONNECTED;
    }

    public void disconnect() throws IOException {
        this.out.flush();
        this.out.close();
        this.listener.close();
        this.status = Status.NOTCONNECTED;
    }

    public void sendTxtMsg(String msg, int conv_id) throws Exception {
        DESKeySpec keyspec = new DESKeySpec(this.conv_keys.get(conv_id));
        SecretKey key = this.keyFactory.generateSecret(keyspec);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainTextByte = msg.getBytes("UTF8");
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);

        TextMessage message = new TextMessage(encryptedBytes, conv_id);
        out.writeObject(message);
    }

    public void newConversation(String[] users) throws Exception{
        ConversationMessage cmsg = new ConversationMessage(users);
        AsymmetricEncryption enc = new AsymmetricEncryption();
        byte[] key = new byte[16];
        this.rnd.nextBytes(key);
        for (String s : users) {
            cmsg.keys.put(s, enc.encryptBytes(key, this.users.get(s)));
        }
        out.writeObject(cmsg);
    }

    public void handle_msg(Message msg) throws Exception {

        if (msg.getClass() == TextMessage.class) {
            TextMessage tmsg = (TextMessage) msg;

            DESKeySpec keyspec = new DESKeySpec(this.conv_keys.get(tmsg.conversation));
            SecretKey key = this.keyFactory.generateSecret(keyspec);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);


            this.frontend.newTxtMsg(tmsg.decrypt(cipher), tmsg.conversation, tmsg.user, tmsg.stamp);

        } else if (msg.getClass() == ErrorMessage.class) {
            ErrorMessage emsg = (ErrorMessage) msg;
            System.out.printf("Server err: %s%n", emsg);
            this.frontend.newErrMsg(emsg.toString());
        } else if (msg.getClass() == ConversationMessage.class) {
            ConversationMessage cmsg = (ConversationMessage) msg;
            this.conversations.put(cmsg.id, cmsg.users);
            AsymmetricEncryption asEnc = new AsymmetricEncryption();

            byte[] key = asEnc.decryptBytes(cmsg.keys.get(this.user), this.km.getPrivateKey());
            this.conv_keys.put(cmsg.id, key);

            this.frontend.newConMsg(cmsg.id, cmsg.users);
        } else if (msg.getClass() == RegisterMessage.class) {
            RegisterMessage rmsg = (RegisterMessage) msg;
            this.users.put(rmsg.name, rmsg.key);
            this.frontend.newRegMsg(rmsg.name);
        }

    }

}
class MessageListener extends Thread {
    private Socket sock;
    private  Client client;
    private ObjectInputStream in;

    public MessageListener(Socket sock, Client client){
        try {
            this.sock = sock;
            this.client = client;

        }catch (Exception e){
            System.out.printf("Failed to start outputstream: ", e);
        }
    }

    public void run(){
        try {
            in = new ObjectInputStream(sock.getInputStream());
            while(true) {

                Message msg = (Message) in.readObject();
                System.out.println("GOT MESSAGE!!!");
                client.handle_msg(msg);

            }
        }catch (Exception e) {
            System.out.printf("Failed to recieve: %s %n", e);
            e.printStackTrace();

        }
    }
    public void close() throws IOException {
        this.stop();


        this.in.close();
        this.sock.close();
    }
}
