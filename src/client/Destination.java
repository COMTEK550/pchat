public class Destination {
    public String host;
    public int port;

    public Destination(String str) throws NumberFormatException {
        // Split by :
        String[] parts = str.split(":");
        if (parts.length > 1) {
            this.port = Integer.parseInt(parts[1]);
        } else {
            this.port = 6969;
        }

        if (parts[0].equals("")) {
            this.host = "localhost";
        } else {
            this.host = parts[0];
        }
    }
}
