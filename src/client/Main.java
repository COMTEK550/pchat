
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
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
    private String pkey;
    public void connect(String hostName, int portNumber) {
        ObjectOutputStream out;
        try{
        Socket echoSocket = new Socket(hostName, portNumber);

        //KUN TIL TEST

        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));


        generate_keys();

        System.out.println("give username");

        String user = obj.readLine();
        RegisterMessage rmsg = new RegisterMessage(this.pkey, user);

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
                out.writeObject(cmsg);
            }
             else if(message.startsWith("-listc")){
                 for(int i=0;i<this.conversations.size();i++){
                     System.out.println(Arrays.toString(this.conversations.get(i)));
                 }

            }else {
                int conv_id;
                try {
                    String[] message_split = message.split("_");
                    conv_id = Integer.parseInt(message_split[1]);
                    sent_text(message_split[0], out, conv_id);

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
            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void sent_text(String msg, ObjectOutputStream out,int conv_id) throws Exception {
        String secret = "hejmeddig";
        Encryption encryption = new Encryption();
        String encrypted_message = encryption.encryptText(secret,encryption.getPrivate("KeyPair/privateKey"));

        TextMessage message = new TextMessage(encrypted_message,conv_id);
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void handle_msg(Message msg){
        if(msg.getClass()==TextMessage.class){
            TextMessage tmsg = (TextMessage) msg;
            System.out.println(tmsg.toString());
        }if(msg.getClass()==ConversationMessage.class){
            ConversationMessage cmsg = (ConversationMessage) msg;

            this.conversations.put(cmsg.id, cmsg.users);
            System.out.println(cmsg.users);
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

