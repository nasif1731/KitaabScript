package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String PROD_URL = "jdbc:mysql://127.0.0.1:3306/kitaab_script";
    private static final String TEST_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection(boolean isTest) throws SQLException {
        String url = isTest ? TEST_URL : PROD_URL;
        this.connection = DriverManager.getConnection(url, USER, PASSWORD);
    }

    public static DatabaseConnection getInstance(boolean isTest) throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection(isTest);
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
