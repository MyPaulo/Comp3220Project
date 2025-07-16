import java.sql.SQLException;
import java.util.List;

public class Main {
	public static void main(String args[]) throws ClassNotFoundException {
		System.out.println("Hello World");
		
		MySQLDatabaseConnection conn = new MySQLDatabaseConnection("jdbc:mysql://localhost:3306/cityofwindsor", "root", "");
		DatasetService datasetService = new DatasetService(conn);
		try {
			conn.getConnection();// print "Database connection established" if successful
		}catch(SQLException e) {
			System.err.println("Failed to establish database connection: " + e.getMessage());
			
		}/*finally {
            try {
                con.closeConnection();  // This will print "Database connection closed" if successful
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
            }
		} */
		
	
		// Fetch all approved datasets from the database
	    List<CatalogEntry> approvedDatasets = datasetService.getAllApprovedDatasets();
		//This is for testing
		UIManagement frame = new UIManagement();
		for(CatalogEntry entry : approvedDatasets) {
			frame.addToCatalog(entry);		
		}
		frame.setVisible(true);
		frame.updateCatalog();
		//frame.updateDataSet();
	}
}
