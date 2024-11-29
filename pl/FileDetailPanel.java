package pl;
import bll.IBLFacade;
import dto.FileDTO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;
import dto.PageDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.function.Function;

public class FileDetailPanel extends JFrame {
	private JTextPane fileContentArea;
	private JButton updateButton;
	private JButton prevButton;
	private JButton nextButton;
	private JLabel wordCountLabel;
	private JLabel pageLabel;
	private IBLFacade blFacade;
	private int currentPage = 1;
	private FileDTO fileDTO;
	private String filename;
	private int totalPages = 0;
	private int pageId;
	private TransliterationPanel transliterationPanel;
	private LemmatizationPanel lemmatizationPanel;
	private POSTaggingPanel posTaggingPanel;
	private LinkedList<POSTaggingDTO> allPOSTaggingResults;
	private JButton showPOSTaggingButton;
	private LinkedList<LemmatizationDTO> allLemmatizationResults;
	private JButton showLemmatizationButton; 
	
	private JPanel cardPanel; 
	public FileDetailPanel(String fileName, IBLFacade blFacade) {
		this.blFacade = blFacade;
		this.filename = fileName;
		this.fileDTO = blFacade.getOneFile(filename);
		if (fileDTO == null) {
			JOptionPane.showMessageDialog(this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		allPOSTaggingResults = new LinkedList<>();
		allLemmatizationResults = new LinkedList<>();
		initializeUI();
		loadFileDetails(fileName);
		int fileId = blFacade.getFileID(filename);
		setVisible(true);
		

	}

	private <T> String generateHtmlTable(LinkedList<T> results, String[] headers, Function<T, String[]> rowMapper) {
		StringBuilder tableBuilder = new StringBuilder("<html><body><table border='1' style='width:100%;'>");
		tableBuilder.append("<tr>");
		for (String header : headers) {
			tableBuilder.append("<th>").append(header).append("</th>");
		}
		tableBuilder.append("</tr>");

		for (T result : results) {
			String[] rowData = rowMapper.apply(result);
			tableBuilder.append("<tr>");
			for (String data : rowData) {
				tableBuilder.append("<td>").append(data).append("</td>");
			}
			tableBuilder.append("</tr>");
		}

		tableBuilder.append("</table></body></html>");
		return tableBuilder.toString();
	}

	private void initializeUI() {
	    setLayout(new BorderLayout());
	    setBackground(new Color(235, 224, 199));
	    ImageIcon icon = new ImageIcon("resources/images/icon.png");
	    setIconImage(icon.getImage());
	    this.setSize(500, 500);

	    fileContentArea = new JTextPane();
	    fileContentArea.setEditable(false);
	    add(new JScrollPane(fileContentArea), BorderLayout.CENTER);

	    JPopupMenu contextMenu = new JPopupMenu();
	    JMenuItem transliterateItem = new JMenuItem("Transliterate");
	    transliterateItem.addActionListener(e -> showTransliterationSidebar());
//	    JMenuItem lemmatizeItem = new JMenuItem("Lemmatize");
//	    lemmatizeItem.addActionListener(e -> showLemmatizationSidebar());
	    contextMenu.add(transliterateItem);
//	    contextMenu.add(lemmatizeItem);

	    fileContentArea.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mousePressed(MouseEvent e) {
	            if (e.isPopupTrigger()) contextMenu.show(fileContentArea, e.getX(), e.getY());
	        }

	        @Override
	        public void mouseReleased(MouseEvent e) {
	            if (e.isPopupTrigger()) contextMenu.show(fileContentArea, e.getX(), e.getY());
	        }
	    });

	    pageId = blFacade.getPageID(fileDTO.getId(), currentPage);
	    cardPanel = new JPanel(new CardLayout());
        add(cardPanel, BorderLayout.EAST);

	    /*transliterationPanel = new TransliterationPanel(blFacade, pageId, currentPage);
	    transliterationPanel.setVisible(false);
	    add(transliterationPanel, BorderLayout.EAST);

	    lemmatizationPanel = new LemmatizationPanel(blFacade, pageId);
	    lemmatizationPanel.setVisible(false);
	    add(lemmatizationPanel, BorderLayout.EAST);

	    posTaggingPanel = new POSTaggingPanel(blFacade, pageId);
	    posTaggingPanel.setVisible(false);
	    add(posTaggingPanel, BorderLayout.EAST);*/
        
        transliterationPanel = new TransliterationPanel(blFacade, pageId, currentPage);
        cardPanel.add(transliterationPanel, "Transliteration");

        lemmatizationPanel = new LemmatizationPanel(blFacade, pageId);
        cardPanel.add(lemmatizationPanel, "Lemmatization");

        posTaggingPanel = new POSTaggingPanel(blFacade, pageId);
        cardPanel.add(posTaggingPanel, "POS Tagging");

	    updateButton = new JButton("Update");
	    updateButton.setBackground(new Color(138, 83, 43));
	    updateButton.setForeground(Color.WHITE);
	    updateButton.addActionListener(e -> loadWriteMode());

	    showPOSTaggingButton = new JButton("Show POS Tagging");
	    showPOSTaggingButton.setBackground(new Color(138, 83, 43));
	    showPOSTaggingButton.setForeground(Color.WHITE);
	    showPOSTaggingButton.addActionListener(e -> showAllPOSTaggingResults());
	    
	    showLemmatizationButton= new JButton("Show Lemmatization");
	    showLemmatizationButton.setBackground(new Color(138, 83, 43));
	    showLemmatizationButton.setForeground(Color.WHITE);
	    showLemmatizationButton.addActionListener(e -> showAllLemmatizationResults());

	    
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    buttonPanel.add(updateButton); 
	    buttonPanel.add(showPOSTaggingButton); 
	    buttonPanel.add(showLemmatizationButton);
	    buttonPanel.setBackground(new Color(235, 224, 199));

	    prevButton = new JButton("← Previous");
	    prevButton.setBackground(new Color(138, 83, 43));
	    prevButton.setForeground(Color.WHITE);
	    prevButton.addActionListener(e -> pageNavigation(currentPage - 1));

	    nextButton = new JButton("Next →");
	    nextButton.setBackground(new Color(138, 83, 43));
	    nextButton.setForeground(Color.WHITE);
	    nextButton.addActionListener(e -> pageNavigation(currentPage + 1));

	    JPanel navigationPanel = new JPanel(new FlowLayout());
	    navigationPanel.add(prevButton);
	    navigationPanel.add(nextButton);
	    navigationPanel.setBackground(new Color(235, 224, 199));
	    buttonPanel.add(navigationPanel); 

	    add(buttonPanel, BorderLayout.NORTH); 

	    wordCountLabel = new JLabel("Word Count: 0");
	    wordCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	    add(wordCountLabel, BorderLayout.SOUTH);

	    pageLabel = new JLabel("Page " + currentPage + " of " + totalPages);
	    pageLabel.setHorizontalAlignment(SwingConstants.LEFT);

	    JPanel bottomPanel = new JPanel(new BorderLayout());
	    bottomPanel.add(pageLabel, BorderLayout.WEST);
	    bottomPanel.add(wordCountLabel, BorderLayout.EAST);
	    add(bottomPanel, BorderLayout.SOUTH);
	}
	private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (cardPanel.getLayout());
        cl.show(cardPanel, panelName);  // "Transliteration", "Lemmatization", or "POS Tagging"
    }
	private void processAllPagesLemmatization() {
        int totalPageCount = blFacade.getTotalPages(fileDTO.getId());
        for (int i = 1; i <= totalPageCount; i++) {
            PageDTO page = blFacade.getPageContent(fileDTO.getId(), i);
            if (page != null) {
                blFacade.processLemmatizationForPage(page.getPageId(), page.getPageContent());
                LinkedList<LemmatizationDTO> pageResults = blFacade.getLemmatizationForPage(page.getPageId());
                allLemmatizationResults.addAll(pageResults);
            }
        }
    }

	private void showAllLemmatizationResults() {
		processAllPagesLemmatization(); 
        if (allLemmatizationResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Lemmatization results available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String lemmatizationResults = generateHtmlTable(allLemmatizationResults, new String[]{"Word", "Lemma", "Root"}, 
            dto -> new String[]{dto.getWord(), dto.getLemma(), dto.getRoot()});
        lemmatizationPanel.setContent(lemmatizationResults);
        showPanel("Lemmatization"); 
        /*setComponentZOrder(lemmatizationPanel, 0);
        lemmatizationPanel.setVisible(true);*/
    }

	private void processAllPagesPOSTagging() {
        int totalPageCount = blFacade.getTotalPages(fileDTO.getId());
        for (int i = 1; i <= totalPageCount; i++) {
            PageDTO page = blFacade.getPageContent(fileDTO.getId(), i);
            if (page != null) {
                blFacade.processPOSTaggingForPage(page.getPageId(), page.getPageContent());
                LinkedList<POSTaggingDTO> pageResults = blFacade.getPOSTaggingForPage(page.getPageId());
                allPOSTaggingResults.addAll(pageResults);
            }
        }
    }
	private void showAllPOSTaggingResults() {
		processAllPagesPOSTagging();
        if (allPOSTaggingResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No POS tagging results available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String posResults = generateHtmlTable(allPOSTaggingResults, new String[]{"Word", "POS Tag"}, 
            dto -> new String[]{dto.getWord(), dto.getPosTag()});
        posTaggingPanel.setContent(posResults);
        /*setComponentZOrder(posTaggingPanel, 0);
        posTaggingPanel.setVisible(true);*/
        showPanel("POS Tagging"); 
    }

	private void showTransliterationSidebar() {
		String selectedText = fileContentArea.getSelectedText();
		if (selectedText != null) {
			transliterationPanel.textPane.setText(selectedText);
			transliterationPanel.performTransliteration(selectedText);
			 showPanel("Transliteration"); 
			/*transliterationPanel.setVisible(true);*/
		} else {
			JOptionPane.showMessageDialog(this, "Please select text for transliteration.", "No Text Selected",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	

	void loadFileDetails(String fileName) {
		try {
			displayPage(currentPage);
			int wordCount = blFacade.getWordCount(fileName);
			wordCountLabel.setText("Word Count: " + wordCount);
			totalPages = blFacade.getTotalPages(fileDTO.getId());
			updatePageLabel();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading file details: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void displayPage(int pageNumber) {
		
		PageDTO page = blFacade.getPageContent(fileDTO.getId(), pageNumber);
		if (page != null) {
			currentPage = pageNumber;
			pageId = page.getPageId();
			String pageContent = page.getPageContent();
			setLanguageOrientation(pageContent);
			fileContentArea.setText(pageContent);

			

			

			updatePageLabel();
			blFacade.saveTransliterationIfNotExists(page.getPageId(), page.getPageContent());
			blFacade.processPOSTaggingForPage(page.getPageId(), page.getPageContent());
			blFacade.processLemmatizationForPage(page.getPageId(), page.getPageContent());
			 LinkedList<POSTaggingDTO> posTaggingResults=blFacade.getPOSTaggingForPage(page.getPageId());
			 LinkedList<LemmatizationDTO> lemmatizationResults=blFacade.getLemmatizationForPage(page.getPageId());
			 String posResults = generateHtmlTable(posTaggingResults, new String[]{"Word", "POS Tag"}, dto -> new String[]{dto.getWord(), dto.getPosTag()});
		        

		        String lemmaResults = generateHtmlTable(lemmatizationResults, new String[]{"Word", "Lemma", "Root"}, dto -> new String[]{dto.getWord(), dto.getLemma(), dto.getRoot()});
		        
			
		} else {
			JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void pageNavigation(int pageNumber) {
		if (pageNumber < 1 || pageNumber > totalPages) {
			JOptionPane.showMessageDialog(this, "No more pages available.", "Navigation",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		displayPage(pageNumber);
	}
//	private String normalizeText(String text) {
//	    return text.replaceAll("[\\u064B-\\u0652]", "") // Remove diacritics
//	               .replaceAll("[^\\p{L}\\p{N}\\s]", "") // Remove non-letters/numbers
//	               .trim(); // Trim whitespace
//	}
//	private boolean isWordInSelectedText(String word, String selectedText) {
//	    
//	    String token = selectedText;
//	         
////	        System.out.println(token + "  " + word);
//	        String normalizedSearchWord = normalizeText(word);
//	        String normalizedtoken = normalizeText(token);
//	        if (normalizedtoken.contains(normalizedSearchWord)) {
////	        	System.out.println("Im Here");
//	            return true;
//	        }
//	    
//	    return false;
//	}

	private void updatePageLabel() {
		pageLabel.setText("Page " + currentPage + " of " + totalPages);
	}

	void loadWriteMode() {
		dispose();
		FileUpdatePanel dialog = new FileUpdatePanel(fileDTO.getFilename(), blFacade);
		dialog.setVisible(true);
	}

	private void setLanguageOrientation(String content) {
		if (fileContentArea == null)
			return;

		boolean isUrdu = content.codePoints()
				.anyMatch(c -> (c >= 0x0600 && c <= 0x06FF) || (c >= 0x0750 && c <= 0x077F));
		fileContentArea.setComponentOrientation(
				isUrdu ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
	}
}