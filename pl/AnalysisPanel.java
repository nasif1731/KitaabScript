package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bll.IBLFacade;

public class AnalysisPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IBLFacade blFacade;
    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JComboBox<String> analysisTypeComboBox;

    public AnalysisPanel(IBLFacade blFacade) {
        this.blFacade = blFacade;

       
        setLayout(new BorderLayout(15, 15)); 
        setBackground(new Color(235, 224, 199));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        Font font = new Font("Serif", Font.BOLD, 18);

        
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.setBackground(new Color(235, 224, 199));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(235, 224, 199));
        JLabel searchLabel = new JLabel("Search for a word:");
        searchLabel.setFont(font);
        searchField = new JTextField(20);
        searchField.setFont(font);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        
        JPanel analysisTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        analysisTypePanel.setBackground(new Color(235, 224, 199));
        JLabel analysisTypeLabel = new JLabel("Choose Analysis:");
        analysisTypeLabel.setFont(font);
        analysisTypeComboBox = new JComboBox<>(new String[]{"TF-IDF", "PMI", "KL Divergence"});
        analysisTypeComboBox.setFont(font);
        analysisTypePanel.add(analysisTypeLabel);
        analysisTypePanel.add(analysisTypeComboBox);

        
        topPanel.add(searchPanel);
        topPanel.add(analysisTypePanel);

        
        resultArea = new JTextArea(10, 30);
        resultArea.setFont(new Font("Serif", Font.PLAIN, 16)); 
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(102, 51, 17), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Analysis Results"));


       
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(235, 224, 199));

        JButton performAnalysisButton = new JButton("Perform Analysis");
        performAnalysisButton.setFont(font);
        performAnalysisButton.setBackground(new Color(138, 83, 43));
        performAnalysisButton.setForeground(new Color(255, 244, 206));
        performAnalysisButton.setPreferredSize(new Dimension(200, 50));
        performAnalysisButton.setFocusPainted(false);
        performAnalysisButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(102, 51, 17), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        performAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAnalysis();
            }
        });

        bottomPanel.add(performAnalysisButton);

       
        add(topPanel, BorderLayout.NORTH);
        add(resultScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void performAnalysis() {
        String searchTerm = searchField.getText().trim();
        String selectedAnalysis = (String) analysisTypeComboBox.getSelectedItem();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a word to search.");
            return;
        }

        try {
            Map<String, Double> result;
            StringBuilder res = new StringBuilder();
            switch (selectedAnalysis) {
                case "TF-IDF":
                    result = blFacade.performTFIDFAnalysisForWord(searchTerm);
                    break;
                case "PMI":
                    result = blFacade.performPMIAnalysisForWord(searchTerm);
                    break;
                case "KL Divergence":
                    result = blFacade.performKLAnalysisForWord(searchTerm);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid analysis type selected.");
                    return;
            }

            
            
           
            for (Map.Entry<String, Double> entry : result.entrySet()) {
                String word = entry.getKey();
                double score = entry.getValue();
                if (word.isEmpty()) continue;

                
                res.append(String.format(" %-15s : %8.4f%n", word, score)); 
                res.append("-".repeat(40)).append("\n");
            }

            resultArea.setText(res.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error performing analysis: " + ex.getMessage());
        }
    }

}
