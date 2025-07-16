/*
 * Filter Class
 * Description: Given a set of files and keywords, this class should be able to sort based on category and/or keywords given.
 */

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private List<CatalogEntry> files;

    /**
     * Constructor for Filter class
     * Initializes Filter object with a list of files.
     * @param files a list of filePoint objects representing the files to filter
     */
    public Filter(List<CatalogEntry> files) {
        this.files = files;
    }
    
    public List<CatalogEntry> getList() {return files;}
  
    /**
     * filterByKeyword method
     * Filters the file by keyword and returns data that matches the keyword.
     * @param keyword the keyword to filter files by
     * @return        a list that matches the keyword
     */
    public List<CatalogEntry> filterByKeyword(List<String> keywords) {
        List<CatalogEntry> filteredFiles = new ArrayList<>();
        for (CatalogEntry file : files) {
        	for (FilePoint file2 : file.downloadFiles) {
        		for (String keyword : keywords) {
            		if (file2.hasKeyword(keyword)) {
            			filteredFiles.add(file);
            			break;
            		}
        		}
        	}
        }
        return filteredFiles;
    }

    /**
     * filterByResourceType method
     * Filters the files based on resource type.
     * @param resourceType the resource type to filter files by (e.g. CSV, DWG, KMZ...)
     * @return             a list that matches the resource type
     */
    public List<CatalogEntry> filterByResourceType(String resourceType) {
        List<CatalogEntry> filteredFiles = new ArrayList<>();
        for (CatalogEntry file : files) {
        	for (FilePoint file2 : file.downloadFiles) {
                if (file2.getFileType().equalsIgnoreCase(resourceType)) {
                    filteredFiles.add(file);
                    break;
                }	
        	}
        }
        return filteredFiles;
    }

    /**
     * filterByKeywordAndResourceType method
     * Outputs the filtered list by combining both keyword and resource type filters.
     * @param keyword      the keyword to filter files by
     * @param resourceType the resource type to filter files by
     * @return             a list that matches both the keyword and resource type
     */
    public List<CatalogEntry> filterByKeywordAndResourceType(List<String> keywords, String resourceType) {
        List<CatalogEntry> filteredFiles = new ArrayList<>();
        for (CatalogEntry file : files) {
        	for (FilePoint file2 : file.downloadFiles) {
        		for (String keyword : keywords) {
                    if (file2.hasKeyword(keyword) && file2.getFileType().equalsIgnoreCase(resourceType)) {
                        filteredFiles.add(file);
                        break;
                    }
        		}
        	}
        }
        return filteredFiles;
    }
    
    public List<CatalogEntry> filterByAll(List<String> selectedSubjects, String resourceType, String keyword) {
    	List<CatalogEntry> output = new ArrayList<>();
    	keyword = keyword.toLowerCase();
    	for (CatalogEntry entry : files) {
    		//selectedSubjects filter
    		for (String subject : selectedSubjects) {
    			if (entry.subjects.contains(subject)) {
    				output.add(entry);
    				break;
    			}
    		}
    		//resourceType filter
    		String tempResource = resourceType.toLowerCase();
    		if (!resourceType.equals("")) {
	    		for (FilePoint fileInCatalog : entry.downloadFiles) {
	    			String tempFileInCatalog = fileInCatalog.getFileType().toLowerCase();
	    			if (tempFileInCatalog.equals(tempResource)) {
	    				output.add(entry);
	    				break;
	    			}
	    		}
    		}
    		//keyword filter
    		String tempTitle = entry.title.toLowerCase();
    		if (!keyword.equals("")) {
	    		if (tempTitle.contains(keyword)) {
	    			output.add(entry);
	    		}
    		}
    	}
		return output;
    }

    /**
     * outputFilteredList method
     * Outputs the filtered lists along with file details.
     */
    public void outputFilteredList(List<FilePoint> filteredFiles) {
        for (FilePoint file : filteredFiles) {
            file.displayFileDetails();
        }
    }

}