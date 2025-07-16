import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class UploadManager {

	public static void uploadDatasetEntry(Connection dbConnection, String title, String description, String dataCurrency,
			String datasetDescription, String dataAccuracy, String category, String status,
			File selectedFile, int userId, String attributes, UIManagement uiManagement, boolean isAdmin) {
		// Validate file type
		String fileName = selectedFile.getName();
		if (!isValidFileType(fileName)) {
			JOptionPane.showMessageDialog(null, "Invalid file type. Only .xls, .xlsx, .csv, .txt files are allowed.");
			return;
		}

		// Determine the target directory
		File targetDir = new File(System.getProperty("user.dir") + "/data/" + category);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		// Move the file to the target directory
		File targetFile = new File(targetDir, fileName);
		try {
			Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to upload the file: " + e.getMessage());
			return;
		}

		// Check if the category already exists in the database
		int datasetId = 0;
		try (PreparedStatement checkStmt = dbConnection.prepareStatement(
				"SELECT dataset_id FROM datasets WHERE category = ?")) {
			checkStmt.setString(1, category);
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next()) {
				datasetId = rs.getInt("dataset_id");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Failed to check existing dataset: " + e.getMessage());
			return;
		}

		// If the dataset exists, add the new file to it; otherwise, insert a new dataset entry
		if (datasetId > 0) {
			// Check if the file already exists in the files table
			boolean fileExists = false;
			try (PreparedStatement fileCheckStmt = dbConnection.prepareStatement(
					"SELECT COUNT(*) FROM files WHERE dataset_id = ? AND file_name = ?")) {
				fileCheckStmt.setInt(1, datasetId);
				fileCheckStmt.setString(2, fileName);
				ResultSet rs = fileCheckStmt.executeQuery();
				if (rs.next() && rs.getInt(1) > 0) {
					fileExists = true;
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Failed to check existing file: " + e.getMessage());
				return;
			}

			// Insert or update file details in the files table if the file does not exist
			if (!fileExists) {
				try (PreparedStatement fileStmt = dbConnection.prepareStatement(
						"INSERT INTO files (dataset_id, file_name, file_size, file_type, file_path, upload_date) " +
						"VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE file_name = VALUES(file_name), file_size = VALUES(file_size), file_type = VALUES(file_type)")) {
					fileStmt.setInt(1, datasetId);
					fileStmt.setString(2, fileName);
					fileStmt.setLong(3, selectedFile.length());
					fileStmt.setString(4, getFileExtension(fileName));
					fileStmt.setString(5, targetFile.getAbsolutePath());
					fileStmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));

					fileStmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "File uploaded successfully!");

					// Close the upload panel and return to the appropriate dashboard
					if (isAdmin) {
						uiManagement.setContentPane(uiManagement.Administrator());
					} else {
						uiManagement.setContentPane(uiManagement.Contributor());
					}
					uiManagement.revalidate();
					uiManagement.repaint();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Failed to insert file metadata: " + e.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(null, "File already exists in the database.");
			}
		} else {
			// Insert a new dataset entry
			try (PreparedStatement insertStmt = dbConnection.prepareStatement(
					"INSERT INTO datasets (title, description, data_custodian, data_currency, dataset_description, data_accuracy, user_id, category, status, file_path, attributes, approved_by, approval_date) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
				insertStmt.setString(1, title);
				insertStmt.setString(2, description);
				insertStmt.setInt(3, userId); // Assuming dataCustodian is the userId
				insertStmt.setString(4, dataCurrency);
				insertStmt.setString(5, datasetDescription);
				insertStmt.setString(6, dataAccuracy);
				insertStmt.setInt(7, userId);
				insertStmt.setString(8, category);
				insertStmt.setString(9, isAdmin ? "approved" : "pending"); // Set status based on user role
				insertStmt.setString(10, targetFile.getAbsolutePath());
				insertStmt.setString(11, attributes);
				if (isAdmin) {
					insertStmt.setInt(12, userId); // Set approved_by if admin
					insertStmt.setTimestamp(13, new java.sql.Timestamp(System.currentTimeMillis())); // Set approval_date to current timestamp
				} else {
					insertStmt.setNull(12, java.sql.Types.INTEGER); // Set approved_by to NULL if not admin
					insertStmt.setNull(13, java.sql.Types.TIMESTAMP); // Set approval_date to NULL if not admin
				}

				insertStmt.executeUpdate();

				// Get the generated dataset_id
				ResultSet generatedKeys = insertStmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					datasetId = generatedKeys.getInt(1);
				}

				// Insert the file details into the files table
				try (PreparedStatement fileStmt = dbConnection.prepareStatement(
						"INSERT INTO files (dataset_id, file_name, file_size, file_type, file_path, upload_date) " +
						"VALUES (?, ?, ?, ?, ?, ?)")) {
					fileStmt.setInt(1, datasetId);
					fileStmt.setString(2, fileName);
					fileStmt.setLong(3, selectedFile.length());
					fileStmt.setString(4, getFileExtension(fileName));
					fileStmt.setString(5, targetFile.getAbsolutePath());
					fileStmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));

					fileStmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Dataset and file uploaded successfully!");

					// Close the upload panel and return to the appropriate dashboard
					if (isAdmin) {
						uiManagement.setContentPane(uiManagement.Administrator());
					} else {
						uiManagement.setContentPane(uiManagement.Contributor());
					}
					uiManagement.revalidate();
					uiManagement.repaint();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Failed to insert file metadata: " + e.getMessage());
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Failed to insert dataset metadata: " + e.getMessage());
			}
		}
	}

	private static boolean isValidFileType(String fileName) {
		String[] validExtensions = {".xls", ".xlsx", ".csv", ".txt"};
		for (String ext : validExtensions) {
			if (fileName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	private static String getFileExtension(String fileName) {
		int lastIndexOfDot = fileName.lastIndexOf('.');
		if (lastIndexOfDot == -1) {
			return ""; // No extension found
		}
		return fileName.substring(lastIndexOfDot);
	}
}