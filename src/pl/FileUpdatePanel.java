package pl;

import bll.FileBO;
import bll.FilePaginationBO;
import dto.FileDTO;
import dto.PageDTO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FileUpdatePanel extends JFrame {
	private JTextArea fileContentArea;
	private JButton saveButton;
	private JLabel lastModifiedLabel;
	private JLabel wordCountLabel;
	private FileBO fileBO;
	private FilePaginationBO filePaginationBO;
	private String currentFileName;
	   FileDTO fileDTO;

	public FileUpdatePanel(String fileName, FileBO fileBO,FilePaginationBO filePaginationBO) {
        this.currentFileName = fileName;
        this.fileBO = fileBO;
        this.filePaginationBO = filePaginationBO;
        initializeUI();
        loadFileContent();
        updateWordCount();
    }

	private void initializeUI() {
		setTitle("File Update - " + currentFileName);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

		getContentPane().setBackground(new Color(235, 224, 199));

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(235, 224, 199));

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(new Color(235, 224, 199));

		lastModifiedLabel = new JLabel("Last Modified At: ");
		lastModifiedLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		lastModifiedLabel.setForeground(new Color(138, 83, 43));
		headerPanel.add(lastModifiedLabel);
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		
		Font mughalFont = new Font("Serif", Font.PLAIN, 16);
		fileContentArea = new JTextArea(20, 50);
		fileContentArea.setFont(mughalFont);
		fileContentArea.setBorder(BorderFactory.createLineBorder(new Color(138, 83, 43), 2));
		JScrollPane scrollPane = new JScrollPane(fileContentArea);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		wordCountLabel = new JLabel("Word Count: 0");
		wordCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		wordCountLabel.setFont(new Font("Serif", Font.PLAIN, 16));
		wordCountLabel.setForeground(new Color(138, 83, 43));
		mainPanel.add(wordCountLabel, BorderLayout.SOUTH);

		
		saveButton = createStyledButton("Save", mughalFont);
		saveButton.setPreferredSize(new Dimension(120, 40));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(235, 224, 199));
		buttonPanel.add(saveButton);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (saveFile()) {
					navigateToFileTable();
				}
			}
		});

		fileContentArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateWordCount();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateWordCount();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateWordCount();
			}
		});

		add(mainPanel);
		setVisible(true);
	}


	private void loadFileContent() {
	    try {
	        FileDTO fileDTO = fileBO.getOneFile(currentFileName);
	        if (fileDTO != null) {
	            PageDTO page = filePaginationBO.getPageContent(fileDTO.getId(), 1); 
	                fileContentArea.setText(page.getPageContent());
	                lastModifiedLabel.setText("Last Modified At: " + fileDTO.getUpdatedAt());
	                updateWordCount();
	        }
	    }
	     catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error loading file content: " + e.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}


    private void displayPage(int pageNumber) {
        int currentPage = 1;
		PageDTO page = filePaginationBO.getPageContent(fileDTO.getId(), pageNumber); 
        
            currentPage = pageNumber;
            fileContentArea.setText(page.getPageContent());
            //totalPages = page.getTotalPages(); 
//        } else {
//            JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation", JOptionPane.INFORMATION_MESSAGE);
//        }
    }

	
	private boolean saveFile() {
		try {
			String updatedContent = fileContentArea.getText();
			fileBO.updateFile(updatedContent, updatedContent); 
			JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			return true; // Indicate successful save
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private void updateWordCount() {
		int wordCount = fileBO.getWordCount(currentFileName);
		wordCountLabel.setText("Word Count: " + wordCount);
	}

	private JButton createStyledButton(String text, Font font) {
		JButton button = new JButton(text);
		button.setFont(font);
		button.setBackground(new Color(138, 83, 43));
		button.setForeground(Color.WHITE);
		return button;
	}

	private void navigateToFileTable() {
		dispose();
		FileTablePanel fileTablePanel = new FileTablePanel(fileBO,filePaginationBO);
		fileTablePanel.setVisible(true);
	}
}
