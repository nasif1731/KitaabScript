package pl;

import java.awt.BorderLayout;

import org.apache.logging.log4j.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import bll.IBLFacade;

public class TransliterationPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger  = LogManager.getLogger(TransliterationPanel.class);
	public JTextPane textPane;
    private JPopupMenu contextMenu;
    private IBLFacade blFacade;
    private JPanel sidebarPanel;
    private JTextArea transliteratedTextArea;
    
	private int pageID;
	private int pageNumber; 

    public TransliterationPanel(IBLFacade blFacade, int pageID, int pageNumber) {
        this.blFacade = blFacade;
        this.setPageID(pageID);
        this.setPageNumber(pageNumber);

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Arial", Font.PLAIN, 16));
        
        
        
        contextMenu = new JPopupMenu();
        JMenuItem transliterateItem = new JMenuItem("Transliterate");
        transliterateItem.addActionListener(e -> performTransliteration(textPane.getText()));
        contextMenu.add(transliterateItem);
        
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
        });
        
        sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createTitledBorder("Transliterated Text"));
        transliteratedTextArea = new JTextArea();
        transliteratedTextArea.setEditable(false);
        transliteratedTextArea.setWrapStyleWord(true);
        transliteratedTextArea.setLineWrap(true);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> toggleSidebar(false));

        sidebarPanel.add(closeButton, BorderLayout.NORTH);
        sidebarPanel.add(new JScrollPane(transliteratedTextArea), BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        add(sidebarPanel, BorderLayout.EAST); 
    }


 
    public void showContextMenu(MouseEvent e) {
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }
    public void performTransliteration(String text) {
        String selectedText = text;
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select text for transliteration.");
            logger.warn("No text selected for transliteration.");
            return;
        }

        String transliteratedText = blFacade.getTransliterationForText(selectedText);
        
        
        if (transliteratedText == null || transliteratedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Transliteration failed.");
            return;
        }

        JDialog transliterationDialog = new JDialog();
        transliterationDialog.setTitle("Transliteration");
        transliterationDialog.setModal(true);
        transliterationDialog.setSize(400, 300);
        transliterationDialog.setLocationRelativeTo(this);

        JTextArea transliteratedTextArea = new JTextArea(transliteratedText);
        transliteratedTextArea.setEditable(false);
        transliteratedTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        transliteratedTextArea.setLineWrap(true);
        transliteratedTextArea.setWrapStyleWord(true);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> transliterationDialog.dispose());

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.add(new JScrollPane(transliteratedTextArea), BorderLayout.CENTER);
        dialogPanel.add(closeButton, BorderLayout.SOUTH);

        transliterationDialog.getContentPane().add(dialogPanel);
        transliterationDialog.setVisible(true);
    }

    public void toggleSidebar(boolean visible) {
        sidebarPanel.setVisible(visible);
        revalidate();
        repaint();
    }



	public int getPageID() {
		return pageID;
	}



	public void setPageID(int pageID) {
		this.pageID = pageID;
	}



	public int getPageNumber() {
		return pageNumber;
	}



	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
