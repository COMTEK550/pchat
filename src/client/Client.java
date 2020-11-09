import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Client extends Thread {
    private HashMap<Integer, String[]> conversations = new HashMap<>();
    private int selected;
    private String user;
    private String hostName;
    private int portNumber;
    private HashMap<Integer, byte[]> conv_keys;
    private HashMap<String, PublicKey> users;
    private int randomNumber;
    private SecretKeyFactory keyFactory;
    private Frontend frontend;
    private ObjectOutputStream out;

    private KeyManager km;

    public Client(Frontend f) throws Exception {
        this.conv_keys = new HashMap<>();
        this.users = new HashMap<>();
        this.frontend = f;
        this.keyFactory = SecretKeyFactory.getInstance("DES");
    }

    public void connect(String hostName, int portNumber, String username, KeyManager km) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.user = username;
        this.start();

        this.km = km;
    }
    public void run(){
        Hello.Do("Client");


        try {
            Socket echoSocket = new Socket(hostName, portNumber);

            //KUN TIL TEST

            BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

            Random random = new Random();
            this.randomNumber = random.nextInt(9999);

            RegisterMessage rmsg = new RegisterMessage(this.km.getPublicKey(), this.user);

            //START INPUT
            MessageRecieved msgRecieved = new MessageRecieved(echoSocket, this);
            msgRecieved.start();

            //START OUTPUT
            this.out = new ObjectOutputStream(echoSocket.getOutputStream());
            out.writeObject(rmsg);

            while (true) {
                String message = obj.readLine();
                if (message.startsWith("-startc")) {
                    String members = message.split(" ")[1];
                    String[] member_split = members.split(",");
                    ConversationMessage cmsg = new ConversationMessage(member_split);
                    AsymmetricEncryption enc = new AsymmetricEncryption();
                    for (String s : member_split) {
                        cmsg.keys.put(s, enc.encryptText("hejmeddig", this.users.get(s)));
                    }
                    out.writeObject(cmsg);
                } else if (message.startsWith("-listc")) {
                    for (int i = 0; i < this.conversations.size(); i++) {
                        System.out.println(Arrays.toString(this.conversations.get(i)) + " with id: " + i);
                    }
                } else if (message.startsWith("-select")) {
                    String idstr = message.split(" ")[1];
                    this.selected = Integer.parseInt(idstr);

                } else if (message.startsWith("-users")) {
                    System.out.println(this.users.keySet());

                } else {
                    try {
                        sent_text(message, this.selected);

                    } catch (Exception e) {
                        System.out.printf("Forkert conversation id: %s %n", e);
                    }
                }
            }

        } catch (
                UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (
                Exception e) {
            System.err.printf("fejl: %s%n", e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sent_text(String msg, int conv_id) throws Exception {
        DESKeySpec keyspec = new DESKeySpec(this.conv_keys.get(conv_id));
        SecretKey key = this.keyFactory.generateSecret(keyspec);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] plainTextByte = msg.getBytes("UTF8");
        byte[] encryptedBytes = cipher.doFinal(plainTextByte);

        TextMessage message = new TextMessage(encryptedBytes, conv_id);
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newConversation(String[] users) throws Exception{
        ConversationMessage cmsg = new ConversationMessage(users);
        AsymmetricEncryption enc = new AsymmetricEncryption();
        for (String s : users) {
            cmsg.keys.put(s, enc.encryptText("hejmeddig", this.users.get(s)));
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


            this.frontend.newTxtMsg(tmsg.decrypt(cipher), tmsg.conversation);

        } else if (msg.getClass() == ErrorMessage.class) {
            ErrorMessage emsg = (ErrorMessage) msg;
            System.out.printf("Server err: %s%n", emsg);
            this.frontend.newErrMsg(emsg.toString());
        } else if (msg.getClass() == ConversationMessage.class) {
            ConversationMessage cmsg = (ConversationMessage) msg;
            this.conversations.put(cmsg.id, cmsg.users);
            AsymmetricEncryption asEnc = new AsymmetricEncryption();

            byte[] key = asEnc.decryptText(cmsg.keys.get(this.user), this.km.getPrivateKey()).getBytes();
            this.conv_keys.put(cmsg.id, key);

            this.frontend.newConMsg(cmsg.id, cmsg.users);
        } else if (msg.getClass() == RegisterMessage.class) {
            RegisterMessage rmsg = (RegisterMessage) msg;
            this.users.put(rmsg.name, rmsg.key);
            this.frontend.newRegMsg(rmsg.name);
        }

    }

}
class MessageRecieved extends Thread {
    private Socket sock;
    private  Client client;
    public MessageRecieved(Socket sock,Client client){
        try {
            this.sock = sock;
            this.client = client;

        }catch (Exception e){
            System.out.printf("Failed to start outputstream: ", e);
        }
    }

    public void run(){
        try {
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
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
}

