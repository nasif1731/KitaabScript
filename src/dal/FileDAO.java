package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dto.FileDTO;
import dto.PageDTO;
import util.DatabaseConnection;
import util.HashGenerator;



public class FileDAO  implements IFileDAO{
	private final PaginationDAO paginationDAO;
	private final Connection conn;
	public FileDAO(Connection connection) {
		this.conn=connection;
		this.paginationDAO = new PaginationDAO(conn);
		
	}

	private int countWords(String content) {
	    if (content == null || content.isEmpty()) {
	        return 0;
	    }

	    int numThreads = Runtime.getRuntime().availableProcessors();
	    String[] words = content.split("\\s+");
	    int wordDivision = (int) Math.ceil((double) words.length / numThreads);

	    // Shared total word count
	    int[] totalWordCount = {0};
	    Thread[] threads = new Thread[numThreads];

	    //Creating and Starting Thread
	    for (int i = 0; i < numThreads; i++) {
	        int start = i * wordDivision;
	        int end = Math.min(start + wordDivision, words.length);

	        threads[i] = new Thread(() -> {
	            int wordCount = 0;
	            for (int j = start; j < end; j++) {
	                if (!words[j].isBlank()) {
	                	wordCount++;
	                }
	            }
	            synchronized (totalWordCount) {
	                totalWordCount[0] += wordCount;
	            }
	        });
	        threads[i].start();
	    }

	    for (Thread thread : threads) {
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

	    return totalWordCount[0];
	}

	@Override
	public PageDTO createFile(String name, String content) {
	    try {
	        String hash;
	        int count = countWords(content);

	        if (content.isEmpty()) {
	            hash = HashGenerator.generateHashFromFile(name);
	            count = 0;
	        } else {
	            hash = HashGenerator.generateHashFromContent(content);
	        }

	        String insertSQL = "INSERT INTO text_files (filename, hash, word_count, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW())";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

	            stmt.setString(1, name);
	            stmt.setString(2, hash);
	            stmt.setInt(3, count);
	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected == 0) {
	                return null;
	            }

	            ResultSet rs = stmt.getGeneratedKeys();
	            int fileId = 0;
	            if (rs.next()) {
	                fileId = rs.getInt(1);
	            }

	            if (fileId == 0) {
	                return null;
	            }

	            List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, content);
	          //  System.out.println("Paginated Content: " + paginatedContent.size()); 
	            
	            if (paginatedContent.isEmpty()) {
	                return null; 
	            }

	            paginationDAO.insertContent(paginatedContent);
	            return paginatedContent.get(0);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	

	@Override
	public void deleteFile(String name) {
	    try {
	        String deleteSQL = "DELETE FROM text_files WHERE filename = ?";

	        try (
	             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

	            stmt.setString(1, name);
	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected == 0) {
	                System.err.println("No file found with the name: " + name);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

//	@Override
	public PageDTO updateFile(String name, int pageNumber, String newContent) {
	    try {
	        int count = countWords(newContent);

	        String updateSQL = "UPDATE text_files SET word_count = ?, updated_at = NOW() WHERE filename = ?";

	        try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
	            stmt.setInt(1, count);
	            stmt.setString(2, name);
	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected == 0) {
	                System.err.println("No file found with the name: " + name);
	                return null;
	            }

	            int fileId = fetchFileIdByName(name);
	            List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, newContent);

	            String updatePaginationSQL = "UPDATE pagination SET page_content = ? WHERE text_file_id = ? AND page_number = ?";
	            String insertPaginationSQL = "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (?, ?, ?)";

	            try (PreparedStatement updateStmt = conn.prepareStatement(updatePaginationSQL);
	                 PreparedStatement insertStmt = conn.prepareStatement(insertPaginationSQL)) {

	                for (PageDTO page : paginatedContent) {
	                    updateStmt.setString(1, page.getPageContent());
	                    updateStmt.setInt(2, fileId);
	                    updateStmt.setInt(3, page.getPageNumber());
	                    int updatedRows = updateStmt.executeUpdate();

	                    if (updatedRows == 0) {
	                        insertStmt.setInt(1, fileId);
	                        insertStmt.setInt(2, page.getPageNumber());
	                        insertStmt.setString(3, page.getPageContent());
	                        insertStmt.addBatch();
	                    }
	                }

	                insertStmt.executeBatch();
	            }

	            return paginatedContent.get(0);

	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public String createdAt(String name) {
        String createdAt = null;
        try {
            String selectSQL = "SELECT created_at FROM text_files WHERE filename = ?";
            try (
                 PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    createdAt = rs.getString("created_at");
                } else {
                    System.err.println("No file found with the name: " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createdAt;
    }
	@Override
	public String updatedAt(String name) {
        String updatedAt = null;
        try {
            String selectSQL = "SELECT updated_at FROM text_files WHERE filename = ?";
            try (
                 PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    updatedAt = rs.getString("updated_at");
                } else {
                    System.err.println("No file found with the name: " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedAt;
    }
	@Override
    public List<FileDTO> getAllFiles() {
        List<FileDTO> fileDetails = new ArrayList<>();
        String query = "SELECT filename, updated_at, language, hash, word_count FROM text_files"; 

        try (
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String fileName = rs.getString("filename"); 
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                String language = rs.getString("language");
                String hash = rs.getString("hash");
                int wordCount = rs.getInt("word_count");

                FileDTO fileDTO = new FileDTO(fileName, null, language, hash, wordCount); 
                fileDTO.setUpdatedAt(updatedAt); 
                fileDetails.add(fileDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileDetails;
    }
	@Override
	public FileDTO getOneFile(String fileName) {
	    String query = "SELECT text_files.id, text_files.language, text_files.hash, text_files.word_count, " +
	                   "text_files.updated_at, pagination.page_content " +
	                   "FROM text_files " +
	                   "JOIN pagination ON pagination.text_file_id = text_files.id " +
	                   "WHERE text_files.filename = ? " +
	                   "ORDER BY pagination.page_number"; 

	    FileDTO fileDTO = null;
	    StringBuilder contentBuilder = new StringBuilder();

	    try (
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        stmt.setString(1, fileName);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            if (fileDTO == null) {  
	                int id = rs.getInt("id");
	                String language = rs.getString("language");
	                String hash = rs.getString("hash");
	                int wordCount = rs.getInt("word_count");
	                Timestamp updatedAt = rs.getTimestamp("updated_at");

	                fileDTO = new FileDTO(fileName, null, language, hash, wordCount);
	                fileDTO.setId(id);
	                fileDTO.setUpdatedAt(updatedAt);
	            }

	            String pageContent = rs.getString("page_content");
	            contentBuilder.append(pageContent);
	        }

	        if (fileDTO != null) {
	            fileDTO.setContent(contentBuilder.toString()); 
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.err.println("Error retrieving file: " + e.getMessage());
	    }

	    return fileDTO;
	}
	
	@Override
    public int getWordCount(String fileName) {
        int wordCount = 0;
        String query = "SELECT word_count FROM text_files WHERE filename = ?"; 

        try (
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fileName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                wordCount = rs.getInt("word_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordCount;
    }
	@Override
	public int fetchFileIdByName(String name) throws SQLException {
	    String query = "SELECT id FROM text_files WHERE filename = ?";
	    try (
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, name);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("id");
	            } else {
	                throw new SQLException("No file ID found for filename: " + name);
	            }
	        }
	    }
	 	
}
	 @Override
	  public String getFileName(int fileId) {
	      String fileName = null;
	      String query = "SELECT filename FROM text_files WHERE id = ?"; 

	      try (
	           PreparedStatement stmt = conn.prepareStatement(query)) {
	          stmt.setInt(1, fileId);
	          try (ResultSet rs = stmt.executeQuery()) {
	              if (rs.next()) {
	                  fileName = rs.getString("filename");
	              }
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	          throw new RuntimeException("Error retrieving file name", e);
	      }

	      return fileName;
	  
}
   
   @Override
   public List<Integer> getAllFileIds() {
       List<Integer> fileIds = new ArrayList<>();
       String query = "SELECT id FROM text_files"; 

       try (
       		PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
           while (rs.next()) {
               fileIds.add(rs.getInt("id"));
           }
       } catch (SQLException e) {
           e.printStackTrace();
           throw new RuntimeException("Error retrieving file IDs", e);
       }

       return fileIds;
   }


}