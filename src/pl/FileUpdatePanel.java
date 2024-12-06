package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import org.apache.logging.log4j.*;

import bll.IBLFacade;
import dto.FileDTO;
import dto.PageDTO;

public class FileUpdatePanel extends JFrame {

	
	private static final long serialVersionUID = 1L;
private static final Logger logger = LogManager.getLogger(FileUpdatePanel.class);
	private JTextArea fileContentArea;
	private JButton saveButton;
	private JButton prevButton;
	private JButton nextButton;
	private JToggleButton autoSaveToggle;
	private JLabel lastModifiedLabel;
	private JLabel wordCountLabel;
	private JLabel pageLabel;
	private IBLFacade blFacade;
	private String currentFileName;
	private FileDTO fileDTO;
	private int currentPage = 1;
	private int totalPages = 0;
	 private ScheduledExecutorService autosaveExecutor;
	 private boolean isAutosaveEnabled = false;
	public FileUpdatePanel(String fileName, IBLFacade blFacade) {
		this.currentFileName = fileName;
		this.blFacade = blFacade;
		initializeUI();
		loadFileContent();
		updateWordCount();
		initializeAutosave();
	}



	private void initializeUI() {
		setTitle("File Update - " + currentFileName);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
		ImageIcon icon = new ImageIcon("resources/images/icon.png");
        setIconImage(icon.getImage());

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

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(new Color(235, 224, 199));

		wordCountLabel = new JLabel("Word Count: 0");
		wordCountLabel.setForeground(new Color(138, 83, 43));
		bottomPanel.add(wordCountLabel, BorderLayout.EAST);

		setPrevButton(createStyledButton("← Previous", mughalFont));
		setNextButton(createStyledButton("Next →", mughalFont));

		pageLabel = new JLabel("Page " + currentPage + " of " + totalPages);
		pageLabel.setForeground(new Color(138, 83, 43));
		bottomPanel.add(pageLabel, BorderLayout.WEST);

		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		saveButton = createStyledButton("Save", mughalFont);
		saveButton.setPreferredSize(new Dimension(120, 40));

	 prevButton = createStyledButton("← Previous", mughalFont);
		 nextButton = createStyledButton("Next →", mughalFont);
		prevButton.addActionListener(e -> navigatePage(currentPage - 1));
		nextButton.addActionListener(e -> navigatePage(currentPage + 1));
		autoSaveToggle = new JToggleButton("Enable Autosave");
        autoSaveToggle.setFont(mughalFont);
        autoSaveToggle.setBackground(new Color(138, 83, 43));
        autoSaveToggle.setForeground(Color.WHITE);
        autoSaveToggle.addActionListener(e -> toggleAutosave(autoSaveToggle.isSelected()));
        
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlPanel.setBackground(new Color(235, 224, 199));
		controlPanel.add(prevButton);
		controlPanel.add(saveButton);
		controlPanel.add(nextButton);
		controlPanel.add(autoSaveToggle);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);

   
		saveButton.addActionListener(e -> {
			if (saveFile()) {
//				navigateToFileDetailPanel();
				navigateToFileTable();
			}
		});

		add(mainPanel);
		setVisible(true);
	}

	private void initializeAutosave() {
        autosaveExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private void toggleAutosave(boolean enable) {
        isAutosaveEnabled = enable;
        if (enable) {
            autoSaveToggle.setText("Disable Autosave");
            startAutosave();
        } else {
            autoSaveToggle.setText("Enable Autosave");
            stopAutosave();
        }
    }

    private void startAutosave() {
        autosaveExecutor.scheduleAtFixedRate(() -> {
            if (isAutosaveEnabled) {
                saveFileSilently();
            }
        }, 0, 5, TimeUnit.MINUTES); 
    }

    private void stopAutosave() {
        autosaveExecutor.shutdownNow();
        initializeAutosave(); 
    }

    private boolean saveFileSilently() {
        try {
            String updatedContent = fileContentArea.getText();
            blFacade.updateFile(currentFileName, currentPage, updatedContent);
//            System.out.println("Autosaved successfully at " + System.currentTimeMillis());
            return true;
        } catch (Exception e) {
//            System.err.println("Autosave failed: " + e.getMessage());
            return false;
        }
    }




	private void updateWordCount() {
        int wordCount = blFacade.getWordCount(currentFileName);
        wordCountLabel.setText("Word Count: " + wordCount);
    }

	private void loadFileContent() {
		try {
			logger.info("Loading content for file: {}", currentFileName);
			fileDTO = blFacade.getOneFile(currentFileName);
			if (fileDTO != null) {
				PageDTO page = blFacade.getPageContent(fileDTO.getId(), currentPage);
				if (page != null) {
					setLanguageOrientation(page.getPageContent());
					lastModifiedLabel.setText("Last Modified At: " + fileDTO.getUpdatedAt());
					displayPage(currentPage);
					totalPages = blFacade.getTotalPages(fileDTO.getId());
					updatePageLabel();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error loading file content: {}", e.getMessage(), e);
			JOptionPane.showMessageDialog(this, "Error loading file content: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void setLanguageOrientation(String content) {
		boolean isUrdu = content.codePoints()
				.anyMatch(c -> (c >= 0x0600 && c <= 0x06FF) || (c >= 0x0750 && c <= 0x077F));
		fileContentArea.setComponentOrientation(
				isUrdu ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
	}


	private void updatePageLabel() {
		pageLabel.setText("Page " + currentPage + " of " + totalPages);
	}

	private boolean saveFile() {
		try {
			String updatedContent = fileContentArea.getText();
			blFacade.updateFile(currentFileName, currentPage, updatedContent);
			JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
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
		FileTablePanel fileTablePanel = new FileTablePanel(blFacade);
		fileTablePanel.setVisible(true);
	}

	private void navigatePage(int pageNumber) {
		if (pageNumber < 1 || pageNumber > totalPages) {
			JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		currentPage = pageNumber;
		displayPage(currentPage);
	}

	private void displayPage(int pageNumber) {
		try {
			PageDTO page = blFacade.getPageContent(fileDTO.getId(), pageNumber);
			if (page != null) {
				setLanguageOrientation(page.getPageContent());
				fileContentArea.setText(page.getPageContent());
				lastModifiedLabel.setText("Last Modified At: " + fileDTO.getUpdatedAt());
				updatePageLabel();
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading page: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}



	public JButton getPrevButton() {
		return prevButton;
	}



	public void setPrevButton(JButton prevButton) {
		this.prevButton = prevButton;
	}



	public JButton getNextButton() {
		return nextButton;
	}



	public void setNextButton(JButton nextButton) {
		this.nextButton = nextButton;
	}
}


