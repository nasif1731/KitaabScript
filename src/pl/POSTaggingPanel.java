
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

import interfaces.IBLFacade;

public class POSTaggingPanel extends JPanel {
  
	private static final long serialVersionUID = 1L;

	private IBLFacade blFacade;
    private int pageId;
    private JTextPane posTaggingResultsArea;
    private JButton closeButton;

    public POSTaggingPanel(IBLFacade blFacade, int pageId) {
        this.setBlFacade(blFacade);
        this.setPageId(pageId);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(200, 220, 240));
        setPreferredSize(new Dimension(300, 500));

        JLabel headerLabel = new JLabel("POS Tagging Results");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        posTaggingResultsArea = new JTextPane();
        posTaggingResultsArea.setEditable(false);
        posTaggingResultsArea.setContentType("text/html"); 
        add(new JScrollPane(posTaggingResultsArea), BorderLayout.CENTER);

        closeButton = new JButton("Close");
        closeButton.setBackground(new Color(70, 120, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> setVisible(false));
        add(closeButton, BorderLayout.SOUTH);
    }

////    public void performPOSTagging(String text) {
//        if (text == null || text.trim().isEmpty()) {
//            JOptionPane.showMessageDialog(this, "No text provided for POS tagging.", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        posTaggingResultsArea.setText(""); 
//        StringBuilder resultsBuilder = new StringBuilder("<html><body><table border='1' style='width:100%;'>");
//        resultsBuilder.append("<tr><th>Word</th><th>POS Tag</th></tr>");
//
//        blFacade.processPOSTaggingForPage(pageId, text);
//        LinkedList<POSTaggingDTO> posTaggingResults = blFacade.getPOSTaggingForPage(pageId);
//
//        for (POSTaggingDTO dto : posTaggingResults) {
//            resultsBuilder.append("<tr>")
//                          .append("<td>").append(dto.getWord()).append("</td>")
//                          .append("<td>").append(dto.getPosTag()).append("</td>")
//                          .append("</tr>");
//        }
//
//        resultsBuilder.append("</table></body></html>");
//        posTaggingResultsArea.setText(resultsBuilder.toString());
//    }


	public void setContent(String content) {
		// TODO Auto-generated method stub
		posTaggingResultsArea.setText(content);
	}

	public IBLFacade getBlFacade() {
		return blFacade;
	}

	public void setBlFacade(IBLFacade blFacade) {
		this.blFacade = blFacade;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
}
