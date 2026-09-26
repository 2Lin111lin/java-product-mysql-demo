import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库工具类：获取连接、关闭资源
 */
public class DBUtil {
    // 1.数据库连接信息，修改为你自己的账号密码
    private static final String URL = "";
    private static final String USER = "";
    // =========这里替换成你的mysql root密码========
    private static final String PASSWORD = "";

    // 静态代码块：加载驱动，程序启动只执行一次
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接对象
     * @return Connection 连接
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭资源，重载方法1：增删改的时候调用，没有ResultSet结果集
     */
    public static void close(Connection conn, PreparedStatement pstmt){
        try {
            if(pstmt != null) pstmt.close();
            if(conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源，重载方法2：查询的时候调用，需要关闭结果集ResultSet
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try{
            if(rs != null) rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        close(conn,pstmt); //调用原来的两参数close
    }
    
}
