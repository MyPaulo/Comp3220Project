import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private int userId;
    protected String username;
    protected String email;
    protected String password;
    private String role;
    private String createdAt; // Use String for formatted date

    public User() {
        this.username = "";
        this.email = "";
        this.password = "";
    }

    public User(int userId, String username, String email, String role, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public int getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Helper method to validate email
    static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email); 
        return matcher.matches();
    }

    // Method to show users Different dashboard
    public void userDashboard() {}

    @Override
    public String toString() {
        return "User [Username= " + this.username + ", email=" + email + "]";
    }
}
