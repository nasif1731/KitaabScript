package pl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bll.FileBO;

class CreateFileDialogue extends JDialog {
	private JTextField fileNameField;
	private JButton proceedButton;
	private MainScreen mainScreen;
	private FileBO fileBO;

	public CreateFileDialogue(MainScreen mainScreen, FileBO fileBO) {
		this.mainScreen = mainScreen;
		this.fileBO = fileBO;
		setTitle("Create New File");
		setModal(true);
		setSize(300, 150);
		setLocationRelativeTo(mainScreen);

		fileNameField = new JTextField(20);
		proceedButton = new JButton("Proceed");

		setLayout(new BorderLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("File Name:"));
		inputPanel.add(fileNameField);
		add(inputPanel, BorderLayout.CENTER);
		add(proceedButton, BorderLayout.SOUTH);

		proceedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = fileNameField.getText();
				if (!fileName.isEmpty()) {
					try {

						fileBO.createFile(fileName, "");

						setVisible(false);
						dispose();
						FileUpdatePanel fileUpdatePanel = new FileUpdatePanel(fileName,fileBO);
						fileUpdatePanel.setVisible(true);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(CreateFileDialogue.this,
								"Error creating file: " + ex.getMessage());
					}
				}
			}
		});
	}
}
