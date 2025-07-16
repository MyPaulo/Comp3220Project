import java.sql.Connection;
import java.sql.SQLException;

/**
 * The DatabaseConnection interface provides an abstraction for establishing and managing 
 * a database connection, ensuring flexibility and separation of connection logic.
 */
public interface DatabaseConnection {
    /**
     * Establishes and returns a database connection.
     *
     * @return a Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the database connection if it is open.
     *
     * @throws SQLException if an error occurs while closing the connection.
     */
    void closeConnection() throws SQLException;
}
