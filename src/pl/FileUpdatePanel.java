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
    private JLabel pageLabel;
    private FileBO fileBO;
    private FilePaginationBO filePaginationBO;
    private String currentFileName;
    private FileDTO fileDTO;
    private int currentPage = 1;
    private int totalPages = 0;

    public FileUpdatePanel(String fileName, FileBO fileBO, FilePaginationBO filePaginationBO) {
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

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(235, 224, 199));

        wordCountLabel = new JLabel("Word Count: 0");
        wordCountLabel.setForeground(new Color(138, 83, 43));
        bottomPanel.add(wordCountLabel, BorderLayout.EAST);

        pageLabel = new JLabel("Page " + currentPage + " of " + totalPages);
        pageLabel.setForeground(new Color(138, 83, 43));
        bottomPanel.add(pageLabel, BorderLayout.WEST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        saveButton = createStyledButton("Save", mughalFont);
        saveButton.setPreferredSize(new Dimension(120, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(235, 224, 199));
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            if (saveFile()) {
                navigateToFileTable();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private void loadFileContent() {
        try {
            fileDTO = fileBO.getOneFile(currentFileName);
            if (fileDTO != null) {
                PageDTO page = filePaginationBO.getPageContent(fileDTO.getId(), currentPage);
                if (page != null) {
                    setLanguageOrientation(page.getPageContent());
                    fileContentArea.setText(page.getPageContent());
                    lastModifiedLabel.setText("Last Modified At: " + fileDTO.getUpdatedAt());
                    totalPages = filePaginationBO.getTotalPages(fileDTO.getId());
                    updatePageLabel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading file content: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setLanguageOrientation(String content) {
        boolean isUrdu = content.codePoints().anyMatch(
                c -> (c >= 0x0600 && c <= 0x06FF) || (c >= 0x0750 && c <= 0x077F));
        fileContentArea.setComponentOrientation(isUrdu ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
    }

    private void updateWordCount() {
        int wordCount = fileBO.getWordCount(currentFileName);
        wordCountLabel.setText("Word Count: " + wordCount);
    }

    private void updatePageLabel() {
        pageLabel.setText("Page " + currentPage + " of " + totalPages);
    }

    private boolean saveFile() {
        try {
            String updatedContent = fileContentArea.getText();
            fileBO.updateFile(currentFileName, updatedContent);
            JOptionPane.showMessageDialog(this, "File saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        FileTablePanel fileTablePanel = new FileTablePanel(fileBO, filePaginationBO);
        fileTablePanel.setVisible(true);
    }
}
