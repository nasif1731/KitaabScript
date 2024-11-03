package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import bll.IBLFacade;
import dto.PageDTO;

public class TransliterationPanel extends JPanel {

    public JTextPane textPane;
    private JPopupMenu contextMenu;
    private IBLFacade blFacade;
    private JPanel sidebarPanel;
    private JTextArea transliteratedTextArea;
    private int pageID;
    private int pageNumber; 

    public TransliterationPanel(IBLFacade blFacade, int pageID, int pageNumber) {
        this.blFacade = blFacade;
        this.pageID = pageID;
        this.pageNumber = pageNumber;

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Call the method to load the page content when the panel is created
        loadPageContent();
        
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

    // Load content for the current page
    public void loadPageContent() {
        PageDTO page = blFacade.getPageContent(pageID, pageNumber);
        if (page != null) {
            textPane.setText(page.getPageContent());
            // Optionally, you can save the transliteration immediately after loading the page content
            blFacade.saveTransliterationIfNotExists(pageID, page.getPageContent());
        } else {
            textPane.setText("No content available for this page.");
//            System.out.println("PageDTO is null for pageID: " + pageID + ", pageNumber: " + pageNumber); // Debugging statement
        }
    }

    // Show the context menu for transliteration
    public void showContextMenu(MouseEvent e) {
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }
    // Perform transliteration and display result in sidebar
    public void performTransliteration(String text) {
        String selectedText = text;
        System.out.println("Selected text for transliteration: '" + selectedText + "'"); // Debugging statement
        if (selectedText == null || selectedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select text for transliteration.");
            return;
        }

        // Retrieve transliterated text through blFacade
        String transliteratedText = blFacade.getTransliterationForText(selectedText);
        
        // Check if transliteratedText is null or empty
        if (transliteratedText == null || transliteratedText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Transliteration failed.");
            return;
        }

        // Show the transliteration in a dialog
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

    // Show or hide the sidebar
    public void toggleSidebar(boolean visible) {
        sidebarPanel.setVisible(visible);
        revalidate();
        repaint();
    }
}
