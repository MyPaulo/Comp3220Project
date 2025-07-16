public class DatasetReviewEntry {
    private int datasetId;
    private int fileId; // Add this field
    private String fileName;
    private String uploadDate;
    private String filePath;
    private String email; // Optional field for admin view
    private String dateAdded;
    private String status; 
    private String approvalDate; 

    // Constructor for contributor view (without email)
    public DatasetReviewEntry(int datasetId, int fileId, String fileName, String uploadDate, String filePath) {
        this.datasetId = datasetId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
    }

    // Constructor for admin view (with email)
    public DatasetReviewEntry(int datasetId, int fileId, String fileName, String uploadDate, String filePath, String email) {
        this.datasetId = datasetId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
        this.email = email;
    }
    
    // Constructor for recent approvals view (with additional fields)
    public DatasetReviewEntry(int datasetId, int fileId, String fileName, String dateAdded, String status, String approvalDate, String email) {
        this.datasetId = datasetId;
        this.fileId = fileId;
        this.fileName = fileName;
        this.dateAdded = dateAdded;
        this.status = status;
        this.approvalDate = approvalDate;
        this.email = email;
    }

    // Getters
    public int getDatasetId() { 
        return datasetId; 
    }
    public int getFileId() { 
        return fileId; 
    }
    public String getFileName() { 
        return fileName; 
    }
    public String getUploadDate() { 
        return uploadDate; 
    }
    public String getFilePath() { 
        return filePath; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getStatus() { 
        return status; 
    }
    public String getApprovalDate() { 
        return approvalDate; 
    }
    public String getDateAdded() {
        return dateAdded;
    }
}