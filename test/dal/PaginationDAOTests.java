package dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dto.PageDTO;
import util.MockDatabaseConnection;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginationDAOTests {

    private Connection conn;
    private PaginationDAO paginationDAO;

    @BeforeEach
    void setUp() throws SQLException, InterruptedException {
        conn = MockDatabaseConnection.getInstance().getConnection();
        paginationDAO = new PaginationDAO(conn);

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO text_files (id, filename, created_at, updated_at, hash, word_count, language) " +
                "VALUES (?, ?, NOW(), NOW(), ?, ?, ?) ON DUPLICATE KEY UPDATE id = id")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "Test File");
            stmt.setString(3, "test_hash");
            stmt.setInt(4, 100);
            stmt.setString(5, "English");
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM pagination WHERE text_file_id = 1")) {
            stmt.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM pagination WHERE text_file_id = 1");
            stmt.executeUpdate("DELETE FROM text_files WHERE id = 1");
        }
    }


    @Test
    void testPaginateContentWithEmptyContent() {
        String content = "";
        int fileId = 1;
        List<PageDTO> pages = paginationDAO.paginateContent(fileId, content);
        assertNotNull(pages);
        assertTrue(pages.isEmpty());
    }

    @Test
    void testPaginateContentWithOnePageContent() {
        String content = "This is a single page of content that fits within the word limit.";
        int fileId = 1;
        List<PageDTO> pages = paginationDAO.paginateContent(fileId, content);
        assertNotNull(pages);
        assertEquals(1, pages.size());
    }

    @Test
    void testPaginateContentWithMultiplePages() {
        String content = "This is a longer piece of content that will span across multiple pages. "
                + "Each page will contain up to 400 words, and each line can have up to 10 words. "
                + "We will continue to add enough words to ensure that multiple pages are required.";
        int fileId = 1;
        List<PageDTO> pages = paginationDAO.paginateContent(fileId, content);
        assertNotNull(pages);
    }

    @Test
    void testInsertContent() throws SQLException {
        int fileId = 1;
        List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, "This is some content to insert.");
        paginationDAO.insertContent(paginatedContent);

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pagination WHERE text_file_id = ?")) {
            stmt.setInt(1, fileId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("This is some content to insert.", rs.getString("page_content"));
        }
    }

    @Test
    void testInsertBatchContent() throws SQLException {
        int fileId = 1;
        List<PageDTO> paginatedContent = paginationDAO.paginateContent(fileId, "This is a batch of content to insert across multiple pages.");
        paginationDAO.insertContent(paginatedContent);

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pagination WHERE text_file_id = ?")) {
            stmt.setInt(1, fileId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
        }
    }

    @Test
    void testGetPageID() throws SQLException {
        int fileId = 1;
        paginationDAO.insertContent(paginationDAO.paginateContent(fileId, "Page content for page ID retrieval."));
        int pageId = paginationDAO.getPageID(fileId, 1);
        assertTrue(pageId > 0);
    }

    @Test
    void testGetPageIDForNonExistingPage() throws SQLException {
        int fileId = 1;
        int pageId = paginationDAO.getPageID(fileId, 999); 
        assertEquals(-1, pageId);
    }

    @Test
    void testGetTotalPages() throws SQLException {
        int fileId = 1;
        paginationDAO.insertContent(paginationDAO.paginateContent(fileId, "Test content for total page count."));
        int totalPages = paginationDAO.getTotalPages(fileId);
        assertEquals(1, totalPages);
    }

    @Test
    void testGetTotalPagesForFileWithNoPages() throws SQLException {
        int fileId = 1;
        int totalPages = paginationDAO.getTotalPages(fileId);
        assertEquals(0, totalPages);
    }

    @Test
    void testGetPage() throws SQLException {
        int fileId = 1;
        paginationDAO.insertContent(paginationDAO.paginateContent(fileId, "Page content for page retrieval."));
        PageDTO page = paginationDAO.getPage(fileId, 1);
        assertNotNull(page);
        assertEquals("Page content for page retrieval.", page.getPageContent());
    }

    @Test
    void testGetPageForNonExistingPage() throws SQLException {
        int fileId = 1;
        PageDTO page = paginationDAO.getPage(fileId, 999);
        assertNull(page);
    }

    @Test
    void testGetPagesByTextFileId() throws SQLException {
        int fileId = 1;
        paginationDAO.insertContent(paginationDAO.paginateContent(fileId, "Multiple pages content for retrieval."));
        List<PageDTO> pages = paginationDAO.getPagesByTextFileId(fileId);
        assertNotNull(pages);
        assertFalse(pages.isEmpty());
    }
}
