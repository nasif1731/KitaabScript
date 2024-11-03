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
import bll.FilePaginationBO;
import bll.IBLFacade;

class CreateFileDialogue extends JDialog {
	private JTextField fileNameField;
	private JButton proceedButton;
	private MainScreen mainScreen;
	private IBLFacade blFacade;

	public CreateFileDialogue(MainScreen mainScreen, IBLFacade blFacade) {
		this.mainScreen = mainScreen;
		this.blFacade = blFacade;
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

						blFacade.createFile(fileName, "");

						setVisible(false);
						dispose();
						FileUpdatePanel fileUpdatePanel = new FileUpdatePanel(fileName,blFacade);
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
