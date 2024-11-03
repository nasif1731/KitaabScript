package pl;

import bll.FileBO;
import bll.FilePaginationBO;
import bll.IBLFacade;
import dto.FileDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class FileTablePanel extends JPanel { 
    private JTable fileTable;
    private IBLFacade blFacade;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
	private FilePaginationBO filePaginationBO;
	private FileBO fileBO;

    
    private static final Dimension BUTTON_SIZE = new Dimension(100, 30);
    private static final Color BACKGROUND_COLOR = new Color(235, 224, 199);
    private static final Color BUTTON_COLOR = new Color(138, 83, 43);
    private static final Color TABLE_HEADER_COLOR = BACKGROUND_COLOR;
    public FileTablePanel(IBLFacade blFacade) {
        this.blFacade = blFacade;
        initializeUI();
        loadFiles();
    }

    public FileTablePanel(FileBO fileBO, FilePaginationBO filePaginationBO) {
		this.fileBO = fileBO;
		this.filePaginationBO = filePaginationBO;
	}

	private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(BUTTON_SIZE);
        deleteButton.setBackground(BACKGROUND_COLOR);
        deleteButton.setForeground(BUTTON_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BUTTON_COLOR);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Filename", "Last Modified"}, 0);
        fileTable = new JTable(tableModel);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileTable.setBackground(Color.WHITE);
        fileTable.setForeground(Color.BLACK);
        fileTable.getTableHeader().setBackground(TABLE_HEADER_COLOR);

        JScrollPane scrollPane = new JScrollPane(fileTable);
        add(scrollPane, BorderLayout.CENTER);

        
        fileTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = fileTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);
                        openFileDetailPanel(selectedFileName);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = fileTable.getSelectedRow();
                if (selectedRow != -1) {
                    String selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);

                   
                    int confirm = JOptionPane.showConfirmDialog(
                            FileTablePanel.this,
                            "Are you sure you want to delete the file: " + selectedFileName + "?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            
                        	blFacade.deleteFile(selectedFileName);

                            
                            tableModel.removeRow(selectedRow);

                            JOptionPane.showMessageDialog(
                                    FileTablePanel.this,
                                    "File '" + selectedFileName + "' deleted successfully!",
                                    "Deletion Successful",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                    FileTablePanel.this,
                                    "Error deleting file: " + ex.getMessage(),
                                    "Deletion Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            FileTablePanel.this,
                            "Please select a file to delete.",
                            "No File Selected",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }

    private void loadFiles() {
        List<FileDTO> fileList = blFacade.getAllFiles();
        for (FileDTO fileDTO : fileList) {
            tableModel.addRow(new Object[]{fileDTO.getFilename(), fileDTO.getUpdatedAt()});
        }
    }

    private void openFileDetailPanel(String fileName) {
        
        FileDetailPanel fileDetailPanel = new FileDetailPanel(fileName,blFacade);
        fileDetailPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        fileDetailPanel.setSize(600, 400); 
        fileDetailPanel.setLocationRelativeTo(null); 
        fileDetailPanel.setVisible(true); 
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("File Management System");
//            FileBO fileBO = new FileBO(); 
//            FileTablePanel fileTablePanel = new FileTablePanel(fileBO);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
//            frame.setContentPane(fileTablePanel); 
//            frame.setSize(600, 400);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}
