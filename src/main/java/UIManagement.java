import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JMenuBar;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//Added by paul
import javax.swing.JComboBox;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;


/*
 * UIManagement Class
 * Last Updated: 2024-11-04
 * Purpose: To house and handle all UI elements for the City of Windsor Open Catalogue
 * Notes: To see any changes for the catalog, you must call updateCatalog(). Login Functionality Added
 * 		  TODO: Dataset Functionality
 * */
public class UIManagement extends JFrame {
	private MySQLDatabaseConnection dbConnection;  // Declare a database connection object
	private DatasetService datasetService;
	private JPanel contentPane;
	private JTextField txtKeyword;
	private JPanel panel;
	// Regular expression for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private int userId;// retrieve user ID

	//Catalog Filters
	private String[] resourceList = {"", ".CSV", ".DWG", ".KMZ", ".SHPLL84", ".SHPUTM", ".TXT", ".XLS", ".XLSX"};
	private List<CatalogEntry> savedCatalogEntries = new ArrayList<>();
	
	//Catalog Variables
	private static int dataBaseSize = 0;
	private List<CatalogEntry> catalogEntries = new ArrayList<>();
	
	//For Administrator Dashboard
	private String fullName;
	private JTable table;
	
	//Store the displays for quick future use
	JMenuBar menuBar;
	JPanel mainDashboard;
	
	//Login Method Variables
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    
    
