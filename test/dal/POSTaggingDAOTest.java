package dal;

import dto.POSTaggingDTO;
import org.junit.jupiter.api.*;
import util.MockDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class POSTaggingDAOTest {

    private POSTaggingDAO posTaggingDAO;
    private Connection mockConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        try {
            mockConnection = MockDatabaseConnection.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }

        posTaggingDAO = new POSTaggingDAO(mockConnection);

        // Insert required test data
        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "INSERT IGNORE INTO text_files (id) VALUES (?)")) {
            stmt.setInt(1, 1);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "INSERT IGNORE INTO pagination (id, text_file_id) VALUES (?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "DELETE FROM pos_tagging")) {
            stmt.executeUpdate();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Clean up test data
        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "DELETE FROM pos_tagging")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "DELETE FROM text_files")) {
            stmt.executeUpdate();
        }
    }

    @Test
    public void testAddPOSTaggingSuccess() {
        POSTaggingDTO dto = new POSTaggingDTO(1, 1, "word1", "NOUN");
        posTaggingDAO.addPOSTagging(dto);
        List<POSTaggingDTO> results = posTaggingDAO.getPOSTaggingForPage(1);

        assertEquals(1, results.size());
        assertEquals("word1", results.get(0).getWord());
        assertEquals("NOUN", results.get(0).getPosTag());
    }

    @Test
    public void testAddPOSTaggingFailure() {
        POSTaggingDTO dto = new POSTaggingDTO(1, 1, null, null);
        assertThrows(RuntimeException.class, () -> posTaggingDAO.addPOSTagging(dto));
    }

    @Test
    public void testGetPOSTaggingForPageWithData() throws SQLException {
        POSTaggingDTO dto1 = new POSTaggingDTO(1, 1, "word1", "NOUN");
        POSTaggingDTO dto2 = new POSTaggingDTO(2, 1, "word2", "VERB");

        posTaggingDAO.addPOSTagging(dto1);
        posTaggingDAO.addPOSTagging(dto2);

        List<POSTaggingDTO> results = posTaggingDAO.getPOSTaggingForPage(1);

        assertEquals(2, results.size());
        assertEquals("word1", results.get(0).getWord());
        assertEquals("word2", results.get(1).getWord());
    }

    // Test for matching POS tags (True)
    @Test
    public void testIsPOSTaggingSavedForPageTrue() throws SQLException {
        POSTaggingDTO dto1 = new POSTaggingDTO(1, 1, "إِرْثًا", "اسم جامد");
        POSTaggingDTO dto2 = new POSTaggingDTO(2, 1, "الْعَرَبِيَّةُ", "مصدر صناعي");

        posTaggingDAO.addPOSTagging(dto1);
        posTaggingDAO.addPOSTagging(dto2);

        assertTrue(posTaggingDAO.isPOSTaggingSavedForPage(1, "إِرْثًا"));
        assertTrue(posTaggingDAO.isPOSTaggingSavedForPage(1, "الْعَرَبِيَّةُ"));
    }

    // Test for mismatched POS tags (False)
    @Test
    public void testIsPOSTaggingSavedForPageFalse() throws SQLException {
        POSTaggingDTO dto = new POSTaggingDTO(1, 1, "إِرْثًا", "اسم جامد");
        posTaggingDAO.addPOSTagging(dto);

        // Testing mismatch with different POS tag or word
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, "إِرْثًا:اسم آخر"));
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, "غير موجود"));
    }

    // Test when input is empty content
    @Test
    public void testIsPOSTaggingSavedForPageEmptyContent() throws SQLException {
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, ""));
    }

    // Test when the word is not in the database (edge case)
    @Test
    public void testIsPOSTaggingSavedForPageWordNotInDB() throws SQLException {
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, "غير موجود"));
    }

    // Test when input contains multiple words, ensuring they all match
    @Test
    public void testIsPOSTaggingSavedForPageMultipleWords() throws SQLException {
        POSTaggingDTO dto1 = new POSTaggingDTO(1, 1, "إِرْثًا", "اسم جامد");
        POSTaggingDTO dto2 = new POSTaggingDTO(2, 1, "الْعَرَبِيَّةُ", "مصدر صناعي");

        posTaggingDAO.addPOSTagging(dto1);
        posTaggingDAO.addPOSTagging(dto2);

        assertTrue(posTaggingDAO.isPOSTaggingSavedForPage(1, "إِرْثًا الْعَرَبِيَّةُ"));
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, "إِرْثًا غير موجود"));
    }

    // Test for handling null words
    @Test
    public void testIsPOSTaggingSavedForPageNullWord() throws SQLException {
        assertFalse(posTaggingDAO.isPOSTaggingSavedForPage(1, null));
    }
}
