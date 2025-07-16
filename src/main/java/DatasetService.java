import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class DatasetService{
    private DatabaseConnection dbConnection;

    public DatasetService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    public void userDashboard() {
    	
    }
    
  //get approved dataset and relvant files in the mainpage
    public List<CatalogEntry> getAllApprovedDatasets() {
        List<CatalogEntry> approvedDatasets = new ArrayList<>();
        String sql = "SELECT * FROM datasets WHERE status = 'approved'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                CatalogEntry entry = new CatalogEntry(rs.getString("title"), rs.getString("description"));
                entry.setDataCustodian(rs.getInt("data_custodian"));
                entry.setDataCurrency(rs.getString("data_currency"));
                entry.setDatasetDescription(rs.getString("dataset_description"));
                entry.setDataAccuracy(rs.getString("data_accuracy"));
                entry.setAttributes(rs.getString("attributes"));
                entry.setUserId(rs.getInt("user_id"));
                entry.setCategory(rs.getString("category"));
                entry.setStatus(rs.getString("status"));
                entry.setFilePath(rs.getString("file_path"));

                // Fetch associated download files
                String fileSql = "SELECT * FROM files WHERE dataset_id = ?";
                try (PreparedStatement fileStmt = conn.prepareStatement(fileSql)) {
                    fileStmt.setInt(1, rs.getInt("dataset_id"));
                    try (ResultSet fileRs = fileStmt.executeQuery()) {
                        while (fileRs.next()) {
                            FilePoint filePoint = new FilePoint(
                                fileRs.getString("file_id"),
                                fileRs.getString("file_name"),
                                fileRs.getLong("file_size"),
                                fileRs.getString("file_type"),
                                fileRs.getString("file_path")
                            );
                            entry.addDownloadFiles(filePoint);
                        }
                    }
                }

                approvedDatasets.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return approvedDatasets;
    }
    
    //this method fetch all files for a dataset
    public List<FilePoint> getFilesForDataset(int datasetId) {
        List<FilePoint> files = new ArrayList<>();
        String sql = "SELECT * FROM files WHERE dataset_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, datasetId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FilePoint filePoint = new FilePoint(
                        rs.getString("file_id"),
                        rs.getString("file_name"),
                        rs.getLong("file_size"),
                        rs.getString("file_type"),
                        rs.getString("file_path")
                    );
                    files.add(filePoint);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }
    
    
    
    
    //pending dataset for contributor 
    public List<DatasetReviewEntry> getPendingDatasets(int userId) {
        List<DatasetReviewEntry> pendingDatasets = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.dataset_id, f.file_id, f.file_name, f.upload_date, f.file_path " +
                     "FROM datasets d " +
                     "JOIN files f ON d.dataset_id = f.dataset_id " +
                     "WHERE d.user_id = ? AND d.status = 'pending'")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int datasetId = rs.getInt("dataset_id");
                int fileId = rs.getInt("file_id"); // Retrieve file_id
                String fileName = rs.getString("file_name");
                String uploadDate = rs.getString("upload_date");
                String filePath = rs.getString("file_path");
                pendingDatasets.add(new DatasetReviewEntry(datasetId, fileId, fileName, uploadDate, filePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingDatasets;
    }
  //fetch pending dataset for Admin 
    public List<DatasetReviewEntry> getPendingDatasetsForAdmin() {
        List<DatasetReviewEntry> pendingDatasets = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.dataset_id, f.file_id, f.file_name, f.upload_date, f.file_path, u.email " +
                     "FROM datasets d " +
                     "JOIN files f ON d.dataset_id = f.dataset_id " +
                     "JOIN users u ON d.user_id = u.user_id " +
                     "WHERE d.status = 'pending'")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int datasetId = rs.getInt("dataset_id");
                int fileId = rs.getInt("file_id"); // Retrieve file_id
                String fileName = rs.getString("file_name");
                String uploadDate = rs.getString("upload_date");
                String filePath = rs.getString("file_path");
                String email = rs.getString("email");
                pendingDatasets.add(new DatasetReviewEntry(datasetId, fileId, fileName, uploadDate, filePath, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pendingDatasets;
    }
    
  //fetch Approved dataset for contributor 
    public List<DatasetReviewEntry> getApprovedDatasets(int userId) {
        List<DatasetReviewEntry> approvedDatasets = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.dataset_id, f.file_id, f.file_name, f.upload_date, f.file_path " +
                     "FROM datasets d " +
                     "JOIN files f ON d.dataset_id = f.dataset_id " +
                     "WHERE d.user_id = ? AND d.status = 'approved'")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int datasetId = rs.getInt("dataset_id");
                int fileId = rs.getInt("file_id"); // Retrieve file_id
                String fileName = rs.getString("file_name");
                String uploadDate = rs.getString("upload_date");
                String filePath = rs.getString("file_path");
                approvedDatasets.add(new DatasetReviewEntry(datasetId, fileId, fileName, uploadDate, filePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return approvedDatasets;
    }
    
  //fetch Approved dataset for  Admin 
    public List<DatasetReviewEntry> getApprovedDatasetsByAdmin(int adminId) {
        List<DatasetReviewEntry> approvedDatasets = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.dataset_id, f.file_id, f.file_name, d.date_added, d.status, DATE_FORMAT(d.approval_date, '%Y-%m-%d') AS approval_date, u.email " +
                     "FROM datasets d " +
                     "JOIN files f ON d.dataset_id = f.dataset_id " +
                     "JOIN users u ON d.user_id = u.user_id " +
                     "WHERE d.approved_by = ? AND d.status = 'approved'")) {
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int datasetId = rs.getInt("dataset_id");
                int fileId = rs.getInt("file_id"); // Retrieve file_id
                String fileName = rs.getString("file_name");
                String dateAdded = rs.getString("date_added");
                String status = rs.getString("status");
                String approvalDate = rs.getString("approval_date");
                String email = rs.getString("email");
                approvedDatasets.add(new DatasetReviewEntry(datasetId, fileId, fileName, dateAdded, status, approvalDate, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return approvedDatasets;
    }


    
    
    public void updateDatasetStatus(int datasetId, String status) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE datasets SET status = ?, approval_date = CURRENT_TIMESTAMP WHERE dataset_id = ?")) {
            stmt.setString(1, status);
            stmt.setInt(2, datasetId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteFile(int fileId) {
        Connection conn = null;
        boolean autoCommit = true;
        String filePath = null;

        try {
            conn = dbConnection.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // Start transaction

            System.out.println("Database connection established");

            // Retrieve the file path before deleting the file
            try (PreparedStatement retrieveStmt = conn.prepareStatement("SELECT file_path FROM files WHERE file_id = ?")) {
                retrieveStmt.setInt(1, fileId);
                ResultSet rs = retrieveStmt.executeQuery();
                if (rs.next()) {
                    filePath = rs.getString("file_path");
                } else {
                    System.err.println("No file found with ID: " + fileId);
                    return;
                }
            }

            // Delete the file from the files table
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM files WHERE file_id = ?")) {
                stmt.setInt(1, fileId);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected); // Log the number of rows affected
                if (rowsAffected == 0) {
                    System.err.println("No file found with ID: " + fileId);
                } else {
                    System.out.println("File with ID " + fileId + " deleted successfully.");
                }
            }

            // Delete the file from the file system
            if (filePath != null) {
                File file = new File(filePath);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("Deleted file: " + filePath);
                    } else {
                        System.err.println("Failed to delete file: " + filePath);
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(autoCommit); // Restore auto-commit mode
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
    
    
    

    public void deleteDataset(int datasetId) {
        List<String> filePaths = new ArrayList<>();
        Connection conn = null;
        boolean autoCommit = true;

        try {
            conn = dbConnection.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // Start transaction

            // Retrieve all file paths before deleting the dataset
            try (PreparedStatement stmt = conn.prepareStatement("SELECT file_path FROM files WHERE dataset_id = ?")) {
                stmt.setInt(1, datasetId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    filePaths.add(rs.getString("file_path"));
                }
            }

            // Delete from files table
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM files WHERE dataset_id = ?")) {
                stmt.setInt(1, datasetId);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Files deleted: " + rowsAffected);
            }

            // Delete from datasets table
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM datasets WHERE dataset_id = ?")) {
                stmt.setInt(1, datasetId);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Datasets deleted: " + rowsAffected);
            }

            // Delete the files from the file system
            for (String filePath : filePaths) {
                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + filePath);
                        } else {
                            System.err.println("Failed to delete file: " + filePath);
                        }
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(autoCommit); // Restore auto-commit mode
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
   
    
   
    
    public List<User> searchUsers(String searchText) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, DATE_FORMAT(created_at, '%Y-%m-%d') as created_at FROM users WHERE username LIKE ? OR user_id LIKE ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String createdAt = rs.getString("created_at"); // Use String for formatted date
                users.add(new User(userId, username, email, role, createdAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, email, role, DATE_FORMAT(created_at, '%Y-%m-%d') as created_at FROM users";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String createdAt = rs.getString("created_at"); // Use String for formatted date

                users.add(new User(userId, username, email, role, createdAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    
    
    
    public void addUser(String username, String email, String password, String role) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                throw new IllegalArgumentException("Username or email already exists.");
            } else {
                e.printStackTrace();
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

    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

}
