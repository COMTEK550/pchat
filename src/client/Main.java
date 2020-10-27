
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        Hello.Do("Client");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rootGUI().setVisible(true);
            }
        });

				/*
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

    public void connect(String hostName, int portNumber) {
        ObjectOutput out;
        try{
        Socket echoSocket = new Socket(hostName, portNumber);

        //KUN TIL TEST
        System.out.println("give key");
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        String keyStr = obj.readLine();
        System.out.println("give username");

        String user = obj.readLine();
        RegisterMessage rmsg = new RegisterMessage(keyStr, user);

        //START INPUT
        MessageRecieved msgRecieved = new MessageRecieved(echoSocket, this);
        msgRecieved.start();

        //START OUTPUT
        out = new ObjectOutputStream(echoSocket.getOutputStream());
        out.writeObject(rmsg);

        while(true){
            String message = obj.readLine();
            sent_text(message,out);
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

    public void sent_text(String msg, ObjectOutput out){
        TextMessage message = new TextMessage(msg,0);
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
                client.handle_msg(msg);

            }
        }catch (Exception e) {
            System.out.printf("Failed to recieve: %s %n", e);

        }
				*/
    }
}

