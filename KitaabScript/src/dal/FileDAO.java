package dal;

import dto.FileDTO;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    public List<FileDTO> getAllFiles() {
        List<FileDTO> fileDetails = new ArrayList<>();
        String query = "SELECT filename, updated_at FROM text_files"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String fileName = rs.getString("filename"); 
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                
                FileDTO fileDTO = new FileDTO(0, fileName, null, null, null, updatedAt, null, 0); 
                fileDetails.add(fileDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fileDetails;
    }

    public FileDTO getOneFile(String fileName) throws SQLException {
        String query = "SELECT content FROM text_files WHERE filename = ?"; 
        FileDTO fileDTO = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fileName); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fileContent = rs.getString("content"); 

                fileDTO = new FileDTO(0, fileName, null, null, null, null, null, 0);
                fileDTO.setContent(fileContent); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        }

        return fileDTO;
    }


    public int getWordCount(String fileName) {
        int wordCount = 0;
        String query = "SELECT word_count FROM text_files WHERE filename = ?"; // Corrected column name

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
