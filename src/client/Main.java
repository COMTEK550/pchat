
import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);


        try (
                Socket echoSocket = new Socket(hostName, portNumber);

                ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());

        ) {
            out.writeObject(new TextMessage("Hej med dig fra client"));
            out.writeObject(new TextMessage("Besked 2"));
            out.close();
            echoSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}

