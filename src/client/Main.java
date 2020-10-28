
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        Client client = new Client();

        client.connect(hostName, portNumber);
    }

}
class Client {
    private HashMap<Integer,String[]> conversations = new HashMap<>();
    private int selected;
    private PublicKey pkey;
    private String user;
    private HashMap<Integer,byte[]> conv_keys = new HashMap<>();
    private HashMap<String,PublicKey> users = new HashMap<>();
    public void connect(String hostName, int portNumber) {
        ObjectOutputStream out;
        try{
        Socket echoSocket = new Socket(hostName, portNumber);

        //KUN TIL TEST

        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));


        generate_keys();

        System.out.println("give username");

        this.user = obj.readLine();
        RegisterMessage rmsg = new RegisterMessage(this.pkey, this.user);

        //START INPUT
        MessageRecieved msgRecieved = new MessageRecieved(echoSocket, this);
        msgRecieved.start();

        //START OUTPUT
        out = new ObjectOutputStream(echoSocket.getOutputStream());
        out.writeObject(rmsg);

        while(true){
            String message = obj.readLine();
            if(message.startsWith("-startc")){
                String members = message.split(" ")[1];
                String[] member_split = members.split(",");
                ConversationMessage cmsg = new ConversationMessage(member_split);
                AsymmetricEncryption enc = new AsymmetricEncryption();
                for(int i=0;i<member_split.length;i++){
                    cmsg.keys.put(member_split[i],enc.encryptText("hejmeddig",this.users.get(member_split[i])));
                }
                out.writeObject(cmsg);
            } else if(message.startsWith("-listc")){
                 for(int i=0;i<this.conversations.size();i++){
                     System.out.println(Arrays.toString(this.conversations.get(i)));
                 }
            } else if(message.startsWith("-select")) {
                String idstr = message.split(" ")[1];
                this.selected = Integer.parseInt(idstr);

            } else if(message.startsWith("-users")){
                for(int i=0;i<this.users.size();i++){
                    System.out.println(this.users.keySet());
                }
            } else {
                int conv_id;
                try {
                    sent_text(message, out, this.selected);

                } catch (Exception e) {
                    System.out.printf("Forkert conversation id: %s %n",e);
                }
                }
            }

        } catch(
                UnknownHostException e)

        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch(
                Exception e)

        {
            System.err.printf("fejl: %s%n", e);
            System.exit(1);
        }
    }

    private void generate_keys(){
        GenerateKeys gk;
        try {
            gk = new GenerateKeys(1024);
            gk.createKeys();
            this.pkey = gk.getPublicKey();
            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void sent_text(String msg, ObjectOutputStream out,int conv_id) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");

        SymmetricEncryption symEnc = new SymmetricEncryption();
        keyGenerator.init(168);
        SecretKey secretKey = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance("DESede");
        byte[] plainTextByte = msg.getBytes("UTF8");
        byte[] encryptedBytes = symEnc.encrypt(plainTextByte, secretKey);

        TextMessage message = new TextMessage(encryptedBytes,conv_id);
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handle_msg(Message msg) throws Exception{

        if(msg.getClass()==TextMessage.class){

            TextMessage tmsg = (TextMessage) msg;
            SecretKey secretKey = new SecretKeySpec(this.conv_keys.get(tmsg.conversation), "DESede");
            byte[] decryptedBytes = SymmetricEncryption.decrypt(tmsg.msg, secretKey);
            String decryptedText = new String(decryptedBytes, "UTF8");
            System.out.println(decryptedText);
        } else if(msg.getClass() == ErrorMessage.class) {
            ErrorMessage emsg = (ErrorMessage) msg;
            System.out.printf("Server err: %s%n", emsg);
        }else if(msg.getClass()==ConversationMessage.class){
            ConversationMessage cmsg = (ConversationMessage) msg;
            this.conversations.put(cmsg.id, cmsg.users);
            AsymmetricEncryption asEnc = new AsymmetricEncryption();

            byte[] key = asEnc.decryptText(cmsg.keys.get(this.user),asEnc.getPrivate("KeyPair/privateKey")).getBytes();
            this.conv_keys.put(cmsg.id, key);

            System.out.printf("Joined conversation %d with %s%n", cmsg.id, Arrays.toString(cmsg.users));
            String shhhSecret = new String(key, "UTF-8");
            System.out.println(shhhSecret);
        }else if(msg.getClass()==RegisterMessage.class){
            RegisterMessage rmsg = (RegisterMessage) msg;
            this.users.put(rmsg.name,rmsg.key);
            System.out.printf("New user has been added to list %s with key: %s %n",rmsg.name,rmsg.key);
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

        }
    }
}

