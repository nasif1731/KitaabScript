package dal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dto.TransliterationDTO;
import util.MockDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransliterationDAOTest {

    private Connection conn;
    private TransliterationDAO transliterationDAO;

    @BeforeEach
    void setUp() throws SQLException {
        try {
            conn = MockDatabaseConnection.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        transliterationDAO = new TransliterationDAO(conn);

        
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT IGNORE INTO text_files (id) VALUES (?)")) {
            stmt.setInt(1, 1);
            stmt.executeUpdate();
        }

        
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT IGNORE INTO pagination (id, text_file_id) VALUES (?, ?)")) {
            stmt.setInt(1, 1); 
            stmt.setInt(2, 1);
            stmt.executeUpdate();
        }

        
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM transliterations")) {
            stmt.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
    	try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM text_files")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM transliterations")) {
            stmt.executeUpdate();
        }
    }

    @Test
    void testAddTransliterationWithValidData() {
        TransliterationDTO transliteration = new TransliterationDTO(1, 1, "Hello", "Hola");
        transliterationDAO.addTransliteration(transliteration);

        List<TransliterationDTO> transliterations = transliterationDAO.getTransliterationsForPage(1);
        assertNotNull(transliterations);
        assertEquals(1, transliterations.size()); 
    }

    @Test
    void testAddTransliterationWithNullValues() {
        assertThrows(NullPointerException.class, () -> {
            transliterationDAO.addTransliteration(null);
        });
    }

    @Test
    void testGetTransliterationsForPageWithNoEntries() {
        List<TransliterationDTO> transliterations = transliterationDAO.getTransliterationsForPage(1);
        assertNotNull(transliterations);
        assertTrue(transliterations.isEmpty());
    }

    @Test
    void testIsTransliterationSavedForPageWithExistingContent() {
        TransliterationDTO transliteration = new TransliterationDTO(1, 1, "Hello", "Hola");
        transliterationDAO.addTransliteration(transliteration);
        boolean isSaved = transliterationDAO.isTransliterationSavedForPage(1, "Hello");
        assertTrue(isSaved); 
    }

    @Test
    void testIsTransliterationSavedForPageWithDifferentContent() {
        TransliterationDTO transliteration = new TransliterationDTO(1, 1, "Hello", "Hola");
        transliterationDAO.addTransliteration(transliteration);

        boolean isSaved = transliterationDAO.isTransliterationSavedForPage(1, "Goodbye");
        assertFalse(isSaved);
        
        
        List<TransliterationDTO> transliterations = transliterationDAO.getTransliterationsForPage(1);
        assertEquals(0, transliterations.size());
    }

    @Test
    void testIsTransliterationSavedForPageWithNoExistingEntries() {
        boolean isSaved = transliterationDAO.isTransliterationSavedForPage(1, "Hello");
        assertFalse(isSaved);
    }

    @Test
    void testGetTransliterationsForPageWithMultipleEntries() throws SQLException {
        transliterationDAO.addTransliteration(new TransliterationDTO(1, 1, "Hello", "Hola"));
        transliterationDAO.addTransliteration(new TransliterationDTO(2, 1, "World", "Mundo"));

        List<TransliterationDTO> transliterations = transliterationDAO.getTransliterationsForPage(1);
        assertNotNull(transliterations);
        assertEquals(2, transliterations.size());
    }

    @Test
    void testAddTransliterationWithEmptyStrings() {
        TransliterationDTO transliteration = new TransliterationDTO(1, 1, "", "");
        transliterationDAO.addTransliteration(transliteration);

        List<TransliterationDTO> transliterations = transliterationDAO.getTransliterationsForPage(1);
        assertNotNull(transliterations);
        assertEquals(1, transliterations.size()); 
    }
}
