package dal;

import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;
import dto.LemmatizationDTO;
import util.MockDatabaseConnection;
import static org.junit.jupiter.api.Assertions.*;

public class LemmatizationDAOTests {

    private LemmatizationDAO lemmatizationDAO;

    @BeforeEach
    public void setUp() throws SQLException, InterruptedException {
        Connection mockConnection = MockDatabaseConnection.getInstance().getConnection();
        lemmatizationDAO = new LemmatizationDAO(mockConnection);
        clearDatabase();
        setUpPaginationData();
    }

    private void clearDatabase() {
        try (Connection conn = MockDatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM lemmatization");
            stmt.executeUpdate("DELETE FROM pagination");
        } catch (Exception e) {
            throw new RuntimeException("Error clearing test database.", e);
        }
    }

    private void setUpPaginationData() throws InterruptedException {
        try (Connection conn = MockDatabaseConnection.getInstance().getConnection()) {
            String insertPaginationQuery = "INSERT INTO pagination (id, text_file_id, page_number, page_content) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertPaginationQuery)) {
                stmt.setInt(1, 1);
                stmt.setInt(2, 1);
                stmt.setInt(3, 1);
                stmt.setString(4, "Sample page content");
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting pagination data.", e);
        }
    }

    @Test
    public void testAddLemmatizationSuccess() {
        LemmatizationDTO dto = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
        lemmatizationDAO.addLemmatization(dto);
        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
        assertEquals(1, results.size());
        assertEquals("كلمة1", results.get(0).getWord());
    }

    @Test
    public void testGetLemmatizationForPageWithData() throws SQLException {
        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
        LemmatizationDTO dto2 = new LemmatizationDTO(2, 1, "كلمة2", "تصريف2", "جذر2");
        lemmatizationDAO.addLemmatization(dto1);
        lemmatizationDAO.addLemmatization(dto2);
        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
        assertEquals(2, results.size());
        assertEquals("كلمة1", results.get(0).getWord());
        assertEquals("كلمة2", results.get(1).getWord());
    }

    @Test
    public void testIsLemmatizationSavedForPageTrue() throws SQLException {
        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
        lemmatizationDAO.addLemmatization(dto1);

        String content = "كلمة1";
        assertTrue(lemmatizationDAO.isLemmatizationSavedForPage(1, content));
    }

    @Test
    public void testIsLemmatizationSavedForPageFalse() throws SQLException {
        assertFalse(lemmatizationDAO.isLemmatizationSavedForPage(1, "كلمة3"));
    }
}
