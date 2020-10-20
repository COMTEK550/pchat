import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {
    private int port;
    public Listener(int port) {
        this.port = port;
    }

    public void listen() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);

        while (true) {
            Socket conn = serverSocket.accept();
            SocketHandler h = new SocketHandler(conn);
            h.start();
        }
    }
}

class SocketHandler extends Thread {
    private Socket conn;

    public SocketHandler(Socket conn) {
        this.conn = conn;
    }

    public void run() {

        try (
            ObjectInputStream in = new ObjectInputStream(this.conn.getInputStream());
        ) {
            while (true) {
                Message m = (Message) in.readObject();
                System.out.println(m);
            }
        } catch (EOFException e) {
            // Client disconnected do nothing
        } catch (Exception e) {
            System.out.printf("Fejl fra client: %s%n", e);
        }

    }
}
