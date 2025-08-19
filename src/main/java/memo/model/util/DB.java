package memo.model.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load(); // 이거 주의하세요... (배포 의식)
    public static final String URL = dotenv.get("DB_URL");
    public static final String USER = dotenv.get("DB_USER");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new RuntimeException("환경변수 에러 (.env 세팅해주세요!)");
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("커넥션 연결 실패");
            System.err.println(e.getMessage());
            throw new SQLException(e);
        }
    }
}