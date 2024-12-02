package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String PROD_URL = "jdbc:mysql://127.0.0.1:3306/kitaab_script";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() throws SQLException {
        String url =  PROD_URL;
        this.connection = DriverManager.getConnection(url, USER, PASSWORD);
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
