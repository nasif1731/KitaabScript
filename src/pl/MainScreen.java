package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import bll.BLFacade;
import bll.FileBO;
import bll.FileImportBO;
import bll.FilePaginationBO;
import bll.IBLFacade;
import bll.IFileBO;
import bll.IFileImportBO;
import bll.IFilePaginationBO;
import bll.IKLAnalysisBO;
import bll.ILemmatizationBO;
import bll.IPMIAnalysisBO;
import bll.IPOSTaggingBO;
import bll.ISearchResultBO;
import bll.ITFIDFAnalysisBO;
import bll.ITransliterationBO;
import bll.KLAnalysisBO;
import bll.LemmatizationBO;
import bll.PMIAnalysisBO;
import bll.POSTaggingBO;
import bll.SearchResultBO;
import bll.TFIDFAnalysisBO;
import bll.TransliterationBO;
import dal.AbstractDALFactory;
import dal.DALFacade;
import dal.IDALFacade;
import dal.IDALFactory;
import dal.IFileDAO;
import dal.IFileImportDAO;
import dal.ILemmatizationDAO;
import dal.IPOSTaggingDAO;
import dal.IPaginationDAO;
import dal.ISearchResultDAO;
import dal.ITransliterationDAO;


public class MainScreen extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton openFileButton;
    private JButton createFileButton;
    private JButton importFileButton;
    private JButton searchButton; 
    
	private IBLFacade blFacade;
    private JButton performAnalysisButton;
    public MainScreen(IBLFacade blFacade) {
        this.setBlFacade(blFacade);
        setTitle("Kitaab Script");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(235, 224, 199));
        ImageIcon icon = new ImageIcon("C:\\Users\\nasif\\git\\24f-prj-scd-team\\src\\resources\\images\\icon.png");
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
        searchButton = createStyledButton("Search", mughalFont); 
        performAnalysisButton = createStyledButton("Perform Analysis", mughalFont);
        buttonPanel.add(openFileButton);
        buttonPanel.add(createFileButton);
        buttonPanel.add(importFileButton);
        buttonPanel.add(searchButton); 
        buttonPanel.add(performAnalysisButton);
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBackground(new Color(235, 224, 199));
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));
        paddedPanel.add(buttonPanel, BorderLayout.CENTER);
        performAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame analysisFrame = new JFrame("Analysis Panel");
                analysisFrame.setSize(600, 600);
                analysisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                analysisFrame.setLocationRelativeTo(null);
                analysisFrame.add(new AnalysisPanel(blFacade));
                analysisFrame.setVisible(true);
            }
        });
        importFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImportFileDialogueBox dialog = new ImportFileDialogueBox(MainScreen.this, blFacade);
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
                	blFacade.createFile(fileName, ""); 
                    MainScreen.this.setVisible(true);
                    FileUpdatePanel fileUpdatePanel = new FileUpdatePanel(fileName, blFacade);
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
                FileTablePanel fileTablePanel = new FileTablePanel(blFacade);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(fileTablePanel);
                frame.setSize(600, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

       
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SearchPanel searchPanel = new SearchPanel(blFacade);
                
                JFrame searchFrame = new JFrame("Search Panel");
                searchFrame.setSize(600, 600);
                searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                searchFrame.add(searchPanel);
                searchFrame.setVisible(true);
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
            File fontFile = new File("C:\\Users\\nasif\\git\\24f-prj-scd-team\\src\\resources\\fonts\\OldLondon.ttf");
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
                ISearchResultDAO searchResultDAO=dalFactory.getSearchResultDAO();

                ITransliterationDAO transliterationDAO=dalFactory.getTransliterationDAO();  
                ILemmatizationDAO lemmatizationDAO=dalFactory.getLemmatizationDAO();
                IPOSTaggingDAO postaggingDAO=dalFactory.getPOSTaggingDAO();
                
                
                IDALFacade dalFacade = new DALFacade(fileDAO, fileImportDAO, paginationDAO,searchResultDAO,transliterationDAO,lemmatizationDAO,postaggingDAO);

                
                IFileBO fileBO = new FileBO(dalFacade);
                IFileImportBO fileImportBO = new FileImportBO(dalFacade);
                IFilePaginationBO filePaginationBO = new FilePaginationBO(dalFacade);
                ISearchResultBO searchResultBO=new SearchResultBO(dalFacade);

                ITransliterationBO transliterationBO=new TransliterationBO(dalFacade);
                ILemmatizationBO lemmatizationBO=new LemmatizationBO(dalFacade);
                IPOSTaggingBO posTaggingBO =new POSTaggingBO (dalFacade);
                ITFIDFAnalysisBO tfidfAnalysisBO=new TFIDFAnalysisBO(dalFacade);
                IPMIAnalysisBO pmiAnalysisBO=new PMIAnalysisBO(dalFacade);
                IKLAnalysisBO klAnalysisBO=new KLAnalysisBO(dalFacade);
                
                IBLFacade blFacade=new BLFacade(fileBO,fileImportBO,filePaginationBO,searchResultBO,transliterationBO,lemmatizationBO,posTaggingBO,tfidfAnalysisBO,pmiAnalysisBO,klAnalysisBO);

                
                MainScreen mainFrame = new MainScreen(blFacade);
                mainFrame.setVisible(true);
            }
        });
    }

	public IBLFacade getBlFacade() {
		return blFacade;
	}

	public void setBlFacade(IBLFacade blFacade) {
		this.blFacade = blFacade;
	}
}
