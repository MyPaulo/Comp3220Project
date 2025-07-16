import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;


public class DatasetEntryPanel extends JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField titleField, descriptionField, dataCurrencyField, datasetDescriptionField, dataAccuracyField, attributesField;
    private JButton uploadButton, chooseFileButton, cancelButton;
    private JLabel filePathLabel;
    private File selectedFile;
    private Connection dbConnection;
    private JComboBox<String> categoryComboBox;
    private JTextField newCategoryField;
    private boolean isCreatingNewCategory = false;
    public DatasetEntryPanel(Frame owner, Connection conn, int userId, UIManagement uiManagement, String status) {
        super(owner, "Upload Dataset", true);
        this.dbConnection = conn;
        this.selectedFile = null;
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));  // Light background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // Spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font fieldFont = new Font("Arial", Font.PLAIN, 12);

        // Initialize input fields
        titleField = createTextField();
        descriptionField = createTextField();
        dataCurrencyField = createTextField();
        datasetDescriptionField = createTextField();
        dataAccuracyField = createTextField();
        attributesField = createTextField();
        filePathLabel = new JLabel("Selected file: None");

        // Title
        addLabel("Title:", labelFont, gbc, 0, 0);
        addField(titleField, fieldFont, gbc, 1, 0);

        // Description
        addLabel("Description:", labelFont, gbc, 0, 1);
        addField(descriptionField, fieldFont, gbc, 1, 1);

        // Data Currency
        addLabel("Data Currency:", labelFont, gbc, 0, 2);
        addField(dataCurrencyField, fieldFont, gbc, 1, 2);

        // Dataset Description
        addLabel("Dataset Description:", labelFont, gbc, 0, 3);
        addField(datasetDescriptionField, fieldFont, gbc, 1, 3);

        // Data Accuracy
        addLabel("Data Accuracy:", labelFont, gbc, 0, 4);
        addField(dataAccuracyField, fieldFont, gbc, 1, 4);
        
        // Attributes
        addLabel("Attributes:", labelFont, gbc, 0, 5);
        addField(attributesField, fieldFont, gbc, 1, 5);

        // Category Selection
        addLabel("Category:", labelFont, gbc, 0, 6);
        categoryComboBox = new JComboBox<>();
        loadCategories();
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCreatingNewCategory = categoryComboBox.getSelectedIndex() == categoryComboBox.getItemCount() - 1;
                newCategoryField.setEnabled(isCreatingNewCategory);
                setFieldsEnabled(isCreatingNewCategory);
            }
        });
        add(categoryComboBox, createFieldConstraints(gbc, 1, 6));

        // New category text field for user input
        addLabel("New Category (if applicable):", labelFont, gbc, 0, 7);
        newCategoryField = createTextField();
        newCategoryField.setEnabled(false);
        add(newCategoryField, createFieldConstraints(gbc, 1, 7));

        // File Path
        addLabel("File Path:", labelFont, gbc, 0, 8);
        add(filePathLabel, createFieldConstraints(gbc, 1, 8));

        // Choose file button
        chooseFileButton = new JButton("Choose File");
        styleButton(chooseFileButton, Color.DARK_GRAY, Color.WHITE);
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        add(chooseFileButton, createFieldConstraints(gbc, 1, 9));

        // Upload button
        uploadButton = new JButton("Upload Dataset");
        styleButton(uploadButton, new Color(0, 120, 215), Color.WHITE);
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get values from form
                String title = titleField.getText();
                String description = descriptionField.getText();
                String dataCurrency = dataCurrencyField.getText();
                String datasetDescription = datasetDescriptionField.getText();
                String dataAccuracy = dataAccuracyField.getText();
                String attributes = attributesField.getText();
                String category = isCreatingNewCategory ? newCategoryField.getText() : categoryComboBox.getSelectedItem().toString();

                // Validate fields
                if (isCreatingNewCategory && (title.isEmpty() || category.isEmpty() || selectedFile == null)) {
                    JOptionPane.showMessageDialog(null, "Please fill in the required fields and select a file.");
                    return;
                }

                // Validate file extension
                String fileName = selectedFile.getName().toLowerCase();
                if (!fileName.endsWith(".csv") && !fileName.endsWith(".xlsx") && !fileName.endsWith(".txt") && !fileName.endsWith(".xls")) {
                    JOptionPane.showMessageDialog(null, "Please select a file with a valid extension (.csv, .xlsx, .txt, .xls).");
                    return;
                }

                // Upload dataset entry
                boolean isAdmin = status.equals("approved");
                UploadManager.uploadDatasetEntry(dbConnection, title, description, dataCurrency, datasetDescription,
                        dataAccuracy, category, status, selectedFile, userId, attributes, uiManagement, isAdmin);
                // Close the dialog and return to the appropriate dashboard
                dispose();
                if (isAdmin) {
                    uiManagement.showAdminDashboard();
                } else {
                    uiManagement.showContributorDashboard();
                }
            }
        });
        add(uploadButton, createFieldConstraints(gbc, 1, 10));

        // Cancel button
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(220, 53, 69), Color.WHITE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (status.equals("approved")) {
                    uiManagement.showAdminDashboard();
                } else {
                    uiManagement.showContributorDashboard();
                }
            }
        });
        add(cancelButton, createFieldConstraints(gbc, 1, 11));

        pack();
        setLocationRelativeTo(owner);
    }

    private void addLabel(String text, Font font, GridBagConstraints gbc, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(100, 100, 100));
        gbc.gridx = x;
        gbc.gridy = y;
        add(label, gbc);
    }

    private void addField(JComponent field, Font font, GridBagConstraints gbc, int x, int y) {
        field.setFont(font);
        gbc.gridx = x;
        gbc.gridy = y;
        add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 25));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    private GridBagConstraints createFieldConstraints(GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1;
        gbc.weighty = 0;
        return gbc;
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
    
    private void loadCategories() {
        File dataDir = new File(System.getProperty("user.dir") + "/data/");
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] subDirs = dataDir.listFiles(File::isDirectory);
            if (subDirs != null) {
                for (File subDir : subDirs) {
                    categoryComboBox.addItem(subDir.getName());
                }
            }
        }
        categoryComboBox.addItem("Create New Category");
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();

        // Create a file filter to allow only .csv, .xlsx, .xls, and .txt files
        javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
            "CSV, XLSX, XLS, TXT Files", "csv", "xlsx", "xls", "txt");
        fileChooser.setFileFilter(filter);

        // Show the file chooser and check if a file is selected
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
        }
    }

    private void setFieldsEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        descriptionField.setEnabled(enabled);
        dataCurrencyField.setEnabled(enabled);
        datasetDescriptionField.setEnabled(enabled);
        dataAccuracyField.setEnabled(enabled);
        attributesField.setEnabled(enabled);
    }
}
    
    