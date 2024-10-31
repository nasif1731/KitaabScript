package pl;

import bll.FilePaginationBO;
import bll.FileBO;
import dto.FileDTO;
import dto.PageDTO;

import javax.swing.*;
import java.awt.*;

public class FileDetailPanel extends JFrame {
    private JTextPane fileContentArea;
    private JButton updateButton;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel wordCountLabel;
    private JLabel pageLabel;
    private FilePaginationBO filePaginationBO;
    private FileBO fileBO;
    private int currentPage = 1;
    private FileDTO fileDTO; 
    private int totalPages = 0;

    public FileDetailPanel(String fileName, FilePaginationBO filePaginationBO, FileBO fileBO) {
        this.filePaginationBO = filePaginationBO;
        this.fileBO = fileBO;
        initializeUI();
        loadFileDetails(fileName);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199));

        fileContentArea = new JTextPane();
        fileContentArea.setEditable(false);
        fileContentArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        add(new JScrollPane(fileContentArea), BorderLayout.CENTER);

        updateButton = new JButton("Update");
        updateButton.setBackground(new Color(138, 83, 43));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> loadWriteMode());

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(updateButton, BorderLayout.WEST);
        buttonPanel.setBackground(new Color(235, 224, 199));

        prevButton = new JButton("← Previous");
        prevButton.setBackground(new Color(138, 83, 43));
        prevButton.setForeground(Color.WHITE);
        prevButton.addActionListener(e -> pageNavigation(currentPage - 1));

        nextButton = new JButton("Next →");
        nextButton.setBackground(new Color(138, 83, 43));
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(e -> pageNavigation(currentPage + 1));

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.setBackground(new Color(235, 224, 199));
        buttonPanel.add(navigationPanel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.NORTH);

        wordCountLabel = new JLabel("Word Count: 0");
        wordCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(wordCountLabel, BorderLayout.SOUTH);
        pageLabel = new JLabel("Page " + currentPage + " of " + totalPages);
        pageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(pageLabel, BorderLayout.WEST);
        bottomPanel.add(wordCountLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadFileDetails(String fileName) {
        try {
            fileDTO = fileBO.getOneFile(fileName); 
            if (fileDTO != null) {
                displayPage(currentPage);
                int wordCount = fileBO.getWordCount(fileName); 
                wordCountLabel.setText("Word Count: " + wordCount);
               totalPages = filePaginationBO.getTotalPages(fileDTO.getId()); 
                updatePageLabel();
            } else {
                JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading file details: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPage(int pageNumber) {
        PageDTO page = filePaginationBO.getPageContent(fileDTO.getId(), pageNumber);
        if (page != null) {
            currentPage = pageNumber;
            fileContentArea.setText(page.getPageContent());
        } else {
            JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void pageNavigation(int pageNumber) {
        if (pageNumber < 1) {
            JOptionPane.showMessageDialog(this, "You are on the first page.", "Navigation", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        displayPage(pageNumber);
    }
    private void updatePageLabel() {
        pageLabel.setText("Page " + currentPage + " of " + totalPages);
    }

    void loadWriteMode() {
        dispose();
        FileUpdatePanel dialog = new FileUpdatePanel(fileDTO.getFilename(), fileBO,filePaginationBO);
        dialog.setVisible(true);
    }
}