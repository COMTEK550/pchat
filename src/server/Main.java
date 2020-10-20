import java.io.IOException;

public class Main {
    public static void main(String[] args){

        if (args.length != 1) {
            System.err.println("Usage: server <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        Listener listener = new Listener(portNumber);
        try {
            listener.listen();
        } catch(IOException e) {
            System.out.printf("Fejl: %s%n", e);
        }
    }
}
