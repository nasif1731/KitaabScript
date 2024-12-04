package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MockDatabaseConnection{

    private static MockDatabaseConnection instance;
    private BlockingQueue<Connection> connectionPool;
    private static final int POOL_SIZE = 10;

    private static final String TEST_URL = "jdbc:mysql://127.0.0.1:3306/kitaab_script_test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private MockDatabaseConnection() throws SQLException {
    	connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            connectionPool.add(DriverManager.getConnection(TEST_URL, USER, PASSWORD));
        }
    }
   
    public static synchronized MockDatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new MockDatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws InterruptedException {
        return connectionPool.take(); 
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
