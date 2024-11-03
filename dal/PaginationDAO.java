package dal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.PageDTO;
import util.DatabaseConnection;

public class PaginationDAO implements IPaginationDAO {

	@Override
	public List<PageDTO> paginateContent(int fileId,String content) {
	    List<PageDTO> pages = new ArrayList<>();
	    int wordLimit = 400; 
	    int wordsPerLine = 10; 
	    String[] words = content.split("\\s+");
	    int pageNumber = 1;
	    StringBuilder pageContent = new StringBuilder();
	    int wordCount = 0; 

	    for (int i = 0; i < words.length; i++) {
	        pageContent.append(words[i]).append(" ");
	        wordCount++;

	        if (wordCount % wordsPerLine == 0) {
	            pageContent.append("\n"); 
	        }

	        if (wordCount >= wordLimit || i == words.length - 1) {
	            pages.add(new PageDTO(fileId, pageNumber++, pageContent.toString().trim()));
	            pageContent.setLength(0); 
	            wordCount = 0; 
	        }
	    }

	    return pages;
	}



    @Override
    public void insertContent(List<PageDTO> paginatedContent) {
        if (paginatedContent == null || paginatedContent.isEmpty()) {
            System.err.println("Error: no content to insert.");
            return;
        }

        String query = "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        	stmt.clearBatch();
            for (PageDTO page : paginatedContent) {
                stmt.setInt(1, page.getTextFileId());
                stmt.setInt(2, page.getPageNumber());
                stmt.setString(3, page.getPageContent());
                stmt.addBatch();  
            }

            stmt.executeBatch();
           // conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getPageID(int fileId, int pageNumber) {
        String query = "SELECT id FROM pagination WHERE text_file_id = ? AND page_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileId);
            stmt.setInt(2, pageNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving page ID for file ID " + fileId + ", page number " + pageNumber + ": " + e.getMessage());
        }
        return -1; 
    }


    @Override
    public boolean contentExistsForFile(int fileId, int pageNumber) {
        String query = "SELECT 1 FROM pagination WHERE text_file_id = ? AND page_number = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, fileId);
            stmt.setInt(2, pageNumber);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking existing content for file ID " + fileId + ", page number " + pageNumber + ": " + e.getMessage());
        }
        return false;
    }
    @Override
    public int getTotalPages(int fileId) {
        int totalPages = 0;

        String query = "SELECT COUNT(p.page_number) AS total_pages FROM pagination p " +
                       "JOIN text_files tf ON p.text_file_id = tf.id " +
                       "WHERE tf.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, fileId); 

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalPages = resultSet.getInt("total_pages");
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return totalPages;
    }

    @Override
    public PageDTO getPage(int fileId, int pageNumber) {
        String query = "SELECT page_content, id FROM pagination WHERE text_file_id = ? AND page_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, fileId);
            stmt.setInt(2, pageNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String pageContent = rs.getString("page_content");
                int pageId = rs.getInt("id");

                return new PageDTO(pageId, fileId, pageNumber, pageContent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving page content for file ID " + fileId + ", page number " + pageNumber + ": " + e.getMessage());
        }
        return null;
    }







}