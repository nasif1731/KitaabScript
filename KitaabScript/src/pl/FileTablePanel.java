package pl;

import bll.FileBO;
import dto.FileDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class FileTablePanel extends JPanel {
    private JTable fileTable;
    private FileBO fileBO;
    private DefaultTableModel tableModel;
    private FileManagementController controller;

    public FileTablePanel(FileManagementController controller) {
        this.controller = controller;
        this.fileBO = new FileBO(); 
        initializeUI();
        loadFiles();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199)); 

        tableModel = new DefaultTableModel(new Object[]{"Filename", "Last Modified"}, 0);
        fileTable = new JTable(tableModel);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTable.setBackground(Color.WHITE);
        fileTable.setForeground(Color.BLACK);
        fileTable.getTableHeader().setBackground(new Color(235, 224, 199));

        JScrollPane scrollPane = new JScrollPane(fileTable);
        scrollPane.setBackground(new Color(235, 224, 199)); 
        add(scrollPane, BorderLayout.CENTER);

        fileTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
                    int selectedRow = fileTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);
                        controller.loadFileDetail(selectedFileName);
                    }
                }
            }
        });
    }

    private void loadFiles() {
        List<FileDTO> fileList = fileBO.getAllFiles(); 
        for (FileDTO fileDTO : fileList) {
            tableModel.addRow(new Object[]{fileDTO.getFilename(), fileDTO.getUpdatedAt()}); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("File Management System");
            FileManagementController controller = new FileManagementController();
            FileTablePanel fileTablePanel = new FileTablePanel(controller);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(fileTablePanel);
            frame.setSize(600, 400);  
            frame.setLocationRelativeTo(null);  
            frame.setVisible(true); 
        });
    }
}
