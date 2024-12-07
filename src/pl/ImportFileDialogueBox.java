package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import interfaces.IBLFacade;

public class ImportFileDialogueBox extends JDialog {

    /**
	 * 
	 */

	private static final long serialVersionUID = 1841635305535824823L;
private static final Logger logger = LogManager.getLogger(ImportFileDialogueBox.class);
	private IBLFacade blFacade;
    private JTextArea resultArea;
    private JButton importButton;

    public ImportFileDialogueBox(Frame parent,IBLFacade blFacade) {
        super(parent, "Import File", true);
        this.blFacade = blFacade;
        ImageIcon icon = new ImageIcon("resources/images/icon.png");
        setIconImage(icon.getImage());
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(235, 224, 199));
        
        Font mughalFont = new Font("Serif", Font.BOLD, 24); 

        
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
        logger.info("File import process started.");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();

            if (selectedFiles.length == 0) {
                logger.warn("No files selected for import.");

                JOptionPane.showMessageDialog(this, "Please select a file or files to import.", 
                    "No File Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Thread importThread = new Thread(() -> {
                List<String> filePaths = List.of(selectedFiles).stream()
                        .map(File::getAbsolutePath)
                        .toList();

                try {
                    List<String> results = blFacade.bulkImportFiles(filePaths); // Declare inside try block

                    SwingUtilities.invokeLater(() -> {
                        for (String message : results) {
                            resultArea.append(message + "\n");
                        }
                        logger.info("File import completed successfully.");

                        JOptionPane.showMessageDialog(ImportFileDialogueBox.this,
                                "File import completed.", "Completed", JOptionPane.INFORMATION_MESSAGE);
                    });
                } catch (RemoteException e) {
                    logger.error("Error during file import.", e);
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ImportFileDialogueBox.this,
                                "File import failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            });

            importThread.start();
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
