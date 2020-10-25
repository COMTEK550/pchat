
class Main {
    public static void main(String[] args) {
        Hello.Do("Client");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new rootGUI().setVisible(true);
            }
        });
    }
}
