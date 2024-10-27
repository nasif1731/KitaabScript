package dal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dto.FileDTO;
import util.DatabaseConnection;
import util.HashGenerator;

public class FileImportDAO implements IFileImportDAO{

	@Override
	public String importFile(String filePath) {
        File file = new File(filePath);
        String content;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
            String hash = HashGenerator.generateHashFromContent(content);

            if (doesHashExist(hash)) {
                return "Cannot import: A similar file already exists in the database.";
            }

            String language = determineFileLanguage(content);
            int wordCount = countWords(content);

            FileDTO fileDTO = new FileDTO(file.getName(), content, language, hash, wordCount);
            insertFileIntoDatabase(fileDTO);
            return "File imported successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading the file: " + e.getMessage();
        }
    }
	@Override
	public List<String> bulkImportFiles(List<String> filePaths) {
        List<String> messages = new ArrayList<>();
        for (String filePath : filePaths) {
            messages.add(importFile(filePath));
        }
        return messages;
    }
	@Override
	public void insertFileIntoDatabase(FileDTO file) {
        String query = "INSERT INTO text_files (filename, content, language, hash, word_count) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, file.getFilename());
            stmt.setString(2, file.getContent());
            stmt.setString(3, file.getLanguage());
            stmt.setString(4, file.getHash());
            stmt.setInt(5, file.getWordCount());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting "+file.getFilename()+" file into database: " + e.getMessage());
        }
    }
	@Override
	public boolean doesHashExist(String hash) {
        String query = "SELECT COUNT(*) FROM text_files WHERE hash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hash);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking hash existence: " + e.getMessage());
            return false; 
        }
    }
	private String determineFileLanguage(String content) {
        try {
            if (isUrdu(content)) {
                return "Urdu";
            } else if (isArabic(content)) {
                return "Arabic";
            } else {
                return "English";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown"; 
        }
    }

	private boolean isUrdu(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch == 'ٹ' || ch == 'ڈ' || ch == 'ڑ' || ch == 'ں' || ch == 'ے' || ch == 'ؤ' || ch == 'ۓ') {
				return true;
			}
		}
		return false;
	}

	private boolean isArabic(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch >= 0x0600 && ch <= 0x06FF && !isUrdu(String.valueOf(ch))) {
				return true;
			}
		}
		return false;
	}

	private int countWords(String content) {
		if (content.isEmpty()) {
			return 0;
		} else {
			return content.split("\\s+").length;
		}
	}

}
