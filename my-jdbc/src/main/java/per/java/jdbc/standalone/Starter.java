package per.java.jdbc.standalone;

import java.sql.Connection;
import java.sql.SQLException;

public class Starter {
    
    public static void main(String[] args) throws SQLException {
        String a1Url="jdbc:a1:xxxxxxx";
        String b1Url="jdbc:b1:xxxxxxx";

        Connection a1Con = MyDriverManager.getConnection(a1Url);
        Connection b1Con = MyDriverManager.getConnection(b1Url);

    }
    
}
