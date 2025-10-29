
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {

    public static void main(String[] args) throws Exception {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        Class.forName("com.mysql.cj.jdbc.Driver");

        System.out.println("Hola desde java");

    }
}
