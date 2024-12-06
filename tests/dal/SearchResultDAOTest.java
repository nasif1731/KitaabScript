
package dal;

import dto.SearchResultDTO;
import util.MockDatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchResultDAOTest {

    private Connection conn;
    private SearchResultDAO searchResultDAO;
    private PaginationDAO paginationDAO;
    private FileDAO fileDAO;

    @BeforeEach
    void setUp() throws SQLException {
        try {
            conn = MockDatabaseConnection.getInstance().getConnection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        paginationDAO = new PaginationDAO(conn);
        fileDAO = new FileDAO(conn);
        searchResultDAO = new SearchResultDAO(paginationDAO, fileDAO, conn);

        // Insert initial test data
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO text_files (id, filename) VALUES (?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "file1");
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setInt(2, 1);
            stmt.setInt(3, 1);
            stmt.setString(4, "This is a test content with the keyword.");
            stmt.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Clean up database after each test
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM pagination")) {
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM text_files")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testSearch_EmptyKeyword() {
        List<SearchResultDTO> result = searchResultDAO.search("");
        
        assertTrue(result.isEmpty(), "Search result should be empty for empty keyword");
    }

    @Test
    void testSearch_KeywordNotFound() {
        List<SearchResultDTO> result = searchResultDAO.search("nonexistent");
        assertTrue(result.isEmpty(), "Search result should be empty when keyword is not found");
    }

    @Test
    void testSearch_KeywordFound() {
        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertFalse(result.isEmpty(), "Search result should not be empty when keyword is found");
        assertEquals(1, result.size(), "There should be 1 result");
        SearchResultDTO searchResult = result.get(0);
        assertEquals("file1", searchResult.getFileName(), "File name should match");
        assertEquals(1, searchResult.getPageNumber(), "Page number should match");
        assertEquals("keyword", searchResult.getMatchedWord(), "Matched word should be 'keyword'");
    }

    @Test
    void testSearch_MultipleMatches() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 2);
            stmt.setInt(2, 1);
            stmt.setInt(3, 2);
            stmt.setString(4, "Another page with keyword and another keyword.");
            stmt.executeUpdate();
        }

        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertEquals(2, result.size(), "There should be 2 results for multiple matches");
    }

    @Test
    void testSearch_NoFiles() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM text_files")) {
            stmt.executeUpdate();
        }

        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertTrue(result.isEmpty(), "Search result should be empty when no files exist");
    }

    @Test
    void testSearch_EmptyPageContent() throws SQLException {
    	
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 2);
            stmt.setInt(2, 1);
            stmt.setInt(3, 2);
            stmt.setString(4, "");
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM text_files")) {
            stmt.executeUpdate();
        }
        
        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertTrue(result.isEmpty(), "Search result should be empty when page content is empty");
    }

    @Test
    void testSearch_MultiplePages() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 2);
            stmt.setInt(2, 1);
            stmt.setInt(3, 2);
            stmt.setString(4, "This is another test content with the keyword on page 2.");
            stmt.executeUpdate();
        }

        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertEquals(2, result.size(), "There should be 2 results from multiple pages");
    }

    @Test
    void testSearch_KeywordInMultipleFiles() throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO text_files (id, filename) VALUES (?, ?)")) {
            stmt.setInt(1, 2);
            stmt.setString(2, "file2");
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, 3);
            stmt.setInt(2, 2);
            stmt.setInt(3, 1);
            stmt.setString(4, "This is content with the keyword in another file.");
            stmt.executeUpdate();
        }

        List<SearchResultDTO> result = searchResultDAO.search("keyword");
        assertEquals(2, result.size(), "There should be results from multiple files");
    }
}
