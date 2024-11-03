package pl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bll.FileBO;
import bll.FilePaginationBO;
import bll.IBLFacade;
import bll.SearchResultBO;
import dal.AbstractDALFactory;
import dal.MySQLDALFactory;
import dto.SearchResultDTO;

public class SearchPanel extends JPanel {
    private JList<SearchResultDTO> searchList;
    private DefaultListModel<SearchResultDTO> listModel;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;
    private IBLFacade blFacade;
    private FileDetailPanel detailPanel;

    public SearchPanel(IBLFacade blFacade) {
        this.blFacade = blFacade;
        listModel = new DefaultListModel<>();
        searchList = new JList<>(listModel);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);

       
        searchField.setPreferredSize(new java.awt.Dimension(300, 30));
        searchButton.setPreferredSize(new java.awt.Dimension(100, 30));
        resultArea.setPreferredSize(new java.awt.Dimension(450, 250));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Enter search term:"));
        add(searchField);
        add(searchButton);
        add(new JScrollPane(resultArea));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        searchList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    navigateToFile(searchList.getSelectedValue());
                }
            }
        });
    }

    private void navigateToFile(SearchResultDTO result) {
        if (result == null) return;

        new FileDetailPanel(result.getFileName(),blFacade).loadFileDetails(result.getFileName());
        JOptionPane.showMessageDialog(this, "Navigated to file: " + result.getFileName() + " at page: " + result.getPageNumber());
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter a search term.");
            return;
        }

        List<SearchResultDTO> searchResults = blFacade.search(searchTerm);
        displayResults(searchResults);
    }

    private void displayResults(List<SearchResultDTO> searchResults) {
        resultArea.setText("");
        resultArea.setLayout(new BoxLayout(resultArea, BoxLayout.Y_AXIS));
        resultArea.removeAll();

        if (searchResults.isEmpty()) {
            resultArea.setText("No results found.");
        } else {
            for (SearchResultDTO result : searchResults) {
                JPanel resultPanel = new JPanel();
                resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

                JLabel fileNameLabel = new JLabel("File Name: " + result.getFileName());
                JLabel pageNumberLabel = new JLabel("Page Number: " + result.getPageNumber());
                JLabel matchedWordLabel = new JLabel("Matched Word: " + result.getMatchedWord());
                JLabel contextLabel = new JLabel("Context: ..." + result.getBeforeContext() + result.getMatchedWord() + " " + result.getAfterContext() + "...");

                
                JButton openFileButton = new JButton("Open File");
                openFileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        navigateToFile(result);
                    }
                });

                resultPanel.add(fileNameLabel);
                resultPanel.add(pageNumberLabel);
                resultPanel.add(matchedWordLabel);
                resultPanel.add(contextLabel);
                resultPanel.add(openFileButton);

                resultPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
                resultArea.add(resultPanel);
            }
            resultArea.revalidate();
            resultArea.repaint();
        }
    }

    
}