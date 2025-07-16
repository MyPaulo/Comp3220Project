import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * MySQLDatabaseConnection is an implementation of the DatabaseConnection interface 
 * for MySQL databases. It manages the lifecycle of a database connection using 
 * the JDBC DriverManager.
 */
public class MySQLDatabaseConnection implements DatabaseConnection {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;
    
    /**
     * Constructs a MySQLDatabaseConnection instance with specified database credentials.
     *
     * @param dbUrl the URL of the MySQL database.
     * @param dbUser the username for database access.
     * @param dbPassword the password for database access.
     */
    public MySQLDatabaseConnection(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Establishes a connection if it is not already active
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Database connection established");
        }
        return connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        // Closes the connection if it is currently open
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed");
        }
    }
}
