package pl;

import bll.FileBO;
import dto.FileDTO;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FileDetailPanel extends JFrame {
	private JTextPane fileContentArea;
	private JButton updateButton;
	private JLabel wordCountLabel;
	private FileBO fileBO;
	private FileDTO fileDTO;

	public FileDetailPanel(String fileName) {
		fileBO = new FileBO();
		initializeUI();
		loadFileDetails(fileName);
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		setBackground(new Color(235, 224, 199));

		fileContentArea = new JTextPane();
		fileContentArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(fileContentArea);
		add(scrollPane, BorderLayout.CENTER);

		updateButton = new JButton("Update");
		updateButton.setBackground(new Color(138, 83, 43));
		updateButton.setForeground(Color.WHITE);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadWriteMode();
			}
		});

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(updateButton, BorderLayout.WEST);
		buttonPanel.setBackground(new Color(235, 224, 199));
		add(buttonPanel, BorderLayout.NORTH);

		wordCountLabel = new JLabel("Word Count: 0");
		wordCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(wordCountLabel, BorderLayout.SOUTH);
	}

	void loadWriteMode() {
		dispose();
		FileUpdatePanel dialog = new FileUpdatePanel(fileDTO.getFilename());
		dialog.setVisible(true);
	}

	private void loadFileDetails(String fileName) {
		try {

			fileDTO = fileBO.getOneFile(fileName);
			if (fileDTO != null) {

				StyledDocument doc = fileContentArea.getStyledDocument();
				SimpleAttributeSet rightAlign = new SimpleAttributeSet();
				StyleConstants.setAlignment(rightAlign, StyleConstants.ALIGN_RIGHT);
				doc.setParagraphAttributes(0, doc.getLength(), rightAlign, false);
				fileContentArea.setText(fileDTO.getContent());

				int wordCount = fileBO.getWordCount(fileDTO.getFilename());
				wordCountLabel.setText("Word Count: " + wordCount);
			} else {
				JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading file details: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
