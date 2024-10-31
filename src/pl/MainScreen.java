package pl;

import javax.swing.*;

import bll.FileBO;
import bll.FileImportBO;
import bll.FilePaginationBO;
import dal.AbstractDALFactory;
import dal.DALFacade;
import dal.FileDAO;
import dal.FileImportDAO;
import dal.IFileDAO;
import dal.IFileImportDAO;
import dal.IPaginationDAO;
import dal.IDALFacade;
import dal.IDALFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainScreen extends JFrame {

	private JButton openFileButton;
	private JButton createFileButton;
	private JButton importFileButton;
	private FileBO fileBO;
	private FileImportBO fileImportBO;
	private FilePaginationBO filePaginationBO;

    public MainScreen(FileBO fileBO,FileImportBO fileImportBO,FilePaginationBO filePaginationBO) {
        this.fileBO = fileBO;
        this.fileImportBO=fileImportBO;
        this.filePaginationBO = filePaginationBO;
		setTitle("Kitaab Script");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		getContentPane().setBackground(new Color(235, 224, 199));
		ImageIcon icon = new ImageIcon("resources/images/icon.png");
		setIconImage(icon.getImage());
		Font mughalFont = new Font("Serif", Font.BOLD, 24);
		Font gothicFont = loadGothicFont(55f);

		JLabel headerLabel = new JLabel("Kitaab Script");
		headerLabel.setFont(gothicFont);
		headerLabel.setForeground(new Color(138, 83, 43));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(235, 224, 199));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

		openFileButton = createStyledButton("Open File", mughalFont);
		createFileButton = createStyledButton("Create File", mughalFont);
		importFileButton = createStyledButton("Import File", mughalFont);

		buttonPanel.add(openFileButton);
		buttonPanel.add(createFileButton);
		buttonPanel.add(importFileButton);

		JPanel paddedPanel = new JPanel(new BorderLayout());
		paddedPanel.setBackground(new Color(235, 224, 199));
		paddedPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
		paddedPanel.add(buttonPanel, BorderLayout.CENTER);

		importFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImportFileDialogueBox dialog = new ImportFileDialogueBox(MainScreen.this,fileImportBO);
				dialog.setVisible(true);
			}
		});
		createFileButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        String fileName = JOptionPane.showInputDialog(MainScreen.this,
		                "Enter the name of the file (without extension):");

		        if (fileName == null) {
		            return; 
		        }

		        if (fileName.trim().isEmpty()) {
		            JOptionPane.showMessageDialog(MainScreen.this, "File name cannot be empty!");
		            return; 
		        }

		        if (!fileName.toLowerCase().endsWith(".txt")) {
		            fileName += ".txt";
		        }

		        try {
		            
		            fileBO.createFile(fileName, ""); 

		            MainScreen.this.setVisible(true);

		            FileUpdatePanel fileUpdatePanel = new FileUpdatePanel(fileName,fileBO,filePaginationBO);

		            fileUpdatePanel.addWindowListener(new java.awt.event.WindowAdapter() {
		                @Override
		                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		                    MainScreen.this.setVisible(true);
		                }
		            });

		            fileUpdatePanel.setVisible(true);

		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(MainScreen.this, "Error creating file: " + ex.getMessage());
		        }
		    }
		});


		openFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFrame frame = new JFrame("Files in DB");
				FileTablePanel fileTablePanel = new FileTablePanel(fileBO,filePaginationBO);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.add(fileTablePanel);
				frame.setSize(600, 400);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

		getContentPane().add(headerLabel, BorderLayout.NORTH);
		getContentPane().add(paddedPanel, BorderLayout.CENTER);
	}

	private JButton createStyledButton(String text, Font font) {
		JButton button = new JButton(text);
		button.setFont(font);
		button.setPreferredSize(new Dimension(200, 80));
		button.setBackground(new Color(138, 83, 43));
		button.setForeground(new Color(255, 244, 206));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(new Color(138, 83, 43), 2));
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return button;
	}

	private Font loadGothicFont(float size) {
		try {
			File fontFile = new File("resources/fonts/OldLondon.ttf");
			Font gothicFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
			return gothicFont.deriveFont(size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return new Font("Serif", Font.BOLD, 40);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				IDALFactory dalFactory = AbstractDALFactory.getInstance();
                IFileDAO fileDAO = dalFactory.getFileDAO();
                IFileImportDAO fileImportDAO = dalFactory.getFileImportDAO();
                IPaginationDAO paginationDAO = dalFactory.getPaginationDAO();
                IDALFacade dalFacade = new DALFacade(fileDAO, fileImportDAO, paginationDAO); 
                FileBO fileBO = new FileBO(dalFacade);
                FileImportBO fileImportBO = new FileImportBO(dalFacade);
                FilePaginationBO filePaginationBO = new FilePaginationBO(dalFacade);
                MainScreen mainFrame = new MainScreen(fileBO, fileImportBO, filePaginationBO);
                mainFrame.setVisible(true);
			}
		});
	}
}
