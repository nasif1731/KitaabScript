package dal;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import dto.LemmatizationDTO;
import util.MockDatabaseConnection;
import static org.junit.jupiter.api.Assertions.*;

public class LemmatizationDAOTests {

    private LemmatizationDAO lemmatizationDAO;
    Connection mockConnection;
    @BeforeEach
    public void setUp() throws SQLException  {
       
		try {
			mockConnection = MockDatabaseConnection.getInstance().getConnection();
		} catch (InterruptedException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        lemmatizationDAO = new LemmatizationDAO(mockConnection);
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
                "DELETE FROM lemmatization")) {
            stmt.executeUpdate();
        }
    }

   
    @AfterEach
    void tearDown() throws SQLException {
    	try (PreparedStatement stmt = mockConnection.prepareStatement(
                "DELETE FROM text_files")) {
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "DELETE FROM lemmatization")) {
            stmt.executeUpdate();
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
    public void testAddLemmatizationFailure() {
        LemmatizationDTO dto = new LemmatizationDTO(1, 1, null, null, null);
        assertThrows(RuntimeException.class, () -> lemmatizationDAO.addLemmatization(dto));
    }

    @Test
    public void testGetLemmatizationForPageEmpty() {
        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetLemmatizationForPageWithData() throws SQLException {
        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "كلمة1", "تصريف1", "جذر1");
        LemmatizationDTO dto2 = new LemmatizationDTO(1, 1, "كلمة2", "تصريف2", "جذر2");

        lemmatizationDAO.addLemmatization(dto1);
        lemmatizationDAO.addLemmatization(dto2);

        List<LemmatizationDTO> results = lemmatizationDAO.getLemmatizationForPage(1);
        
        assertEquals(2, results.size());
        assertEquals("كلمة1", results.get(0).getWord());
        assertEquals("كلمة2", results.get(1).getWord());
    }

    @Test
    public void testIsLemmatizationSavedForPageTrue() throws SQLException {
        LemmatizationDTO dto1 = new LemmatizationDTO(1, 1, "الْعَرَبِيَّةُ", "عَرَبِيَّة", "عرب");
        LemmatizationDTO dto2 = new LemmatizationDTO(2, 1, "فَصِيحَةٌ", "فَصِيحَة", "فصح");
        lemmatizationDAO.addLemmatization(dto1);
        lemmatizationDAO.addLemmatization(dto2);
        
        
        assertTrue(lemmatizationDAO.isLemmatizationSavedForPage(1, "الْعَرَبِيَّةُ"));
        assertTrue(lemmatizationDAO.isLemmatizationSavedForPage(1, "فَصِيحَةٌ"));
    }

    @Test
    public void testIsLemmatizationSavedForPageFalse() throws SQLException {
        assertFalse(lemmatizationDAO.isLemmatizationSavedForPage(1, "كلمة3"));
    }

    @Test
    public void testIsLemmatizationSavedForPageEmptyContent() {
        assertFalse(lemmatizationDAO.isLemmatizationSavedForPage(1, ""));
    }
}
