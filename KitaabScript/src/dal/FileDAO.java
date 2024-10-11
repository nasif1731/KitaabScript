package dal;

import dto.FileDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    public List<FileDTO> getAllFiles() {
        List<FileDTO> fileDetails = new ArrayList<>();
        String query = "SELECT filename, updated_at, language, hash, word_count FROM text_files"; 

        try (Connection conn = DatabaseConnection.getConnection();
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

    public FileDTO getOneFile(String fileName) throws SQLException {
        String query = "SELECT content, language, hash, word_count, updated_at FROM text_files WHERE filename = ?"; 
        FileDTO fileDTO = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fileName); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String content = rs.getString("content"); 
                String language = rs.getString("language");
                String hash = rs.getString("hash");
                int wordCount = rs.getInt("word_count");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                fileDTO = new FileDTO(fileName, content, language, hash, wordCount);
                fileDTO.setUpdatedAt(updatedAt); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }

        return fileDTO;
    }

    public int getWordCount(String fileName) {
        int wordCount = 0;
        String query = "SELECT word_count FROM text_files WHERE filename = ?"; 

        try (Connection conn = DatabaseConnection.getConnection();
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
}
