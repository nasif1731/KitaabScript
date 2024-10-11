package dal;

import dto.FileDTO;
import util.DatabaseConnection;
import util.HashGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
	private int countWords(String content) {
        return content.isEmpty() ? 0 : content.split("\\s+").length;
    }

    public void createFile(String name, String content) 
            throws IOException, NoSuchAlgorithmException, SQLException {

        String hash;
        int count = countWords(content);

        if (content.isEmpty()) {
            hash = HashGenerator.generateHashFromFile(name);
            count = 0;
        } else {
            hash = HashGenerator.generateHashFromContent(content);
        }

        
        String insertSQL = "INSERT INTO text_files (filename, content, hash, word_count, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, name);     
            stmt.setString(2, content);
            stmt.setString(3, hash);
            stmt.setInt(4, count);
           
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Failed to create the file in the database.");
            }
        }
    }

    public void deleteFile(String name) throws IOException, SQLException {
        String deleteSQL = "DELETE FROM text_files WHERE filename = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

            stmt.setString(1, name);      
            int rowsAffected = stmt.executeUpdate(); 

            if (rowsAffected == 0) {
                throw new SQLException("No file found with the name: " + name);
            }
        }
    }

    public void updateFile(String name, String newContent) 
            throws IOException, NoSuchAlgorithmException, SQLException {

        File file = new File(name);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(newContent);
        }

        int count = countWords(newContent);

        
        String updateSQL = "UPDATE text_files SET content = ?, word_count = ?, updated_at = NOW() WHERE filename = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

           
            stmt.setString(1, newContent);
            stmt.setInt(2, count);
            stmt.setString(3, name);
            int rowsAffected = stmt.executeUpdate(); 

            if (rowsAffected == 0) {
                throw new SQLException("No file found with the name: " + name);
            }
        }
    }

    public String createdAt(String name) throws SQLException {
        String selectSQL = "SELECT created_at FROM text_files WHERE filename = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("created_at");
            } else {
                throw new SQLException("No file found with the name: " + name);
            }
        }
    }

    public String updatedAt(String name) throws SQLException {
        String selectSQL = "SELECT updated_at FROM text_files WHERE filename = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSQL)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("updated_at");
            } else {
                throw new SQLException("No file found with the name: " + name);
            }
        }
    }
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
