
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

import bll.IBLFacade;

public class LemmatizationPanel extends JPanel {
	private IBLFacade blFacade;
	private int pageId;
	private JTextPane lemmatizationResultsArea;
	private JButton closeButton;

	public LemmatizationPanel(IBLFacade blFacade, int pageId) {
		this.blFacade = blFacade;
		this.pageId = pageId;
		initializeUI();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		setBackground(new Color(235, 224, 199));
		setPreferredSize(new Dimension(300, 500));

		JLabel headerLabel = new JLabel("Lemmatization Results");
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(headerLabel, BorderLayout.NORTH);

		lemmatizationResultsArea = new JTextPane();
		lemmatizationResultsArea.setEditable(false);
		lemmatizationResultsArea.setContentType("text/html");
		add(new JScrollPane(lemmatizationResultsArea), BorderLayout.CENTER);

		closeButton = new JButton("Close");
		closeButton.setBackground(new Color(138, 83, 43));
		closeButton.setForeground(Color.WHITE);
		closeButton.addActionListener(e -> setVisible(false));
		add(closeButton, BorderLayout.SOUTH);
	}

////    public void performLemmatization(String text) {
//        if (text == null || text.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "No text provided for lemmatization.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        StringBuilder resultsBuilder = new StringBuilder("<html><body><table border='1' style='width:100%;'>");
//        resultsBuilder.append("<tr><th>Word</th><th>Lemma</th><th>Root</th></tr>");
//
//        blFacade.processLemmatizationForPage(pageId, text);
//        LinkedList<LemmatizationDTO> lemmatizationResults = blFacade.getLemmatizationForPage(pageId);
//
//        for (LemmatizationDTO dto : lemmatizationResults) {
//            resultsBuilder.append("<tr>")
//                          .append("<td>").append(dto.getWord()).append("</td>")
//                          .append("<td>").append(dto.getLemma()).append("</td>")
//                          .append("<td>").append(dto.getRoot()).append("</td>")
//                          .append("</tr>");
//        }
//
//        resultsBuilder.append("</table></body></html>");
//        lemmatizationResultsArea.setText(resultsBuilder.toString());
//    }
	public void setContent(String content) {
		lemmatizationResultsArea.setText(content);
	}
}

