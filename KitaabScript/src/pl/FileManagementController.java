package pl;

import bll.FileBO;
import dto.FileDTO;
import javax.swing.*;
import java.sql.SQLException;

public class FileManagementController {
    private FileBO fileBO;

    public FileManagementController() {
        fileBO = new FileBO();
    }

    public void loadFileDetail(String fileName) {
        try {
           
            FileDTO fileDTO = fileBO.getOneFile(fileName);
            if (fileDTO != null) {
         
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(new JPanel());
                FileDetailPanel fileDetailPanel = new FileDetailPanel(this, fileDTO); 
                JDialog dialog = new JDialog(parentFrame, fileName, true);
                dialog.add(fileDetailPanel);
                dialog.setSize(500, 400);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Error loading file details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getWordCount(String fileName) {
        return fileBO.getWordCount(fileName); 
    }
}
