public class TextMessage extends Message {
    private String msg;

    public TextMessage(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return this.msg;
    }
}
