
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

        TextMessage msg = new TextMessage("Hej med dig hvordan", 0);

        try {
                Socket echoSocket = new Socket(hostName, portNumber);

                ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());


            out.writeObject(msg);

            ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());

            while(true) {
                Message m = (Message) in.readObject();
                System.out.println(m);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (Exception e) {
            System.err.printf("fejl: %s%n", e);
            System.exit(1);
        }
				*/
    }
}

