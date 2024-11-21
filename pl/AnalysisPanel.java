package pl;

import bll.IBLFacade;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AnalysisPanel extends JPanel {

    private IBLFacade blFacade;
    private JTextField searchField;
    private JTextArea resultArea;
    private JComboBox<String> analysisTypeComboBox;

    public AnalysisPanel(IBLFacade blFacade) {
        this.blFacade = blFacade;
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199));

        Font font = new Font("Serif", Font.BOLD, 18);

        // Search Field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JLabel searchLabel = new JLabel("Search for a word:");
        searchLabel.setFont(font);
        searchField = new JTextField(20);
        searchField.setFont(font);
        searchPanel.setBackground(new Color(235, 224, 199));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        // ComboBox for Analysis Type
        JPanel analysisTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        JLabel analysisTypeLabel = new JLabel("Choose Analysis:");
        analysisTypeLabel.setFont(font);
        analysisTypeComboBox = new JComboBox<>(new String[]{"TF-IDF", "PMI", "KL Divergence"});
        analysisTypeComboBox.setFont(font);
        analysisTypePanel.setBackground(new Color(235, 224, 199));
        analysisTypePanel.add(analysisTypeLabel);
        analysisTypePanel.add(analysisTypeComboBox);

        // Result Area
        resultArea = new JTextArea(10, 30);
        resultArea.setFont(new Font("Serif", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Perform Analysis Button
        JButton performAnalysisButton = new JButton("Perform Analysis");
        performAnalysisButton.setFont(font);
        performAnalysisButton.setBackground(new Color(138, 83, 43));
        performAnalysisButton.setForeground(new Color(255, 244, 206));
        performAnalysisButton.setPreferredSize(new Dimension(200, 50));
        performAnalysisButton.setFocusPainted(false);
        performAnalysisButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        performAnalysisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performAnalysis();
            }
        });

        // Adding components to the panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        bottomPanel.setBackground(new Color(235, 224, 199));
        bottomPanel.add(performAnalysisButton);

        add(searchPanel, BorderLayout.NORTH);
        add(analysisTypePanel, BorderLayout.CENTER);
        add(resultScrollPane, BorderLayout.SOUTH);
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
            Map<String,Double> result;
            switch (selectedAnalysis) {
                case "TF-IDF":
                    result = blFacade.performTFIDFAnalysisForWord(searchTerm);
                    //System.out.println(result);
                    break;
                case "PMI":
                    result = blFacade.performPMIAnalysisForWord(searchTerm);
                    break;
                case "KL Divergence":
                    result = blFacade.performKLAnalysisForWord(searchTerm);
                    break;
            }
//            resultArea.setText(result);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error performing analysis: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
