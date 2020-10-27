public class ClientException extends Exception {
    public ClientException(String msg) {
        super(msg);
    }

    public ErrorMessage to_msg() {
        return new ErrorMessage(this.getMessage());
    }
}
