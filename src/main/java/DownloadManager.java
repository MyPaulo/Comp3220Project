import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * DownloadManager Class
 * Last Updated: 2024-11-12
 * Purpose: When given a folder title and the file inside of said folder, will go to the location and copy the contents to a user chosen destination
 * Notes: catalogEntryTitle is the name of the folder inside data, fileDownload is the file that it will be downloading
 * */

public class DownloadManager {
    private FilePoint fileDownload;
    private String catalogEntryTitle;
    private static final String download_path = "./data"; // This assumes that the data folder is under Comp3220Project1 folder

    /*
     * DownloadManager Method
     * Last Updated: 2024-11-12
     * Purpose: Instantiates an instance of the DownloadManager class
     * */
    public DownloadManager() {
    }

    /*
     * DownloadManager Method
     * Last Updated: 2024-11-12
     * Purpose: Instantiates an instance of the DownloadManager class with filled in components
     * Input: A string for the folder title and a FilePoint that holds the file name.
     * */
    public DownloadManager(String catalogEntryTitle, FilePoint fileDownload) {
        this.catalogEntryTitle = catalogEntryTitle;
        this.fileDownload = fileDownload;
    }

    /*
     * setCatalogEntryTitle Method
     * Last Updated: 2024-11-12
     * Purpose: sets the catalog entry title to a given string
     * Input: A string that sets the catalogEntryTitle
     * */
    public void setCatalogEntryTitle(String catalogEntryTitle) {
        this.catalogEntryTitle = catalogEntryTitle;
    }

    /*
     * setFile Method
     * Last Updated: 2024-11-12
     * Purpose: sets a new FilePoint to download
     * Input: A FilePoint which is the file that will be downloaded
     * */
    public void setFile(FilePoint fileDownload) {
        this.fileDownload = fileDownload;
    }

    /*
     * getCatalogEntryTitle Method
     * Last Updated: 2024-11-12
     * Purpose: a getter method for catalogEntryTitle
     * Output: String of catalogEntryTitle
     * */
    public String getCatalogEntryTitle() {
        return catalogEntryTitle;
    }

    /*
     * getFileDownload Method
     * Last Updated: 2024-11-12
     * Purpose: a getter method for fileDownload
     * Output: FilePoint of the fileDownload
     * */
    public FilePoint getfileDownload() {
        return fileDownload;
    }

    /*
     * downloadFile Method
     * Last Updated: 2024-11-12
     * Purpose: a method which downloads the file given an accurate fileDownload and catalogEntryTitle
     * Notes: The file path will be ./data/catalogEntryTitle/fileDownload_name/fileDownload_type
     * */
    public void downloadFile() {
        if (fileDownload != null) {
            String filepath = download_path + "/" + catalogEntryTitle.trim() + "/" + fileDownload.getFileName().trim();
            File sourceFile = new File(filepath);

            // Debugging statement to check the file path
            System.out.println("Attempting to access file at: " + filepath);

            // Checks if there is a file
            if (!sourceFile.exists()) {
                JOptionPane.showMessageDialog(null, "Source file not found: " + filepath, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prompt the user to select a destination folder using JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a destination folder");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File destinationDir = fileChooser.getSelectedFile();
                File destinationFile = new File(destinationDir, sourceFile.getName());

                // Copy the file from source to destination
                try (FileInputStream fis = new FileInputStream(sourceFile);
                     FileOutputStream fos = new FileOutputStream(destinationFile)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }

                    JOptionPane.showMessageDialog(null, "File downloaded successfully to: " + destinationFile.getAbsolutePath(),
                            "Download Complete", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error during file download: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No folder selected. Download canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No file selected for download.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}