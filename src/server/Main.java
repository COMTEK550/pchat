import java.io.IOException;

public class Main {
    public static void main(String[] args){

        if (args.length != 1) {
            System.err.println("Usage: server <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        Store store = new Store();
        Listener listener = new Listener(portNumber, store);
        try {
            System.out.println("Listening");
            listener.listen();
        } catch(Exception e) {
            System.out.printf("Fejl: %s%n", e);
        }
    }
}
