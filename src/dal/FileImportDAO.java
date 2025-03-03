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
import dto.PageDTO;
import util.HashGenerator;

public class FileImportDAO implements IFileImportDAO{

	  private final PaginationDAO paginationDAO;
	  private Connection conn;
	    public FileImportDAO(Connection conn) {
	    	this.conn=conn;
	        this.paginationDAO = new PaginationDAO(conn);
	        
	    }

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

	            FileDTO fileDTO = new FileDTO(file.getName(), null, language, hash, wordCount);
	            int fileId = insertFileIntoDatabase(fileDTO,content);
				List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, content);

	            paginationDAO.insertContent(paginatedContent); 

	            return "File imported successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading the file: " + e.getMessage();
        }
    }
	    @Override
	    public List<String> bulkImportFiles(List<String> filePaths) {
	        List<Thread> threads = new ArrayList<>();
	        List<String> messages = new ArrayList<>();
	        List<String> sharedResults = new ArrayList<>(); 

	        for (String filePath : filePaths) {
	            Thread thread = new Thread(() -> {
	                String message = importFile(filePath);
	                synchronized (sharedResults) {
	                    sharedResults.add(message); 
	                }
	            });
	            threads.add(thread);
	            thread.start();
	        }

	        
	        for (Thread thread : threads) {
	            try {
	                thread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	                messages.add("Error waiting for thread: " + e.getMessage());
	            }
	        }

	       
	        messages.addAll(sharedResults);
	        return messages;
	    }
	@Override
	public int insertFileIntoDatabase(FileDTO file, String content) {
	    String query = "INSERT INTO text_files (filename, language, hash, word_count) VALUES (?, ?, ?, ?)";
	    try (
	         PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        stmt.setString(1, file.getFilename());
	        stmt.setString(2, file.getLanguage());
	        stmt.setString(3, file.getHash());
	        stmt.setInt(4, file.getWordCount());
	        stmt.executeUpdate();

	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                int fileId = rs.getInt(1);

	                List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, content);
	                paginationDAO.insertContent(paginatedContent);

	                return fileId;
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.err.println("Error inserting " + file.getFilename() + " file into database: " + e.getMessage());
	    }
	    return -1;
	}

	
	@Override
	public boolean doesHashExist(String hash) {
        String query = "SELECT COUNT(*) FROM text_files WHERE hash = ?";
        try (
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
	public String determineFileLanguage(String content) {
        try {
            if (isUrdu(content)) {
                return "Urdu";
            } else if (isArabic(content)) {
                return "Arabic";
            } else if(isEnglish(content)) {
                return "English";
            }
            else
            {
            	return "Unknown";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown"; 
        }
    }

	boolean isUrdu(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch == 'ٹ' || ch == 'ڈ' || ch == 'ڑ' || ch == 'ں' || ch == 'ے' || ch == 'ؤ' || ch == 'ۓ') {
				return true;
			}
		}
		return false;
	}

	 boolean isArabic(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch >= 0x0600 && ch <= 0x06FF && !isUrdu(String.valueOf(ch))) {
				return true;
			}
		}
		return false;
	}

	 int countWords(String content) {
		if (content.isEmpty()) {
			return 0;
		} else {
			return content.split("\\s+").length;
		}
	}
	 boolean isEnglish(String sentence) {
	        
	        return sentence.matches("^[A-Za-z0-9\\s.,!?\"'()]*$");
	    }

}
