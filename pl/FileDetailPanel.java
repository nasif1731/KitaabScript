package pl;

import bll.IBLFacade;
import dto.FileDTO;
import dto.PageDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileDetailPanel extends JFrame {
    private JTextPane fileContentArea;
    private JButton updateButton;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel wordCountLabel;
    private JLabel pageLabel;
    private IBLFacade blFacade;
    private int currentPage = 1;
    private FileDTO fileDTO;
    private String filename;
    private int totalPages = 0;
    private int pageId;
    private TransliterationPanel transliterationPanel;


    public FileDetailPanel(String fileName, IBLFacade blFacade) {
        this.blFacade = blFacade;
        this.filename = fileName;
        this.fileDTO = blFacade.getOneFile(filename);
        if (fileDTO == null) {
            JOptionPane.showMessageDialog(this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        initializeUI();
        loadFileDetails(fileName);
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199));
        ImageIcon icon = new ImageIcon("resources/images/icon.png");
        setIconImage(icon.getImage());
        this.setSize(500,500);
        fileContentArea = new JTextPane();
        fileContentArea.setEditable(false);
        add(new JScrollPane(fileContentArea), BorderLayout.CENTER);
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem transliterateItem = new JMenuItem("Transliterate");
        transliterateItem.addActionListener(e -> showTransliterationSidebar());

        contextMenu.add(transliterateItem);
        fileContentArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) contextMenu.show(fileContentArea, e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) contextMenu.show(fileContentArea, e.getX(), e.getY());
            }
        });

        pageId = blFacade.getPageID(fileDTO.getId(), currentPage);
        transliterationPanel = new TransliterationPanel(blFacade, pageId, currentPage);
        transliterationPanel.setVisible(false);
        add(transliterationPanel, BorderLayout.EAST);

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

    private void showTransliterationSidebar() {
        String selectedText = fileContentArea.getSelectedText();
        if (selectedText != null) {
        	transliterationPanel.textPane.setText(selectedText);
            transliterationPanel.performTransliteration(selectedText); 
            transliterationPanel.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select text for transliteration.", "No Text Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    void loadFileDetails(String fileName) {
        try {
            displayPage(currentPage);
            int wordCount = blFacade.getWordCount(fileName); 
            wordCountLabel.setText("Word Count: " + wordCount);
            totalPages = blFacade.getTotalPages(fileDTO.getId());
            updatePageLabel();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading file details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPage(int pageNumber) {
        PageDTO page = blFacade.getPageContent(fileDTO.getId(), pageNumber);
        if (page != null) {
            currentPage = pageNumber;
            String pageContent = page.getPageContent();
            setLanguageOrientation(pageContent);
            fileContentArea.setText(pageContent);

            pageId = page.getPageId(); 

            pageId = blFacade.getPageID(fileDTO.getId(), currentPage);

            updatePageLabel();
            blFacade.saveTransliterationIfNotExists(pageId, page.getPageContent());
        } else {
            JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void pageNavigation(int pageNumber) {
        if (pageNumber < 1 || pageNumber > totalPages) {
            JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        displayPage(pageNumber);
    }

    private void updatePageLabel() {
        pageLabel.setText("Page " + currentPage + " of " + totalPages);
    }

    void loadWriteMode() {
        dispose();
        FileUpdatePanel dialog = new FileUpdatePanel(fileDTO.getFilename(), blFacade);
        dialog.setVisible(true);
    }

    private void setLanguageOrientation(String content) {
        if (fileContentArea == null) return;

        boolean isUrdu = content.codePoints().anyMatch(
                c -> (c >= 0x0600 && c <= 0x06FF) || (c >= 0x0750 && c <= 0x077F));
        fileContentArea.setComponentOrientation(isUrdu ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
    }
}
