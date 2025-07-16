import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The FilePoint class represents a file with its associated metadata,
 * including attributes like file ID, file name, size, type, path, and keywords.
 * This class provides getter and setter methods for each attribute and allows for
 * filtering files based on keywords.
 */
public class FilePoint {
    private final String fileID;
    private String fileName;
    private long fileSize;
    private String fileType;
    private String filePath;
    private List<String> keywords;

     /**
     * Constructor to initialize essential attributes of the FilePoint.
     *
     * @param fileID   A unique ID for the file
     * @param fileName Name of the file
     * @param fileSize Size of the file in bytes
     * @param fileType Type of the file (e.g., "txt", "jpg")
     * @param filePath Path to the file's location on the filesystem
     */
    public FilePoint(String fileID, String fileName, long fileSize, String fileType, String filePath) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.filePath = filePath;
        this.keywords = new ArrayList<>(); // initialize empty keywords
    }

    // Getter methods for retrieving file attributes
    public String getFileID() { return fileID; }
    public String getFileName() { return fileName; }
    public long getFileSize() { return fileSize; }
    public String getFileType() { return fileType; }
    public String getFilePath() { return filePath; }
    public List<String> getKeywords() { return keywords; }

    // Setter methods for updating file attributes
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public void addKeyword(String keyword) {
    	keywords.add(keyword);
    }
    
    
    /**
     * Checks if the file contains a specific keyword.
     * This method is useful for filtering or searching files by keywords.
     *
     * @param keyword The keyword to search for
     * @return true if the keyword is present, false otherwise
     */
    public boolean hasKeyword(String keyword) {
        return keywords.stream()
                .anyMatch(k -> k.equalsIgnoreCase(keyword));
    }
    
    
    
    
    public void download(String destinationPath) throws IOException{
        //File input stream to read the filr from its original path
        try(FileInputStream fis = new FileInputStream(this.filePath);
            FileOutputStream fos = new FileOutputStream(destinationPath);){
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
           
            //Read from the file and write to the destination
            while ((fis.read(buffer)) != -1){
            fos.write(buffer, 0, bytesRead);
            }
            System.out.println("File downloaded successfully to: " + destinationPath);
        }
        catch (IOException e){
            System.out.println("Error downloading the file: " + e.getMessage());
            throw e; //indicates download failure
        }
    }

    public void displayFileDetails() {
        System.out.println("File ID: " + getFileID());
        System.out.println("File Name: " + getFileName());
        System.out.println("File Size: " + getFileSize() + " bytes");
        System.out.println("File Type: " + getFileType());
        System.out.println("File Path: " + getFilePath());
        System.out.println("Keywords: " + String.join(", ", getKeywords()));
    }

}
