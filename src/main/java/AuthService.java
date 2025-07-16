import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private final DatabaseConnection dbConnection;

    public AuthService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean registerUser(String username, String password, String email, String role) throws SQLException {
        // Print raw password for debugging
        System.out.println("Registering user. Raw password: " + password);

        // Hash the password
        String hashedPassword = hashPassword(password);
        System.out.println("Registering user. Hashed password: " + hashedPassword); // Debug statement

        // SQL query to insert user data
        String insertUserSQL = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement insertUserStmt = conn.prepareStatement(insertUserSQL)) {

            insertUserStmt.setString(1, username);
            insertUserStmt.setString(2, hashedPassword);  // Save the hashed password
            insertUserStmt.setString(3, email);
            insertUserStmt.setString(4, role);  // Role will be either 'U' or 'A'

            int rowsAffected = insertUserStmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean loginUser(String usernameOrEmail, String password, String selectedRole) throws SQLException {
        // Print raw password for debugging
        System.out.println("Logging in user. Raw password: " + password);

        // Hash the password
        String hashedPassword = hashPassword(password);
        System.out.println("Logging in user. Hashed password: " + hashedPassword); // Debug statement

        // SQL query to authenticate user
        String loginSQL = "SELECT u.user_id, u.username, u.role " +
                          "FROM users u " +
                          "WHERE (u.username = ? OR u.email = ?) AND u.password = ? AND u.role = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement loginStmt = conn.prepareStatement(loginSQL)) {

            loginStmt.setString(1, usernameOrEmail);
            loginStmt.setString(2, usernameOrEmail);
            loginStmt.setString(3, hashedPassword);
            loginStmt.setString(4, selectedRole);  // Role 'A' for Admin, 'U' for User

            System.out.println("Executing query: " + loginStmt.toString()); // Debug statement

            ResultSet resultSet = loginStmt.executeQuery();

            if (resultSet.next()) {
                String roleName = resultSet.getString("role");
                System.out.println("Login successful. Redirecting to " + roleName + " dashboard.");
                return true;
            } else {
                System.out.println("Invalid login credentials or role.");
                return false;
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }
    
    public int getUserId(String emailOrUsername) throws SQLException {
        String query = "SELECT user_id FROM users WHERE email = ? OR username = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                throw new SQLException("User not found");
            }
        }
    }
}
