package pl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainScreen extends JFrame {

    private JButton openFileButton;
    private JButton createFileButton;
    private JButton importFileButton;

    public MainScreen() {
        setTitle("Kitaab Script");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(235, 224, 199)); 
        ImageIcon icon = new ImageIcon("resources/images/icon.png");
		setIconImage(icon.getImage());
        Font mughalFont = new Font("Serif", Font.BOLD, 24);
        Font gothicFont = loadGothicFont(55f);  

        JLabel headerLabel = new JLabel("Kitaab Script");
        headerLabel.setFont(gothicFont);
        headerLabel.setForeground(new Color(138, 83, 43));  
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(235, 224, 199));  
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        openFileButton = createStyledButton("Open File", mughalFont);
        createFileButton = createStyledButton("Create File", mughalFont);
        importFileButton = createStyledButton("Import File", mughalFont);

        buttonPanel.add(openFileButton);
        buttonPanel.add(createFileButton);
        buttonPanel.add(importFileButton);

        
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBackground(new Color(235, 224, 199));
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 20));  
        paddedPanel.add(buttonPanel, BorderLayout.CENTER);

        importFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImportFileDialogueBox dialog = new ImportFileDialogueBox(MainScreen.this);
                dialog.setVisible(true);
            }
        });

        getContentPane().add(headerLabel, BorderLayout.NORTH);
        getContentPane().add(paddedPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(new Dimension(200, 80));
        button.setBackground(new Color(138, 83, 43)); 
        button.setForeground(new Color(255, 244, 206)); 
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(138, 83, 43), 2)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private Font loadGothicFont(float size) {
        try {
            File fontFile = new File("resources/fonts/OldLondon.ttf"); 
            Font gothicFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return gothicFont.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Serif", Font.BOLD, 40);  
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainScreen mainFrame = new MainScreen();
                mainFrame.setVisible(true);
            }
        });
    }
}
