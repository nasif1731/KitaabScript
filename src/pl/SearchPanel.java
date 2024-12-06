package pl;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bll.IBLFacade;
import dto.SearchResultDTO;
public class SearchPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private static final Logger logger = LogManager.getLogger(SearchPanel.class);
	private JList<SearchResultDTO> searchList;
    private DefaultListModel<SearchResultDTO> listModel;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;
    private JScrollPane scrollPane;
    private IBLFacade blFacade;

    public SearchPanel(IBLFacade blFacade) {
        this.blFacade = blFacade;
        
        initializeUIComponents();
        layoutComponents();
        
        setupListeners();
        styleComponents();
    }

    private void initializeUIComponents() {
        listModel = new DefaultListModel<>();
        searchList = new JList<>(listModel);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(5, 30);
        scrollPane = new JScrollPane(resultArea);
    }

    private void layoutComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Enter search term:"));
        add(searchField);
        add(searchButton);
        add(scrollPane);
    }

    private void setupListeners() {
        searchButton.addActionListener(this::performSearch);
        searchList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    SearchResultDTO selectedResult = searchList.getSelectedValue();
                    if (selectedResult != null) {
                        navigateToFile(selectedResult);
                    }
                }
            }
        });
    }

    private void styleComponents() {
        setBackground(new Color(245, 245, 220));
        Font serifFont = new Font("Serif", Font.BOLD, 16);
        searchField.setFont(serifFont);
        searchField.setMaximumSize(new Dimension(300, 30));
        searchButton.setFont(serifFont);
        searchButton.setBackground(new Color(160, 82, 45));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createRaisedBevelBorder());
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBackground(new Color(255, 248, 220));
        resultArea.setEditable(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(160, 82, 45), 2));
    }

    private void performSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim();
        logger.info("Performing search for term: {}", searchTerm);
        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter a search term.");
            logger.warn("Search term is empty.");
            return;
        }
        List<SearchResultDTO> searchResults = blFacade.search(searchTerm);
        displayResults(searchResults);
    }

    private void navigateToFile(SearchResultDTO result) {
    	 logger.info("Opening file: {}", result.getFileName());
        new FileDetailPanel(result.getFileName(), blFacade).loadFileDetails(result.getFileName());
    }

    private void displayResults(List<SearchResultDTO> searchResults) {
        resultArea.setText("");
        if (searchResults.isEmpty()) {
            resultArea.setText("No results found.");
            return;
        }
        resultArea.setLayout(new BoxLayout(resultArea, BoxLayout.Y_AXIS));
        resultArea.removeAll();
        for (SearchResultDTO result : searchResults) {
            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
            JLabel fileNameLabel = new JLabel("File Name: " + result.getFileName());
            JLabel pageNumberLabel = new JLabel("Page Number: " + result.getPageNumber());
            JLabel matchedWordLabel = new JLabel("Matched Word: " + result.getMatchedWord());
            JLabel contextLabel = new JLabel("Context: ..." + result.getBeforeContext() + result.getMatchedWord() + " " + result.getAfterContext() + "...");
            JButton openFileButton = new JButton("Open File");
            openFileButton.addActionListener(e -> navigateToFile(result));
            resultPanel.add(fileNameLabel);
            resultPanel.add(pageNumberLabel);
            resultPanel.add(matchedWordLabel);
            resultPanel.add(contextLabel);
            resultPanel.add(openFileButton);
            resultArea.add(resultPanel);
        }
        resultArea.revalidate();
        resultArea.repaint();
    }
}