import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLITE {
    Connection conn;
    String DBFileName = "midtermdb.db";

    public void connectToDB() {
        String DBPath = "jdbc:sqlite:" + DBFileName;
        conn = null;
        try {
            conn = DriverManager.getConnection(DBPath);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void closeDB(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}
