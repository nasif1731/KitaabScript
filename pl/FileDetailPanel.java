package pl;
import bll.IBLFacade;
import dto.FileDTO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;
import dto.PageDTO;
import util.FarasaPreProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.BreakIterator;
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

	
	
	public FileDetailPanel(String fileName, IBLFacade blFacade) {
		this.blFacade = blFacade;
		this.filename = fileName;
		this.fileDTO = blFacade.getOneFile(filename);
		if (fileDTO == null) {
			JOptionPane.showMessageDialog(this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
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
		JMenuItem lemmatizeItem = new JMenuItem("Lemmatize");
		lemmatizeItem.addActionListener(e -> showLemmatizationSidebar());
		JMenuItem posTagItem = new JMenuItem("POS Tag");
		posTagItem.addActionListener(e -> showPOSTaggingSidebar());

		contextMenu.add(transliterateItem);
		contextMenu.add(lemmatizeItem);
		contextMenu.add(posTagItem);
		fileContentArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					contextMenu.show(fileContentArea, e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					contextMenu.show(fileContentArea, e.getX(), e.getY());
			}
		});
		
		pageId = blFacade.getPageID(fileDTO.getId(), currentPage);
		transliterationPanel = new TransliterationPanel(blFacade, pageId, currentPage);
		transliterationPanel.setVisible(false);
		add(transliterationPanel, BorderLayout.EAST);

		lemmatizationPanel = new LemmatizationPanel(blFacade, pageId);
		lemmatizationPanel.setVisible(false);
		
		add(lemmatizationPanel, BorderLayout.EAST);
		posTaggingPanel = new POSTaggingPanel(blFacade, pageId);
		posTaggingPanel.setVisible(false);
		add(posTaggingPanel, BorderLayout.EAST);

		updateButton = new JButton("Update");
		updateButton.setBackground(new Color(138, 83, 43));
		updateButton.setForeground(Color.WHITE);
		updateButton.addActionListener(e -> loadWriteMode());

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(updateButton, BorderLayout.WEST);
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
		buttonPanel.add(navigationPanel, BorderLayout.EAST);

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

	private void showTransliterationSidebar() {
		String selectedText = fileContentArea.getSelectedText();
		if (selectedText != null) {
			transliterationPanel.textPane.setText(selectedText);
			transliterationPanel.performTransliteration(selectedText);
			transliterationPanel.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Please select text for transliteration.", "No Text Selected",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	private void showLemmatizationSidebar() {
	    if (lemmatizationPanel == null) {
	        return;
	    }

	    String selectedText = fileContentArea.getSelectedText();
	    if (selectedText != null) {
	        // Tokenize selected text
	        String[] selectedWords = selectedText.split("\\s+");

	        // Fetch lemmatization results
	        LinkedList<LemmatizationDTO> lemmatizationResults = blFacade.getLemmatizationForPage(pageId);
//	        if (lemmatizationResults == null || lemmatizationResults.isEmpty()) {
//	            System.out.println("Debug: No lemmatization results found for pageId = " + pageId);
//	        } else {
//	            System.out.println("Debug: Lemmatization results retrieved for pageId = " + pageId);
//	            for (LemmatizationDTO dto : lemmatizationResults) {
//	                System.out.println("Debug: Word = " + dto.getWord() + ", Lemma = " + dto.getLemma() + ", Root = " + dto.getRoot());
//	            }
//	        }

	        // Build table content
	        StringBuilder resultsBuilder = new StringBuilder("<html><body><table border='1' style='width:100%;'>");
	        resultsBuilder.append("<tr><th>Word</th><th>Lemma</th><th>Root</th></tr>");

	        for (LemmatizationDTO dto : lemmatizationResults) {
	            for (String word : selectedWords) {
	            	if (isWordInSelectedText(dto.getWord(), word)) {
	                    resultsBuilder.append("<tr>")
	                            .append("<td>").append(dto.getWord()).append("</td>")
	                            .append("<td>").append(dto.getLemma()).append("</td>")
	                            .append("<td>").append(dto.getRoot()).append("</td>")
	                            .append("</tr>");
	                }
	            }
	        }
	        

	        resultsBuilder.append("</table></body></html>");
	        //System.out.println("Filtered Lemmatization Results: " + resultsBuilder.toString());
	        
	        lemmatizationPanel.setContent(resultsBuilder.toString());
	        setComponentZOrder(lemmatizationPanel, 0);
	        lemmatizationPanel.setVisible(true);
	        lemmatizationPanel.repaint();
	    } else {
	        JOptionPane.showMessageDialog(this, "Please select text for lemmatization.", "No Text Selected",
	                JOptionPane.WARNING_MESSAGE);
	    }
	}


	private void showPOSTaggingSidebar() {
	    if (posTaggingPanel == null) {
	        return;
	    }

	    String selectedText = fileContentArea.getSelectedText();
	    if (selectedText != null) {
	        // Tokenize selected text
	        String[] selectedWords = selectedText.split("\\s+");

	        // Fetch POS tagging results
	        LinkedList<POSTaggingDTO> posTaggingResults = blFacade.getPOSTaggingForPage(pageId);
//	        if (posTaggingResults == null || posTaggingResults.isEmpty()) {
//	            System.out.println("Debug: No POSTagging results found for pageId = " + pageId);
//	        } else {
//	            System.out.println("Debug: POSTagging results retrieved for pageId = " + pageId);
//	            for (POSTaggingDTO dto : posTaggingResults) {
//	                System.out.println("Debug: Word = " + dto.getWord() + ", POSTag = " + dto.getPosTag());
//	            }
//	        }
	        // Build table content
	        StringBuilder resultsBuilder = new StringBuilder("<html><body><table border='1' style='width:100%;'>");
	        resultsBuilder.append("<tr><th>Word</th><th>POS Tag</th></tr>");

	        for (POSTaggingDTO dto : posTaggingResults) {
	            for (String word : selectedWords) { // or ignore this lopoop kia mtlb?
	            	// ye ana tha wahan
	            	if (isWordInSelectedText(dto.getWord(), word)) {
	                    resultsBuilder.append("<tr>")
	                            .append("<td>").append(dto.getWord()).append("</td>")
	                            .append("<td>").append(dto.getPosTag()).append("</td>")
	                            .append("</tr>");
	                }
	            }
	        }

	        resultsBuilder.append("</table></body></html>");
	        //System.out.println("Filtered POS Tagging Results: " + resultsBuilder.toString());
	        posTaggingPanel.setContent(resultsBuilder.toString());
	        setComponentZOrder(posTaggingPanel, 0);
	        posTaggingPanel.setVisible(true);
	    } else {
	        JOptionPane.showMessageDialog(this, "Please select text for POS tagging.", "No Text Selected",
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
	private String normalizeText(String text) {
	    return text.replaceAll("[\\u064B-\\u0652]", "") // Remove diacritics
	               .replaceAll("[^\\p{L}\\p{N}\\s]", "") // Remove non-letters/numbers
	               .trim(); // Trim whitespace
	}
	private boolean isWordInSelectedText(String word, String selectedText) {
	    
	    String token = selectedText;
	         
//	        System.out.println(token + "  " + word);
	        String normalizedSearchWord = normalizeText(word);
	        String normalizedtoken = normalizeText(token);
	        if (normalizedtoken.contains(normalizedSearchWord)) {
//	        	System.out.println("Im Here");
	            return true;
	        }
	    
	    return false;
	}

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