  //for subject category used on 
    private List<String> subjects = Arrays.asList(
            "Census", "Community Services", "Land and Construction", 
            "Seasonal City Services", "Voting and Elections", 
            "Waste and Waterworks", "Transportation"
        );

	
	/**
	 * UIManagement Method
	 * Last Updated: 2024-11-04
	 * Purpose: Creates the basic frame that will store all elements
	 */
	public UIManagement() {
		
		 // Initialize the MySQL database connection object
        dbConnection = new MySQLDatabaseConnection("jdbc:mysql://localhost:3306/cityofwindsor", "root", "");
        datasetService = new DatasetService(dbConnection); // Initialize DatasetService
        //Initialize the User object
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 845, 664);
		menuBar = createMenuBar();
		mainDashboard = createMainDashboard();
		setContentPane(mainDashboard);
	}
	
	public void updateDataBaseSize(int num) {
		dataBaseSize = num;
	}
	
	public void updateCatalog() {
		getContentPane().remove(mainDashboard);
		mainDashboard = createMainDashboard();
	    setContentPane(mainDashboard);
	    
	    revalidate();
	    repaint();
	}
	
	
	public void addToCatalog(CatalogEntry newEntry) {
		dataBaseSize ++;
		catalogEntries.add(newEntry);
		savedCatalogEntries.add(newEntry);
	}
	
	public void clearCatalog() {
		dataBaseSize = 0;
		catalogEntries.clear();
	}
	
	/*
	 * createMenuBar Method
	 * Last Updated: 2024-11-04
	 * Purpose: Creates the top menu bar that may be required for global access
	 * */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar_1.setMargin(new Insets(100, 0, 100, 0));
		menuBar_1.setForeground(new Color(48, 44, 44));
		menuBar_1.setBorderPainted(false);
		menuBar_1.setBackground(new Color(48, 44, 44));
		getComponentOrientation();
		menuBar_1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		setJMenuBar(menuBar_1);
		
		JButton btnNewButton_1 = new JButton("RSS");
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBackground(new Color(48, 44, 44));
		btnNewButton_1.setBorderPainted(false);
		menuBar_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Accessibility");
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBackground(new Color(48, 44, 44));
		btnNewButton_2.setBorderPainted(false);
		menuBar_1.add(btnNewButton_2);
		
		JButton loginButton = new JButton("LOGIN/SIGN-UP");
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			    setContentPane(Login());
			    
			    revalidate();
			    repaint();
			}
		});
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(128, 128, 128));
		menuBar_1.add(loginButton);
		
		
		
		
		return menuBar_1;
	}
	
	
	
	/*
	 * createMainDashboard Method
	 * Last Updated: 2024-11-04
	 * Purpose: Creates the main dash board that is visible to all users and houses the main open
	 * 			catalog elements along with filter features.
	 * */
	private JPanel createMainDashboard() {
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(Color.white);
		contentPane.add(scrollPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(1000, 2000));
		scrollPane.setViewportView(layeredPane);
		
		JLabel lbCatalog1 = new JLabel("City of Windsor Open Data Catalog");
		lbCatalog1.setBounds(10, 10, 233, 20);
		layeredPane.add(lbCatalog1);
		lbCatalog1.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		
		JLabel lbCatalog2 = new JLabel("Open Data Catalog");
		lbCatalog2.setBounds(67, 27, 233, 42);
		layeredPane.add(lbCatalog2);
		lbCatalog2.setFont(new Font("Yu Gothic UI", Font.BOLD, 24));
		
		JTextArea txtCatalog1 = new JTextArea();
		txtCatalog1.setBounds(67, 68, 660, 64);
		layeredPane.add(txtCatalog1);
		txtCatalog1.setEditable(false);
		txtCatalog1.setWrapStyleWord(true);
		txtCatalog1.setFont(new Font("Yu Gothic", Font.PLAIN, 9));
		txtCatalog1.setText("Citizens are encouraged to use or repurpose the City of Windsor's open data for research purposes or to improve their interaction with municipal services and facilities. Members of the community can use the raw data provided here to create and share resources from maps to applications and more. \n\nPlease be aware that by downloading or accessing this data, you agreet o be bound by the City of Windsor's Open Data Terms of Use.");
		txtCatalog1.setLineWrap(true);
		
		JLabel lbCatalog3 = new JLabel("Using Open Data");
		lbCatalog3.setBounds(67, 142, 220, 32);
		layeredPane.add(lbCatalog3);
		lbCatalog3.setFont(new Font("Yu Gothic", Font.BOLD, 24));
		
		JTextArea txtCatalog2 = new JTextArea();
		txtCatalog2.setBounds(67, 171, 660, 32);
		layeredPane.add(txtCatalog2);
		txtCatalog2.setEditable(false);
		txtCatalog2.setFont(new Font("Yu Gothic", Font.PLAIN, 9));
		txtCatalog2.setText("KMZ files can be imported to Google Maps, Google Earth or Bing maps. Download the instructions.\nAre you looking for JSON? Try our new swagger implementation https://opendata.citywindsor.ca/swagger");
		txtCatalog2.setWrapStyleWord(true);
		txtCatalog2.setLineWrap(true);
		
		JLabel lbCatalog4 = new JLabel("How can we improve the catalog?");
		lbCatalog4.setBounds(67, 208, 414, 42);
		layeredPane.add(lbCatalog4);
		lbCatalog4.setFont(new Font("Yu Gothic", Font.BOLD, 24));
		
		JTextArea txtCatalog3 = new JTextArea();
		txtCatalog3.setBounds(67, 238, 220, 22);
		layeredPane.add(txtCatalog3);
		txtCatalog3.setFont(new Font("Yu Gothic", Font.PLAIN, 9));
		txtCatalog3.setEditable(false);
		txtCatalog3.setText("Email us your suggestions, ideas, or questions.");
		
		JLabel lbFilter = new JLabel("Fliter by resource type:");
		lbFilter.setBounds(67, 270, 181, 20);
		layeredPane.add(lbFilter);
		lbFilter.setFont(new Font("Yu Gothic", Font.BOLD, 12));
		
		txtKeyword = new JTextField();
		txtKeyword.setBounds(67, 341, 297, 19);
		layeredPane.add(txtKeyword);
		txtKeyword.setForeground(Color.BLACK);
		txtKeyword.setColumns(10);
		
		JLabel lbKeyword = new JLabel("Search Dataset by keyword:");
		lbKeyword.setBounds(67, 320, 181, 20);
		layeredPane.add(lbKeyword);
		lbKeyword.setFont(new Font("Yu Gothic", Font.BOLD, 12));
		
		JLabel lbCheckboxes = new JLabel("Subject:");
		lbCheckboxes.setBounds(67, 370, 49, 20);
		layeredPane.add(lbCheckboxes);
		lbCheckboxes.setFont(new Font("Yu Gothic", Font.BOLD, 12));
		
		// Create checkboxes dynamically and get the final y position
	    int finalYPosition = createSubjectCheckboxes(layeredPane);
	    
	    JButton searchButton = new JButton("Search");
	    searchButton.setBounds(67, finalYPosition + 30, 85, 21); // Position below the last checkbox
	    layeredPane.add(searchButton);
	    
		
		JPanel dataSet = new JPanel();
		dataSet.setBorder(null);
		dataSet.setBackground(new Color(255, 255, 255));
		dataSet.setBounds(370, 370, 468, (int) (140 * Math.ceil(dataBaseSize / 2.0)));
		
		for (CatalogEntry entry: catalogEntries) {
			dataSet.add(addDataSet(entry.title, entry.description, entry));
		}
		
		layeredPane.add(dataSet);
		dataSet.setLayout(new GridLayout(0, 2, 10, 10));
        
        JComboBox<?> resourceFilter = new JComboBox<Object>(resourceList);
        resourceFilter.setBounds(67, 289, 297, 21);
        layeredPane.add(resourceFilter);

        searchButton.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
	       	 String resourceType = (String) resourceFilter.getSelectedItem();
	         String keyword = txtKeyword.getText();
	         List<String> selectedSubjects = new ArrayList<>();
	
	         for (Component comp : layeredPane.getComponents()) {
	             if (comp instanceof JCheckBox) {
	                 JCheckBox checkbox = (JCheckBox) comp;
	                 if (checkbox.isSelected()) {
	                     selectedSubjects.add(checkbox.getText());
	                 }
	             }
	         }
	         
	         Filter filter = new Filter(savedCatalogEntries);
	        	catalogEntries = filter.filterByAll(selectedSubjects, resourceType, keyword);
	        	updateDataBaseSize(catalogEntries.size());
	        	updateCatalog();
        }
    });
        return contentPane;
	}
	
	private int createSubjectCheckboxes(JLayeredPane layeredPane) {
	    int yPosition = 390; // Starting y position for the first checkbox, adjusted to be below "Subject"
	    int spacing = 30; // Spacing between checkboxes
	    for (String subject : subjects) {
	        JCheckBox checkbox = new JCheckBox(subject);
	        checkbox.setFont(new Font("Arial", Font.PLAIN, 12));
	        checkbox.setBackground(Color.WHITE);
	        checkbox.setBounds(67, yPosition, 297, 21);
	        layeredPane.add(checkbox);
	        yPosition += spacing; // Increment y position for the next checkbox
	    }
	    return yPosition; // Return the final y position after adding all checkboxes
	}
	
	/*
	 * addDataSet Method
	 * Last Updated: 2024-11-04
	 * Purpose: Adds and returns a new data set button given a label and an accompanying text
	 * Notes: the text can only handle up to 81 characters before it starts breaking. Title 39 characters.
	 * */
	public JPanel addDataSet(String label, String text, CatalogEntry newEntry) {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(471, 361, 261, 140);
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
		
		
		JTextArea dataSetTitle = new JTextArea();
		dataSetTitle.setLineWrap(true);
		dataSetTitle.setEditable(false);
		dataSetTitle.setWrapStyleWord(true);
		dataSetTitle.setText(label);
		dataSetTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		dataSetTitle.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 10, 5, 10)));
		dataSetTitle.setFocusable(false);
		panel.add(dataSetTitle);
		
		JTextArea dataSetDescription = new JTextArea();
		dataSetDescription.setWrapStyleWord(true);
		dataSetDescription.setLineWrap(true);
		dataSetDescription.setFont(new Font("Yu Gothic", Font.PLAIN, 10));
		dataSetDescription.setText(text);
		dataSetDescription.setBorder(new CompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 10, 5, 10)));
		dataSetDescription.setFocusable(false);
		panel.add(dataSetDescription);
		
	    // Add MouseListener to make it clickable
	    MouseAdapter clickListener = new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	setContentPane(NewDataBoard(newEntry));
	        	

	        	revalidate();
	        	repaint();
	        }

	        @Override
	        public void mouseEntered(MouseEvent e) {
	            panel.setBackground(new Color(230, 230, 250)); // Change color on hover
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            panel.setBackground(Color.WHITE); // Reset color when not hovering
	        }
	    };
	    
	    panel.addMouseListener(clickListener);
	    dataSetTitle.addMouseListener(clickListener);
	    dataSetDescription.addMouseListener(clickListener);
	    
		return panel;
	}

	public JPanel NewDataBoard(CatalogEntry entryData) {
		setBounds(100, 100, 845, 664);
		
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255)); // Updated color
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(1, 0, 0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(255, 255, 255));
        contentPane.add(scrollPane);
        
        JPanel attributes = new JPanel();
        attributes.setBackground(Color.WHITE);
        scrollPane.setViewportView(attributes);
        GridBagLayout gbl_attributes = new GridBagLayout();
        gbl_attributes.columnWidths = new int[]{0, 0, 0, 0};
        gbl_attributes.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_attributes.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl_attributes.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
        attributes.setLayout(gbl_attributes);
        
        JLabel openData = new JLabel("Open Data Catalogue");
        openData.setHorizontalAlignment(SwingConstants.LEFT);
        openData.setFont(new Font("Yu Gothic", Font.BOLD, 24));
        GridBagConstraints gbc_openData = new GridBagConstraints();
        gbc_openData.anchor = GridBagConstraints.WEST;
        gbc_openData.insets = new Insets(0, 0, 5, 0);
        gbc_openData.gridx = 2;
        gbc_openData.gridy = 2;
        attributes.add(openData, gbc_openData);
        
        JLabel title = new JLabel(entryData.title);
        title.setHorizontalAlignment(SwingConstants.LEFT);
        title.setFont(new Font("Yu Gothic", Font.BOLD, 16));
        GridBagConstraints gbc_title = new GridBagConstraints();
        gbc_title.anchor = GridBagConstraints.WEST;
        gbc_title.insets = new Insets(0, 0, 5, 0);
        gbc_title.gridx = 2;
        gbc_title.gridy = 3;
        attributes.add(title, gbc_title);
        
        JLabel dataCustodian = new JLabel("Data Custodian");
        dataCustodian.setHorizontalAlignment(SwingConstants.LEFT);
        dataCustodian.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_dataCustodian = new GridBagConstraints();
        gbc_dataCustodian.insets = new Insets(0, 0, 5, 0);
        gbc_dataCustodian.anchor = GridBagConstraints.WEST;
        gbc_dataCustodian.gridx = 2;
        gbc_dataCustodian.gridy = 4;
        attributes.add(dataCustodian, gbc_dataCustodian);
        
        JTextArea dataCustodianTxt = new JTextArea();
        dataCustodianTxt.setText("" + entryData.dataCustodian);
        dataCustodianTxt.setEditable(false);
        dataCustodianTxt.setWrapStyleWord(true);
        dataCustodianTxt.setLineWrap(true);
        GridBagConstraints gbc_dataCustodianTxt = new GridBagConstraints();
        gbc_dataCustodianTxt.insets = new Insets(0, 0, 5, 0);
        gbc_dataCustodianTxt.anchor = GridBagConstraints.WEST;
        gbc_dataCustodianTxt.fill = GridBagConstraints.BOTH;
        gbc_dataCustodianTxt.gridx = 2;
        gbc_dataCustodianTxt.gridy = 5;
        attributes.add(dataCustodianTxt, gbc_dataCustodianTxt);
        
        JLabel dataCurrency = new JLabel("Data Currency Comments");
        dataCurrency.setHorizontalAlignment(SwingConstants.LEFT);
        dataCurrency.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_dataCurrency = new GridBagConstraints();
        gbc_dataCurrency.insets = new Insets(0, 0, 5, 0);
        gbc_dataCurrency.anchor = GridBagConstraints.WEST;
        gbc_dataCurrency.gridx = 2;
        gbc_dataCurrency.gridy = 6;
        attributes.add(dataCurrency, gbc_dataCurrency);
        
        JTextArea dataCurrencyTxt = new JTextArea();
        dataCurrencyTxt.setText(entryData.dataCurrency);
        dataCurrencyTxt.setWrapStyleWord(true);
        dataCurrencyTxt.setLineWrap(true);
        dataCurrencyTxt.setEditable(false);
        GridBagConstraints gbc_dataCurrencyTxt = new GridBagConstraints();
        gbc_dataCurrencyTxt.anchor = GridBagConstraints.WEST;
        gbc_dataCurrencyTxt.insets = new Insets(0, 0, 5, 0);
        gbc_dataCurrencyTxt.fill = GridBagConstraints.BOTH;
        gbc_dataCurrencyTxt.gridx = 2;
        gbc_dataCurrencyTxt.gridy = 7;
        attributes.add(dataCurrencyTxt, gbc_dataCurrencyTxt);
        
        JLabel datasetDescription = new JLabel("Dataset Description");
        datasetDescription.setHorizontalAlignment(SwingConstants.LEFT);
        datasetDescription.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_datasetDescription = new GridBagConstraints();
        gbc_datasetDescription.insets = new Insets(0, 0, 5, 0);
        gbc_datasetDescription.anchor = GridBagConstraints.WEST;
        gbc_datasetDescription.gridx = 2;
        gbc_datasetDescription.gridy = 8;
        attributes.add(datasetDescription, gbc_datasetDescription);
        
        JTextArea datasetDescriptionTxt = new JTextArea();
        datasetDescriptionTxt.setText(entryData.datasetDescription);
        datasetDescriptionTxt.setWrapStyleWord(true);
        datasetDescriptionTxt.setLineWrap(true);
        datasetDescriptionTxt.setEditable(false);
        GridBagConstraints gbc_datasetDescriptionTxt = new GridBagConstraints();
        gbc_datasetDescriptionTxt.anchor = GridBagConstraints.WEST;
        gbc_datasetDescriptionTxt.insets = new Insets(0, 0, 5, 0);
        gbc_datasetDescriptionTxt.fill = GridBagConstraints.BOTH;
        gbc_datasetDescriptionTxt.gridx = 2;
        gbc_datasetDescriptionTxt.gridy = 9;
        attributes.add(datasetDescriptionTxt, gbc_datasetDescriptionTxt);
        
        JLabel dataAccuracy = new JLabel("Data Accuracy Comments");
        dataAccuracy.setHorizontalAlignment(SwingConstants.LEFT);
        dataAccuracy.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_dataAccuracy = new GridBagConstraints();
        gbc_dataAccuracy.insets = new Insets(0, 0, 5, 0);
        gbc_dataAccuracy.anchor = GridBagConstraints.WEST;
        gbc_dataAccuracy.gridx = 2;
        gbc_dataAccuracy.gridy = 10;
        attributes.add(dataAccuracy, gbc_dataAccuracy);
        
        JTextArea dataAccuracyTxt = new JTextArea();
        dataAccuracyTxt.setText(entryData.dataAccuracy);
        dataAccuracyTxt.setWrapStyleWord(true);
        dataAccuracyTxt.setLineWrap(true);
        dataAccuracyTxt.setEditable(false);
        GridBagConstraints gbc_dataAccuracyTxt = new GridBagConstraints();
        gbc_dataAccuracyTxt.anchor = GridBagConstraints.WEST;
        gbc_dataAccuracyTxt.insets = new Insets(0, 0, 5, 0);
        gbc_dataAccuracyTxt.fill = GridBagConstraints.BOTH;
        gbc_dataAccuracyTxt.gridx = 2;
        gbc_dataAccuracyTxt.gridy = 11;
        attributes.add(dataAccuracyTxt, gbc_dataAccuracyTxt);
        
        JLabel lblAttributes = new JLabel("Attributes");
        lblAttributes.setHorizontalAlignment(SwingConstants.LEFT);
        lblAttributes.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_lblAttributes = new GridBagConstraints();
        gbc_lblAttributes.insets = new Insets(0, 0, 5, 0);
        gbc_lblAttributes.anchor = GridBagConstraints.WEST;
        gbc_lblAttributes.gridx = 2;
        gbc_lblAttributes.gridy = 12;
        attributes.add(lblAttributes, gbc_lblAttributes);
        
        JTextArea attributesTxt = new JTextArea();
        attributesTxt.setText(entryData.attributes);
        attributesTxt.setWrapStyleWord(true);
        attributesTxt.setLineWrap(true);
        attributesTxt.setEditable(false);
        GridBagConstraints gbc_attributesTxt = new GridBagConstraints();
        gbc_attributesTxt.anchor = GridBagConstraints.WEST;
        gbc_attributesTxt.insets = new Insets(0, 0, 5, 0);
        gbc_attributesTxt.fill = GridBagConstraints.BOTH;
        gbc_attributesTxt.gridx = 2;
        gbc_attributesTxt.gridy = 13;
        attributes.add(attributesTxt, gbc_attributesTxt);
        
        JLabel relaventDownload = new JLabel("Relavant Downloads");
        relaventDownload.setBackground(Color.WHITE);
        relaventDownload.setHorizontalAlignment(SwingConstants.LEFT);
        relaventDownload.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_relaventDownload = new GridBagConstraints();
        gbc_relaventDownload.insets = new Insets(0, 0, 5, 0);
        gbc_relaventDownload.anchor = GridBagConstraints.WEST;
        gbc_relaventDownload.gridx = 2;
        gbc_relaventDownload.gridy = 14;
        attributes.add(relaventDownload, gbc_relaventDownload);
        
        JPanel relavantDownloadTxt = new JPanel();
        relavantDownloadTxt.setBackground(Color.WHITE);
        GridBagConstraints gbc_relavantDownloadTxt = new GridBagConstraints();
        gbc_relavantDownloadTxt.fill = GridBagConstraints.BOTH;
        gbc_relavantDownloadTxt.gridx = 2;
        gbc_relavantDownloadTxt.gridy = 15;
        attributes.add(relavantDownloadTxt, gbc_relavantDownloadTxt);
        relavantDownloadTxt.setLayout(new BoxLayout(relavantDownloadTxt, BoxLayout.Y_AXIS));
        relavantDownloadTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (FilePoint dataFile : entryData.downloadFiles) {
        	relavantDownloadTxt.add(addRelavantDownload(entryData.title, dataFile));
        }
        
        
        JToolBar toolBar = new JToolBar();
        toolBar.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
	            setContentPane(mainDashboard);
	            setResizable(true);

	            
	            revalidate();
	            repaint();
        	}
        });
        toolBar.setBackground(new Color(255, 255, 230));
        scrollPane.setColumnHeaderView(toolBar);
        
        JLabel dataCatalogue = new JLabel("City of Windsor Open Data Catalogue");
        dataCatalogue.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 16));
        toolBar.add(dataCatalogue);
        
		return contentPane;
        
	}
	
	//TODO: IMPLEMENT DATABASE FUNCTIONALITY TO DOWNLOAD
	private JLabel addRelavantDownload(String entryTitle, FilePoint dataFile) {
		JLabel newDownload = new JLabel(dataFile.getFileName());
		newDownload.setBackground(Color.WHITE);
		newDownload.setForeground(new Color(34, 170, 0));
		newDownload.setFont(new Font("Monospaced", Font.PLAIN, 12));
		newDownload.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				DownloadManager newDownload = new DownloadManager(entryTitle, dataFile);
				newDownload.downloadFile();
			}
		});
		
		
		return newDownload;
	}
	
	/**
     * Create the frame.
     */
	public JPanel Login() {
	    setResizable(false); // Make the login page non-resizable
	    
	    setBounds(320, 180, 500, 500);
	    // Panel setup
	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(65, 105, 225)); // Updated color
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(null);
	    
	    // Title Label
	    JLabel lblTitle = new JLabel("Login");
	    lblTitle.setForeground(Color.WHITE);
	    lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
	    lblTitle.setBounds(200, 40, 100, 40);
	    contentPane.add(lblTitle);

	    // Username Label
	    JLabel lblUsername = new JLabel("e-Mail Address or Username");
	    lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    lblUsername.setForeground(Color.WHITE);
	    lblUsername.setBounds(120, 100, 260, 30);
	    contentPane.add(lblUsername);

	    // Username TextField
	    emailField = new JTextField();
	    emailField.setBounds(120, 130, 260, 30);
	    emailField.setToolTipText("Enter your email address or username");
	    contentPane.add(emailField);
	    emailField.setColumns(10);

	    // Password Label
	    JLabel lblPassword = new JLabel("Password");
	    lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    lblPassword.setForeground(Color.WHITE);
	    lblPassword.setBounds(120, 170, 120, 30);
	    contentPane.add(lblPassword);

	    // Password Field
	    passwordField = new JPasswordField();
	    passwordField.setBounds(120, 200, 260, 30);
	    passwordField.setToolTipText("Enter your password");
	    contentPane.add(passwordField);

	    // Role Selection ComboBox
	    JLabel lblRole = new JLabel("Role");
	    lblRole.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    lblRole.setForeground(Color.WHITE);
	    lblRole.setBounds(120, 240, 120, 30);
	    contentPane.add(lblRole);

	    JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"A", "C"});
	    roleComboBox.setBounds(120, 270, 260, 30);
	    contentPane.add(roleComboBox);

	    // Login Button
	    JButton btnLogin = new JButton("Log In");
	    btnLogin.setForeground(Color.WHITE);
	    btnLogin.setBackground(new Color(76, 175, 80)); // Green background
	    btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    btnLogin.setBounds(180, 320, 140, 40);
	    btnLogin.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            btnLogin.setBackground(new Color(56, 142, 60)); // Darker green on hover
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            btnLogin.setBackground(new Color(76, 175, 80));
	        }
	    });
	    btnLogin.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            String email = emailField.getText();
	            String password = new String(passwordField.getPassword());
	            String role = roleComboBox.getSelectedItem().toString(); // Get selected role
	            
	            // Validate email and password
	            if (email.isEmpty() || password.isEmpty()) {
	                JOptionPane.showMessageDialog(contentPane, "Please fill in both email/username and password.");
	                return;
	            }
	            
	            try {
	                // Assuming AuthService handles the login logic
	                AuthService authService = new AuthService(dbConnection);
	                boolean isValid = authService.loginUser(email, password, role);
	                
	                if (isValid) {
	                	// Retrieve userId from AuthService or database
	                	userId = authService.getUserId(email); // Add this line to get the userId
	                    // Role-based navigation logic
	                    if (role.equals("A")) {
	                        System.out.println("Logging in as Admin");
	                        // Navigate to Admin Dashboard
	                        setContentPane(Administrator());
	                        revalidate();
	                        repaint();
	                    } else {
	                        System.out.println("Logging in as Contributor");
	                        // Navigate to Contributor Dashboard
	                        setContentPane(Contributor());
	                        revalidate();
	                        repaint();
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(contentPane, "Invalid email/username or password or role.");
	                }
	            } catch (SQLException ex) {
	                JOptionPane.showMessageDialog(contentPane, "Database error: " + ex.getMessage());
	                ex.printStackTrace();
	            }
	        }
	    });
	    contentPane.add(btnLogin);

	    // Create Account Label
	    JLabel lblCreateAccount = new JLabel("Don't have an account? Create one");
	    lblCreateAccount.setForeground(Color.WHITE);
	    lblCreateAccount.setFont(new Font("Tahoma", Font.ITALIC, 14));
	    lblCreateAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    lblCreateAccount.setBounds(140, 370, 240, 30); // Adjusted bounds for better alignment
	    lblCreateAccount.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            setContentPane(CreateAccount());
	            revalidate();
	            repaint();
	        }
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            lblCreateAccount.setForeground(Color.YELLOW); // Highlight on hover
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            lblCreateAccount.setForeground(Color.WHITE);
	        }
	    });
	    contentPane.add(lblCreateAccount);

	    // Close Button
	    JLabel lblClose = new JLabel("←");
	    lblClose.setToolTipText("Go Back");
	    lblClose.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            setContentPane(mainDashboard);
	            setResizable(true);
	            setBounds(100, 100, 845, 664);
	            revalidate();
	            repaint();
	        }
	        @Override
	        public void mouseEntered(MouseEvent e) {
	            lblClose.setForeground(Color.RED);
	        }
	        @Override
	        public void mouseExited(MouseEvent e) {
	            lblClose.setForeground(new Color(139, 0, 0));
	        }
	    });
	    lblClose.setForeground(new Color(139, 0, 0));
	    lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	    lblClose.setFont(new Font("Tahoma", Font.BOLD, 20));
	    lblClose.setBounds(460, 10, 20, 20);
	    contentPane.add(lblClose);
	    
	    return contentPane;
	}

    /**
     * Create the frame.
     */
	// Helper method to validate email on Create Account
    static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email); 
        return matcher.matches();
    }
    
    
    /* Panel for Account Creation */
    public JPanel CreateAccount() {
    	setResizable(false); // Make the CreateAccount page non-resizable
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(320, 180, 500, 500);

        // Panel setup
        contentPane = new JPanel();
        contentPane.setBackground(new Color(65, 105, 225));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title Label
        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setBounds(131, 30, 266, 40);
        contentPane.add(lblTitle);

        // Email Label
        JLabel lblEmail = new JLabel("e-Mail Address");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(120, 100, 260, 30);
        contentPane.add(lblEmail);

        // Email TextField
        emailField = new JTextField();
        emailField.setBounds(120, 130, 260, 30);
        emailField.setToolTipText("Enter your email address");
        contentPane.add(emailField);
        emailField.setColumns(10);

        // Username Label
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(120, 170, 260, 30);
        contentPane.add(lblUsername);

        // Username TextField
        usernameField = new JTextField();
        usernameField.setBounds(120, 200, 260, 30);
        usernameField.setToolTipText("Enter your username");
        contentPane.add(usernameField);
        usernameField.setColumns(10);

        // Password Label
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(120, 240, 120, 30);
        contentPane.add(lblPassword);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(120, 270, 260, 30);
        passwordField.setToolTipText("Enter your password");
        contentPane.add(passwordField);

        // Create Account Button
        JButton btnCreateAccount = new JButton("Create Account");
        btnCreateAccount.setForeground(Color.WHITE);
        btnCreateAccount.setBackground(new Color(76, 175, 80)); // Green background
        btnCreateAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCreateAccount.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnCreateAccount.setBounds(150, 320, 201, 40);
        btnCreateAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCreateAccount.setBackground(new Color(56, 142, 60)); // Darker green on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnCreateAccount.setBackground(new Color(76, 175, 80));
            }
        });
        btnCreateAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add account creation logic here
            	String email = emailField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = "C"; // Set role to "Contributor" by default
                System.out.println("Create Account button clicked");
                
                //Validate email format
                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(contentPane, "Please enter a valid email address.");
                    return; // Exit the method if the email is invalid
                }
                // Check if any field is empty
                if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "Please fill in all fields.");
                    return;
                }
             // Call the registerUser method
                try {
                    AuthService authService = new AuthService(dbConnection); 
                    boolean isRegistered = authService.registerUser(username, password, email, role);
                    if (isRegistered) {
                        JOptionPane.showMessageDialog(contentPane, "Account successfully created!");
                        // Navigate to the login screen 
                        setContentPane(Login());
                        revalidate();
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "An error occurred while creating the account.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(contentPane, "Database error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            
        });
        contentPane.add(btnCreateAccount);

        // Login Label
        JLabel lblLogin = new JLabel("Already have an account? Log in here.");
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setFont(new Font("Tahoma", Font.ITALIC, 14));
        lblLogin.setBounds(150, 380, 260, 30);
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
			    setContentPane(Login());
			    
			    revalidate();
			    repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblLogin.setForeground(Color.YELLOW); // Highlight on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblLogin.setForeground(Color.WHITE);
            }
        });
        contentPane.add(lblLogin);

        // Close Button
        JLabel lblClose = new JLabel("←");
        lblClose.setToolTipText("Go Back");
        lblClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	setContentPane(mainDashboard);
            	setResizable(true);
            	setBounds(100, 100, 845, 664);
                revalidate();
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblClose.setForeground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblClose.setForeground(new Color(139, 0, 0));
            }
        });
        lblClose.setForeground(new Color(139, 0, 0));
        lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblClose.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblClose.setBounds(470, 10, 20, 20);
        contentPane.add(lblClose);
        
        return contentPane;
    }

    

 // Method to show the Dataset Entry Panel for contributors
    public void showDatasetEntryPanel() throws SQLException {
        DatasetEntryPanel datasetEntryPanel = new DatasetEntryPanel(this, dbConnection.getConnection(), userId, this, "pending");
        datasetEntryPanel.setVisible(true);
    }
    
	// Method to show the Contributor Dashboard
    public void showContributorDashboard() {
        setContentPane(Contributor());
        revalidate();
        repaint();
    }
	

    public JPanel Contributor() {
        setBounds(100, 100, 845, 664);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255)); // Updated color
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        JLayeredPane layeredPane = new JLayeredPane();
        contentPane.add(layeredPane);

        JLabel lbl1 = new JLabel("City of Windsor Open Data Catalog");
        lbl1.setFont(new Font("Yu Gothic", Font.BOLD, 14));
        lbl1.setBounds(22, 10, 253, 33);
        layeredPane.add(lbl1);

        JButton btnLogOut = new JButton("Log Out");
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCatalog();
            }
        });
        btnLogOut.setBackground(new Color(255, 255, 255));
        btnLogOut.setBounds(666, 10, 119, 27);
        layeredPane.add(btnLogOut);
        
        
        JLabel lblName = new JLabel("Welcome " +"Name" + " (Contributor)");
        lblName.setFont(new Font("Yu Gothic", Font.BOLD, 18));
        lblName.setBounds(22, 72, 288, 48);
        layeredPane.add(lblName);

        JButton btnUpload = new JButton("Upload a New Dataset");
        btnUpload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    showDatasetEntryPanel("pending");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnUpload.setBounds(22, 115, 174, 48);
        layeredPane.add(btnUpload);

        JPanel panelContribution = new JPanel();
        panelContribution.setBounds(22, 188, 436, 187);
        layeredPane.add(panelContribution);
        panelContribution.setLayout(new BorderLayout(0, 0));

        JLabel lblContribution = new JLabel("My Contributions");
        lblContribution.setFont(new Font("Yu Gothic", Font.BOLD, 16));
        panelContribution.add(lblContribution, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        panelContribution.add(scrollPane, BorderLayout.CENTER);

        JPanel contributionsPanel = new JPanel();
        contributionsPanel.setLayout(new BoxLayout(contributionsPanel, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(contributionsPanel);

        JPanel panelDataSet = new JPanel();
        panelDataSet.setBounds(22, 401, 436, 187);
        layeredPane.add(panelDataSet);
        panelDataSet.setLayout(new BorderLayout(0, 0));

        JLabel lblPending = new JLabel("My Pending Datasets");
        lblPending.setFont(new Font("Yu Gothic", Font.BOLD, 16));
        panelDataSet.add(lblPending, BorderLayout.NORTH);

        JScrollPane scrollPane_1 = new JScrollPane();
        panelDataSet.add(scrollPane_1, BorderLayout.CENTER);

        JPanel pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        scrollPane_1.setViewportView(pendingPanel);

        // Fetch and display pending datasets
        List<DatasetReviewEntry> pendingDatasets = datasetService.getPendingDatasets(userId);
        for (DatasetReviewEntry dataset : pendingDatasets) {
            JPanel datasetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout for a compact layout
            JLabel datasetLabel = new JLabel(dataset.getFileName() + " (" + dataset.getUploadDate() + ")");
            JButton deleteButton = new JButton("Delete");

            // Make the Delete button smaller and style it
            deleteButton.setFont(new Font("Yu Gothic", Font.PLAIN, 10)); // Smaller font
            deleteButton.setPreferredSize(new Dimension(70, 20)); // Fixed size
            deleteButton.setMargin(new Insets(2, 5, 2, 5)); // Adjust padding
            deleteButton.setBackground(new Color(220, 53, 69)); // Red background
            deleteButton.setForeground(Color.WHITE); // White text
            deleteButton.setFocusPainted(false); // Remove focus border
            deleteButton.setBorder(new LineBorder(new Color(200, 40, 55), 1, true)); // Border with rounded corners

            // Add a hover effect
            deleteButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    deleteButton.setBackground(new Color(200, 40, 55)); // Darker red on hover
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    deleteButton.setBackground(new Color(220, 53, 69)); // Original color when not hovering
                }
            });

         // Action listener for delete action
            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    datasetService.deleteDataset(dataset.getDatasetId());
                    pendingPanel.remove(datasetPanel);
                    pendingPanel.revalidate();
                    pendingPanel.repaint();
                }
            });

            // Add components in sequence
            datasetPanel.add(datasetLabel);
            datasetPanel.add(deleteButton);
            pendingPanel.add(datasetPanel);
        }

        // Fetch and display approved datasets
        List<DatasetReviewEntry> approvedDatasets = datasetService.getApprovedDatasets(userId);
        for (DatasetReviewEntry dataset : approvedDatasets) {
            JPanel datasetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Use FlowLayout for a compact layout
            JLabel datasetLabel = new JLabel(dataset.getFileName() + " (" + dataset.getUploadDate() + ")");
            // Add components in sequence
            datasetPanel.add(datasetLabel);
            contributionsPanel.add(datasetPanel);
        }

        return contentPane;
    }


    // Method to show the Dataset Entry Panel for admin
    private void showDatasetEntryPanel(String status) throws SQLException {
        DatasetEntryPanel datasetEntryPanel = new DatasetEntryPanel(this, dbConnection.getConnection(), userId, this, status);
        datasetEntryPanel.setVisible(true);
    }

    // Method to show the Admin Dashboard
    public void showAdminDashboard() {
        setContentPane(Administrator());
        revalidate();
        repaint();
    }
    
    //Administrator
    public JPanel Administrator() {
        setBounds(100, 100, 845, 664);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255)); // Updated color
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        JLayeredPane layeredPane = new JLayeredPane();
        contentPane.add(layeredPane);

        JLabel lbl1 = new JLabel("City of Windsor Open Data Catalog");
        lbl1.setFont(new Font("Yu Gothic", Font.BOLD, 14));
        lbl1.setBounds(22, 10, 253, 33);
        layeredPane.add(lbl1);

        JButton btnLogOut = new JButton("Log Out");
        styleButton(btnLogOut, new Color(255, 255, 255), new Color(0, 0, 0));
        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCatalog();
            }
        });
        btnLogOut.setBounds(666, 10, 119, 27);
        layeredPane.add(btnLogOut);

        JLabel lblJoseph;
        if (fullName != null) {
            lblJoseph = new JLabel(fullName + " (Administrator)");
        } else {
            lblJoseph = new JLabel("Example Name" + " (Administrator)");
        }
        lblJoseph.setFont(new Font("Yu Gothic", Font.BOLD, 18));
        lblJoseph.setBounds(22, 72, 288, 48);
        layeredPane.add(lblJoseph);

        JLabel lblNewLabel = new JLabel("Pending Review");
        lblNewLabel.setFont(new Font("Yu Gothic", Font.BOLD, 18));
        lblNewLabel.setBounds(22, 284, 533, 33);
        layeredPane.add(lblNewLabel);

        JButton btnApprovals = new JButton("My Recent Approvals");
        styleButton(btnApprovals, new Color(0, 120, 215), Color.WHITE);
        btnApprovals.setFont(new Font("Yu Gothic", Font.BOLD, 10));
        btnApprovals.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblNewLabel.setText("My Recent Approvals");
                showRecentApprovals();
            }
        });
        btnApprovals.setBounds(22, 117, 153, 48);
        layeredPane.add(btnApprovals);

        JButton btnAnalytics = new JButton("Analytics");
        styleButton(btnAnalytics, new Color(34, 139, 34), Color.WHITE); // Different color
        btnAnalytics.setFont(new Font("Yu Gothic", Font.BOLD, 10));
        btnAnalytics.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblNewLabel.setText("Analytics");
                // Load and display analytics in the table
            }
        });
        btnAnalytics.setBounds(222, 117, 153, 48);
        layeredPane.add(btnAnalytics);

        JButton btnUpload = new JButton("Upload a New Dataset");
        styleButton(btnUpload, new Color(255, 165, 0), Color.WHITE); // Different color
        btnUpload.setFont(new Font("Yu Gothic", Font.BOLD, 10));
        btnUpload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
					showDatasetEntryPanel("approved");
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnUpload.setBounds(22, 197, 153, 48);
        layeredPane.add(btnUpload);

        JButton btnAccount = new JButton("Manage Accounts");
        styleButton(btnAccount, new Color(220, 20, 60), Color.WHITE); // Different color
        btnAccount.setFont(new Font("Yu Gothic", Font.BOLD, 10));
        btnAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblNewLabel.setText("Manage Accounts");
                // Load and display account management in the table
                showManageAccounts();
            }
        });
        btnAccount.setBounds(222, 197, 153, 48);
        layeredPane.add(btnAccount);

        JButton btnPendingReview = new JButton("Pending Review");
        styleButton(btnPendingReview, new Color(70, 130, 180), Color.WHITE); // Different color
        btnPendingReview.setFont(new Font("Yu Gothic", Font.BOLD, 10));
        btnPendingReview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblNewLabel.setText("Pending Review");
                showPendingReview();
            }
        });
        btnPendingReview.setBounds(422, 117, 153, 48);
        layeredPane.add(btnPendingReview);

        panel = new JPanel();
        panel.setBounds(22, 324, 533, 253);
        layeredPane.add(panel);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);

        return contentPane;
    }
    //method to display pending review for admin
    private void showPendingReview() {
        List<DatasetReviewEntry> pendingDatasets = datasetService.getPendingDatasetsForAdmin();
        JPanel pendingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add table headers
        gbc.gridx = 0;
        gbc.gridy = 0;
        addStyledLabel(pendingPanel, "File Name", gbc);
        gbc.gridx = 1;
        addStyledLabel(pendingPanel, "Email", gbc);
        gbc.gridx = 2;
        addStyledLabel(pendingPanel, "Upload Date", gbc);
        gbc.gridx = 3;
        addStyledLabel(pendingPanel, "Actions", gbc);

        // Add dataset entries
        int row = 1;
        for (DatasetReviewEntry dataset : pendingDatasets) {
            gbc.gridx = 0;
            gbc.gridy = row;
            addStyledLabel(pendingPanel, dataset.getFileName(), gbc);

            gbc.gridx = 1;
            addStyledLabel(pendingPanel, dataset.getEmail(), gbc);

            gbc.gridx = 2;
            addStyledLabel(pendingPanel, dataset.getUploadDate(), gbc);

            gbc.gridx = 3;
            JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton approveButton = createStyledButton("Approve", new Color(34, 139, 34), Color.WHITE);
            JButton declineButton = createStyledButton("Decline", new Color(220, 20, 60), Color.WHITE);

            approveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try (Connection conn = dbConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(
                                 "UPDATE datasets SET status = ?, approval_date = CURRENT_TIMESTAMP, approved_by = ? WHERE dataset_id = ?")) {
                        stmt.setString(1, "approved");
                        stmt.setInt(2, userId); // Assuming userId is the admin's user ID
                        stmt.setInt(3, dataset.getDatasetId());
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(pendingPanel, "Dataset approved successfully.");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    pendingPanel.remove(actionsPanel.getParent());
                    pendingPanel.revalidate();
                    pendingPanel.repaint();
                    showAdminDashboard();
                }
            });

            declineButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirmation = JOptionPane.showConfirmDialog(pendingPanel, "Are you sure you want to delete this dataset?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        datasetService.deleteDataset(dataset.getDatasetId()); // Deletes dataset and related files
						JOptionPane.showMessageDialog(pendingPanel, "Dataset deleted successfully.");
						pendingPanel.remove(actionsPanel.getParent());
						pendingPanel.revalidate();
						pendingPanel.repaint();
                    }
                }
            });

            actionsPanel.add(approveButton);
            actionsPanel.add(declineButton);
            pendingPanel.add(actionsPanel, gbc);

            row++;
        }

        JScrollPane scrollPane = new JScrollPane(pendingPanel);
        panel.removeAll(); // Clear the existing content
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
    
    private void showRecentApprovals() {
        List<DatasetReviewEntry> approvedDatasets = datasetService.getApprovedDatasetsByAdmin(userId);
        JPanel approvalsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add table headers
        addTableHeader(approvalsPanel, gbc);

        // Add dataset entries
        int row = 1;
        for (DatasetReviewEntry dataset : approvedDatasets) {
            addDatasetEntry(approvalsPanel, gbc, dataset, row);
            row++;
        }

        JScrollPane scrollPane = new JScrollPane(approvalsPanel);
        panel.removeAll(); // Clear the existing content
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void addTableHeader(JPanel panel, GridBagConstraints gbc) {
        String[] headers = {"File Name", "Contributor", "Date Added", "Status", "Date Approved", "Actions"};
        for (int i = 0; i < headers.length; i++) {
            gbc.gridx = i;
            gbc.gridy = 0;
            addStyledLabel(panel, headers[i], gbc);
        }
    }

    private void addDatasetEntry(JPanel panel, GridBagConstraints gbc, DatasetReviewEntry dataset, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        addStyledLabel(panel, dataset.getFileName(), gbc);

        gbc.gridx = 1;
        addStyledLabel(panel, dataset.getEmail(), gbc);

        gbc.gridx = 2;
        addStyledLabel(panel, dataset.getDateAdded(), gbc);

        gbc.gridx = 3;
        addStyledLabel(panel, dataset.getStatus(), gbc);

        gbc.gridx = 4;
        addStyledLabel(panel, dataset.getApprovalDate(), gbc);

        gbc.gridx = 5;
        JButton deleteButton = createDeleteButton(dataset);
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.add(deleteButton);
        panel.add(actionsPanel, gbc);
    }

    private JButton createDeleteButton(DatasetReviewEntry dataset) {
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Yu Gothic", Font.PLAIN, 10)); // Smaller font
        deleteButton.setPreferredSize(new Dimension(70, 20)); // Fixed size
        deleteButton.setMargin(new Insets(2, 5, 2, 5)); // Adjust padding
        deleteButton.setBackground(new Color(220, 53, 69)); // Red background
        deleteButton.setForeground(Color.WHITE); // White text
        deleteButton.setFocusPainted(false); // Remove focus border
        deleteButton.setBorder(new LineBorder(new Color(200, 40, 55), 1, true)); // Border with rounded corners

        // Add a hover effect
        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                deleteButton.setBackground(new Color(200, 40, 55)); // Darker red on hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(new Color(220, 53, 69)); // Original color when not hovering
            }
        });

        // Action listener for delete action
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete this file?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    System.out.println("Attempting to delete file with ID: " + dataset.getFileId());
                    datasetService.deleteFile(dataset.getFileId()); // Ensure this uses the correct file ID
                    JOptionPane.showMessageDialog(panel, "File deleted successfully.");
                    showRecentApprovals(); // Refresh the table after deletion
                }
            }
        });

        return deleteButton;
    }
    
    private JTable usersTable; // Add this to hold the reference to the users table

    private void showManageAccounts() {
        try {
           // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel accountsPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Search bar
        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Search by ID or Username");

        // Placeholder text
        searchField.setText("Search by name or ID");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search by name or ID")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search by name or ID");
                }
            }
        });

        JButton searchButton = new JButton("Search");
        styleButton(searchButton, new Color(70, 130, 180), Color.WHITE);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                if (searchText.equals("Search by name or ID")) {
                    searchText = ""; // Reset search text if placeholder is still there
                }
                List<User> users;
                if (searchText.isEmpty()) {
                    users = datasetService.getAllUsers();
                } else {
                    users = datasetService.searchUsers(searchText);
                }
                displayUsers(users);
            }
        });

        // Trigger search on Enter key press
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });

        // Add button
        JButton addButton = new JButton("Add User");
        styleButton(addButton, new Color(34, 139, 34), Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        // Delete button
        JButton deleteButton = new JButton("Delete User");
        styleButton(deleteButton, new Color(220, 20, 60), Color.WHITE);

        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);
        topPanel.add(deleteButton);

        accountsPanel.add(topPanel, BorderLayout.NORTH);

        // Table to display users
        usersTable = new JTable();
        accountsPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        panel.removeAll(); // Clear existing content
        panel.add(accountsPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();

        // Load and display all users
        List<User> users = datasetService.getAllUsers();
        displayUsers(users);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Integer> userIdsToDelete = new ArrayList<>();
                for (int i = 0; i < usersTable.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) usersTable.getValueAt(i, 0);
                    if (isSelected != null && isSelected) {
                        int userId = (int) usersTable.getValueAt(i, 1);
                        userIdsToDelete.add(userId);
                    }
                }
                if (!userIdsToDelete.isEmpty()) {
                    int confirmation = JOptionPane.showConfirmDialog(usersTable, "Are you sure you want to delete the selected users?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        for (int userId : userIdsToDelete) {
                            datasetService.deleteUser(userId);
                        }
                        JOptionPane.showMessageDialog(usersTable, "Users deleted successfully.");
                        showManageAccounts(); // Refresh the list
                    }
                } else {
                    JOptionPane.showMessageDialog(usersTable, "Please select users to delete.");
                }
            }
        });
    }

    private void displayUsers(List<User> users) {
        String[] columnNames = {"Select", "User ID", "Username", "Email", "Role", "Created At"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        for (User user : users) {
            model.addRow(new Object[]{false, user.getUserId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt()});
        }
        usersTable.setModel(model);
        usersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
     // Adjust column widths
        TableColumnModel columnModel = usersTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // Select
        columnModel.getColumn(1).setPreferredWidth(50);  // User ID
        columnModel.getColumn(2).setPreferredWidth(100); // Username
        columnModel.getColumn(3).setPreferredWidth(200); // Email
        columnModel.getColumn(4).setPreferredWidth(50);  // Role
        columnModel.getColumn(5).setPreferredWidth(100); // Created At
    }
    

    private void addUser() {
        JPanel addPanel = new JPanel(new GridLayout(5, 2));

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"A", "C"});

        addPanel.add(new JLabel("Username:"));
        addPanel.add(usernameField);
        addPanel.add(new JLabel("Email:"));
        addPanel.add(emailField);
        addPanel.add(new JLabel("Password:"));
        addPanel.add(passwordField);
        addPanel.add(new JLabel("Role:"));
        addPanel.add(roleComboBox);

        boolean isValidInput = false;
        while (!isValidInput) {
            int result = createCustomDialog(addPanel);

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                // Validate email
                if (!email.matches(EMAIL_REGEX)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid email address.");
                    continue; // Prompt the dialog again for correction
                }

                try {
                    datasetService.addUser(username, email, password, role);
                    JOptionPane.showMessageDialog(null, "User added successfully.");
                    isValidInput = true; // Exit the loop if the input is valid
                    showManageAccounts(); // Refresh the list
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
                isValidInput = true; // Exit the loop if the user cancels
            }
        }
    }

    private int createCustomDialog(JPanel addPanel) {
        String[] options = {"Add", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            null, 
            addPanel, 
            "Add User", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        return result;
    }


    private void addStyledLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(50, 50, 50));
        panel.add(label, gbc);
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }



        
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
 
}
