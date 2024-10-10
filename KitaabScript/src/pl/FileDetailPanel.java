package pl;

import dto.FileDTO;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;

public class FileDetailPanel extends JPanel {
    private JTextPane fileContentArea;
    private JButton updateButton;
    private FileManagementController controller;
    private FileDTO fileDTO; 
    private JLabel wordCountLabel;

    public FileDetailPanel(FileManagementController controller, FileDTO fileDTO) {
        this.controller = controller;
        this.fileDTO = fileDTO; 
        initializeUI();
        loadFile(); 
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199));

        fileContentArea = new JTextPane();
        fileContentArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(fileContentArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        updateButton = new JButton("Update");
        updateButton.setBackground(new Color(138, 83, 43));
        updateButton.setForeground(Color.WHITE);
        
        buttonPanel.add(updateButton, BorderLayout.WEST); 
        buttonPanel.setBackground(new Color(235, 224, 199));

        add(buttonPanel, BorderLayout.NORTH);

        wordCountLabel = new JLabel("Word Count: 0");
        wordCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(wordCountLabel, BorderLayout.SOUTH);
    }

    public void loadFile() {
        StyledDocument doc = fileContentArea.getStyledDocument();
        SimpleAttributeSet rightAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
        doc.setParagraphAttributes(0, doc.getLength(), rightAlign, false);
        fileContentArea.setText(fileDTO.getContent()); 
        int wordCount = controller.getWordCount(fileDTO.getFilename()); 
        wordCountLabel.setText("Word Count: " + wordCount);
    }
}
