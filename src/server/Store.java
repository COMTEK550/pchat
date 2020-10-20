import java.sql.Connection;
import java.sql.DriverManager;

public class Store {
    private Connection sql;
    public Store(String username, String password) throws Exception {
        sql = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pchatdb", username, password);
    }
}
