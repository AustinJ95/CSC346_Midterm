import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLITE {
    Connection conn;
    Statement statement;
    final String DBFileName = "midtermdb.db";

    public void connectToDB() {
        String DBPath = "jdbc:sqlite:" + DBFileName;
        conn = null;
        try {
            conn = DriverManager.getConnection(DBPath);
            statement = conn.createStatement();
            System.out.println("connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.out.println("not connected");
            System.exit(1);
        }
    }

    public void closeDB() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
