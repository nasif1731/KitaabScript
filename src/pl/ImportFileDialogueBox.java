package pl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import bll.FileImportBO;

public class ImportFileDialogueBox extends JDialog {

    private FileImportBO fileImportBO;
    private JTextArea resultArea;
    private JButton importButton;

    public ImportFileDialogueBox(Frame parent,FileImportBO fileImportBO) {
        super(parent, "Import File", true);
        this.fileImportBO = fileImportBO;
        ImageIcon icon = new ImageIcon("resources/images/icon.png");
        setIconImage(icon.getImage());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(235, 224, 199));
        
        Font mughalFont = new Font("Serif", Font.BOLD, 24); 

        // Result area setup
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(255, 244, 206)); 
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(138, 83, 43), 2)); 
        JScrollPane scrollPane = new JScrollPane(resultArea);
        
        
        importButton = createStyledButton("Import", mughalFont);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(235, 224, 199)); 
        buttonPanel.add(importButton);

       
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileImport();
            }
        });

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void handleFileImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles.length == 0) {
                JOptionPane.showMessageDialog(this, "Please select a file or files to import.", "No File Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                if (selectedFiles.length == 1) {
                    String importResult = fileImportBO.importFile(selectedFiles[0].getAbsolutePath());
                    resultArea.append(importResult + "\n");
                } else {
                    List<String> filePaths = List.of(selectedFiles).stream()
                            .map(File::getAbsolutePath)
                            .toList();
                    List<String> results = fileImportBO.bulkImportFiles(filePaths);
                    results.forEach(resultArea::append);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error importing file(s): " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(138, 83, 43)); 
        button.setForeground(new Color(255, 244, 206)); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(138, 83, 43), 2)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JFrame frame = new JFrame();
//                ImportFileDialogueBox dialog = new ImportFileDialogueBox(frame);
//                dialog.setVisible(true);
//            }
//        });
//    }
}
