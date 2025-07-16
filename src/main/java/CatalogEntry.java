import java.util.ArrayList;
import java.util.List;

/*
 * DatabaseEntry Interface
 * Last Updated: 2024-11-05
 * Purpose: Serves as a holder for a database entry on the main page. This holds the associated files
 * 			as well as the data point title and description
 * */
public class CatalogEntry {
	public String title;
	public String description;
	public List<String> keywords = new ArrayList<>();
	public List<String> subjects = new ArrayList<>();
	
	//Data Catalog Descriptions
	public int dataCustodian; //id of the data
	public String dataCurrency;
	public String datasetDescription;
	public String dataAccuracy;
	public String attributes;
	public List<FilePoint> downloadFiles;
	
	private int userId;
    private String category;
    private String status;
    private String filePath;
	
	//public List<FilePoint> file;
	
	public CatalogEntry(String title, String description) {
		this.title = title;
		this.description = description;
		downloadFiles = new ArrayList<>();
	}
	
	public void setKeywordList(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	public void clearKeywords() {
		keywords.clear();
	}
	
	public void setDataCustodian(int dataCustodian) {
		this.dataCustodian = dataCustodian;
	}
	
	public void setDataCurrency(String dataCurrency) {this.dataCurrency = dataCurrency;}
	
	public void setDatasetDescription(String datasetDescription) {this.datasetDescription = datasetDescription;}
	
	public void setDataAccuracy(String dataAccuracy) {this.dataAccuracy = dataAccuracy;}
	
	public void setAttributes(String attributes) {this.attributes = attributes;}
	
	public void setUserId(int userId) {this.userId = userId;}
	
	public void setCategory(String category) {this.category = category;}
	
	public void setStatus(String status) {this.status = status;}
	
	public void setFilePath(String filePath) {this.filePath = filePath;}
	
	public int getUserId() {return userId;}
	
	public String getCategory() {return category;}
	
	public String getStatus() {return status;}
	
	public String getFilePath() {return filePath;}

	
	
	public void setDownloadFiles(List<FilePoint> downloadFiles) {this.downloadFiles = downloadFiles;}
	
	public void addDownloadFiles(FilePoint fileToAdd) {downloadFiles.add(fileToAdd);}
	
	public void clearDownloadFiles() {downloadFiles.clear();}
	
	
}
