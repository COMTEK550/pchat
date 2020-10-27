import java.io.Serializable;

public class ErrorMessage extends Message implements Serializable {
    public String msg;

    public ErrorMessage(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return this.msg;
    }
}
