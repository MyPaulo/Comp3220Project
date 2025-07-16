import java.awt.Color;
import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewDataBoard extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	/**
	 * Create the frame.
	 */
	public NewDataBoard(CatalogEntry entryData) {
		setBounds(100, 100, 845, 664);
		
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255)); // Updated color
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(1, 0, 0, 0));
        
        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane);
        
        JPanel attributes = new JPanel();
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
        
        JLabel title = new JLabel("This is the title for something amazing");
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
        gbc_dataCustodianTxt.fill = GridBagConstraints.VERTICAL;
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
        gbc_dataCurrencyTxt.fill = GridBagConstraints.VERTICAL;
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
        gbc_datasetDescriptionTxt.fill = GridBagConstraints.VERTICAL;
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
        gbc_dataAccuracyTxt.fill = GridBagConstraints.VERTICAL;
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
        gbc_attributesTxt.fill = GridBagConstraints.VERTICAL;
        gbc_attributesTxt.gridx = 2;
        gbc_attributesTxt.gridy = 13;
        attributes.add(attributesTxt, gbc_attributesTxt);
        
        JLabel relaventDownload = new JLabel("Relavant Downloads");
        relaventDownload.setHorizontalAlignment(SwingConstants.LEFT);
        relaventDownload.setFont(new Font("Yu Gothic", Font.BOLD, 12));
        GridBagConstraints gbc_relaventDownload = new GridBagConstraints();
        gbc_relaventDownload.insets = new Insets(0, 0, 5, 0);
        gbc_relaventDownload.anchor = GridBagConstraints.WEST;
        gbc_relaventDownload.gridx = 2;
        gbc_relaventDownload.gridy = 14;
        attributes.add(relaventDownload, gbc_relaventDownload);
        
        JPanel relavantDownloadTxt = new JPanel();
        GridBagConstraints gbc_relavantDownloadTxt = new GridBagConstraints();
        gbc_relavantDownloadTxt.fill = GridBagConstraints.BOTH;
        gbc_relavantDownloadTxt.gridx = 2;
        gbc_relavantDownloadTxt.gridy = 15;
        attributes.add(relavantDownloadTxt, gbc_relavantDownloadTxt);
        relavantDownloadTxt.setLayout(new BoxLayout(relavantDownloadTxt, BoxLayout.Y_AXIS));
        relavantDownloadTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JToolBar toolBar = new JToolBar();
        toolBar.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        	}
        });
        toolBar.setBackground(new Color(255, 255, 230));
        scrollPane.setColumnHeaderView(toolBar);
        
        JLabel dataCatalogue = new JLabel("City of Windsor Open Data Catalogue");
        dataCatalogue.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 16));
        toolBar.add(dataCatalogue);
        
	}
	
	/*
	private void addRelavantDownload(FilePoint file) {
		JLabel newDownload = new JLabel();
		newDownload.setText(file);
	}
	*/
	

}
