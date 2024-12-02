package dal;

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
    public List<PageDTO> paginateContent(int fileId, String content) {
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

        String updateQuery = "UPDATE pagination SET page_content = ? WHERE text_file_id = ? AND page_number = ?";
        String insertQuery = "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            for (PageDTO page : paginatedContent) {
                if (contentExistsForFile(page.getTextFileId(), page.getPageNumber())) {
                    updateStmt.setString(1, page.getPageContent());
                    updateStmt.setInt(2, page.getTextFileId());
                    updateStmt.setInt(3, page.getPageNumber());
                    updateStmt.executeUpdate();
                } else {
                    insertStmt.setInt(1, page.getTextFileId());
                    insertStmt.setInt(2, page.getPageNumber());
                    insertStmt.setString(3, page.getPageContent());
                    insertStmt.addBatch();
                }
            }

            insertStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting or updating page content: " + e.getMessage());
        }
    }

    @Override
    public int getPageID(int fileId, int pageNumber) {
        String query = "SELECT id FROM pagination WHERE text_file_id = ? AND page_number = ?";
        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
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
        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
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

        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

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
        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
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
    @Override
    public List<PageDTO> getPagesByTextFileId(int textFileId) {
        List<PageDTO> pages = new ArrayList<>();
        String sql = "SELECT * FROM pagination WHERE text_file_id = ? ORDER BY page_number";

        try (Connection conn = DatabaseConnection.getInstance(false).getConnection();
        		PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, textFileId); 
            ResultSet resultSet = statement.executeQuery(); 

           
            while (resultSet.next()) {
                int id = resultSet.getInt("id"); 
                int pageNumber = resultSet.getInt("page_number"); 
                String content = resultSet.getString("page_content"); 
                pages.add(new PageDTO(id, textFileId, pageNumber, content)); 
            }
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
            System.err.println("Error retrieving pages " + e.getMessage());
        }
        return pages; 
    }
}

