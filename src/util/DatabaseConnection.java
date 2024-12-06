package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private BlockingQueue<Connection> connectionPool;

    private static String PROD_URL;
    private static String USER;
    private static String PASSWORD;
    private static int POOL_SIZE;

    private DatabaseConnection() throws SQLException {
        loadDatabaseConfig();
        connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                connectionPool.add(DriverManager.getConnection(PROD_URL, USER, PASSWORD));
            } catch (SQLException e) {
                System.err.println("Failed to add connection to the pool: " + e.getMessage());
                throw new SQLException("Error creating database connection for the pool", e);
            }
        }
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            PROD_URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
            POOL_SIZE = Integer.parseInt(properties.getProperty("db.poolSize", "10")); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static Connection getConnection() throws InterruptedException {
        return instance.connectionPool.take(); 
    }

    public void releaseConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try {
                connectionPool.put(connection);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
