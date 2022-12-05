import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionSQLIte {
    public void connectSQLIte(){
        try{
                Class.forName("org.sqlite.JDBC");
            Connection co = DriverManager.getConnection("jdbc:sqlite:C:\\SQLite\\CurrencyExchangeDatabase.db");
            System.out.println("Connect");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
