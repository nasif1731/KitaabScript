package pl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class TFIDFPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextPane tfidfResultsArea;
    private JButton closeButton;

    public TFIDFPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 224, 199));
        setPreferredSize(new Dimension(300, 500));

        JLabel headerLabel = new JLabel("TF-IDF Results");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        tfidfResultsArea = new JTextPane();
        tfidfResultsArea.setEditable(false);
        tfidfResultsArea.setContentType("text/html");
        add(new JScrollPane(tfidfResultsArea), BorderLayout.CENTER);

        closeButton = new JButton("Close");
        closeButton.setBackground(new Color(138, 83, 43));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> setVisible(false));
        add(closeButton, BorderLayout.SOUTH);
    }

    public void setContent(String content) {
        tfidfResultsArea.setText(content);
    }
}
