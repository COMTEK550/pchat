import java.io.IOException;

public interface MessageSender {
    public void send(Message msg) throws IOException;
}
