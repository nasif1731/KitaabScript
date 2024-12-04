package dal;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
    }

    private void clearDatabase() {
        try (Connection conn = MockDatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM lemmatization");
        } catch (Exception e) {
            throw new RuntimeException("Error clearing test database.", e);
        }
    }

//    @Test
//    public void testAddLemmatizationSuccess() {
//        LemmatizationDTO dto = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
//        lemmatizationDAO.addLemmatization(dto);
//        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
//        assertEquals(1, results.size());
//        assertEquals("كلمة1", results.get(0).getWord());
//    }

    @Test
    public void testAddLemmatizationFailure() {
        LemmatizationDTO dto = new LemmatizationDTO(1, 1, null, null, null);
        assertThrows(RuntimeException.class, () -> lemmatizationDAO.addLemmatization(dto));
    }

    @Test
    public void testGetLemmatizationForPageEmpty() {
        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
        assertTrue(results.isEmpty());
    }

//    @Test
//    public void testGetLemmatizationForPageWithData() throws SQLException {
//        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
//        LemmatizationDTO dto2 = new LemmatizationDTO(1, 1, "كلمة2", "تصريف2", "جذر2");
//
//        lemmatizationDAO.addLemmatization(dto1);
//        lemmatizationDAO.addLemmatization(dto2);
//
//        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
//        
//        assertEquals(2, results.size());
//        assertEquals("كلمة1", results.get(0).getWord());
//        assertEquals("كلمة2", results.get(1).getWord());
//    }
//
//    @Test
//    public void testIsLemmatizationSavedForPageTrue() throws SQLException {
//        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
//        LemmatizationDTO dto2 = new LemmatizationDTO(1, 1, "كلمة2", "تصريف2", "جذر2");
//        lemmatizationDAO.addLemmatization(dto1);
//        lemmatizationDAO.addLemmatization(dto2);
//
//        assertTrue(lemmatizationDAO.isLemmatizationSavedForPage(1, "كلمة1"));
//        assertTrue(lemmatizationDAO.isLemmatizationSavedForPage(1, "كلمة2"));
//    }

    @Test
    public void testIsLemmatizationSavedForPageFalse() throws SQLException {
        assertFalse(lemmatizationDAO.isLemmatizationSavedForPage(1, "كلمة3"));
    }

    @Test
    public void testIsLemmatizationSavedForPageEmptyContent() {
        assertFalse(lemmatizationDAO.isLemmatizationSavedForPage(1, ""));
    }
}
