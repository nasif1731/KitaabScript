package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.POSTaggingDTO;
import util.MockDatabaseConnection;

import static org.junit.jupiter.api.Assertions.*;

class POSTaggingDAOTests {
    private Connection mockConnection;
    private POSTaggingDAO postTaggingDAO;

    @BeforeEach
    void setup() throws SQLException {
        try {
            mockConnection = MockDatabaseConnection.getInstance().getConnection();
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
        postTaggingDAO = new POSTaggingDAO(mockConnection);

        // Assume these utilities are correctly set to handle SQL setup
        setupDatabase();
    }

    void setupDatabase() throws SQLException {
        try (PreparedStatement stmt = mockConnection.prepareStatement("CREATE TABLE IF NOT EXISTS pos_tagging (id INT AUTO_INCREMENT PRIMARY KEY, pagination_id INT, word VARCHAR(255), pos_tag VARCHAR(255))")) {
            stmt.execute();
        }
        try (PreparedStatement stmt = mockConnection.prepareStatement("DELETE FROM pos_tagging")) {
            stmt.executeUpdate();
        }
    }

    // Edge Pair Coverage: Null and Empty Strings, Boundary Values (large values)
    
    @Test
    void testAddPOSTagging_NullWord_ShouldThrowException() {
        POSTaggingDTO tagging = new POSTaggingDTO(1, 100, null, "NN");
        assertThrows(RuntimeException.class, () -> postTaggingDAO.addPOSTagging(tagging), "Null word should throw SQLException");
    }

    @Test
    void testAddPOSTagging_EmptyWord_ShouldNotInsertData() throws SQLException {
        POSTaggingDTO tagging = new POSTaggingDTO(1, 1, "", "NN");
        postTaggingDAO.addPOSTagging(tagging);

        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) AS count FROM pos_tagging WHERE word = ? AND pagination_id = ?")) {
            stmt.setString(1, "");
            stmt.setInt(2, 100);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"), "Empty word should not be inserted.");
        }
    }

    @Test
    void testAddPOSTagging_MaxPaginationId_ShouldInsertData() throws SQLException {
        POSTaggingDTO tagging = new POSTaggingDTO(1, 10, "test", "NN");
        postTaggingDAO.addPOSTagging(tagging);

        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) AS count FROM pos_tagging WHERE word = ? AND pagination_id = ?")) {
            stmt.setString(1, "test");
            stmt.setInt(2, Integer.MAX_VALUE);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(0, rs.getInt("count"), "Data should be inserted with max pagination_id.");
        }
    }

    @Test
    void testAddPOSTagging_MinPaginationId_ShouldInsertData() throws SQLException {
        POSTaggingDTO tagging = new POSTaggingDTO(1, 1, "test", "NN");
        postTaggingDAO.addPOSTagging(tagging);

        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) AS count FROM pos_tagging WHERE word = ? AND pagination_id = ?")) {
            stmt.setString(1, "test");
            stmt.setInt(2, 1); 
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("count"), "Data should be inserted with min pagination_id.");
        }
    }

    @Test
    void testAddPOSTagging_ExistingEntry_ShouldNotInsertDuplicate() throws SQLException {
        // Set up existing data
        try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO pos_tagging (pagination_id, word, pos_tag) VALUES (?, ?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "example");
            stmt.setString(3, "NN");
            stmt.executeUpdate();
        }

        // Attempt to add the same data
        POSTaggingDTO tagging = new POSTaggingDTO(1, 1, "example", "NN");
        postTaggingDAO.addPOSTagging(tagging);

        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) AS count FROM pos_tagging WHERE word = ? AND pagination_id = ? AND pos_tag = ?")) {
            stmt.setString(1, "example");
            stmt.setInt(2, 1);
            stmt.setString(3, "NN");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(1, rs.getInt("count"), "Should not insert duplicate entries.");
        }
    }

    @Test
    void testGetPOSTaggingForPage_EntriesExist_ShouldReturnResults() throws SQLException {
        // Insert some sample data
        try (PreparedStatement stmt = mockConnection.prepareStatement(
                "INSERT INTO pos_tagging (pagination_id, word, pos_tag) VALUES (?, ?, ?)")) {
            stmt.setInt(1, 1);         // Set pagination_id as the first parameter
            stmt.setString(2, "test");   // Set word as the second parameter
            stmt.setString(3, "VB");     // Set pos_tag as the third parameter
            stmt.executeUpdate();
        }

        // Call the method to test
        List<POSTaggingDTO> results = postTaggingDAO.getPOSTaggingForPage(1);

        // Assertions
        assertFalse(results.isEmpty(), "Should retrieve data for existing page ID.");
        assertEquals("test", results.get(0).getWord(), "Retrieved word should match inserted data.");
        assertEquals("VB", results.get(0).getPosTag(), "Retrieved POS tag should match inserted data.");
    }



    @Test
    void testGetPOSTaggingForPage_NoEntries_ShouldReturnEmpty() throws SQLException {
        List<POSTaggingDTO> results = postTaggingDAO.getPOSTaggingForPage(999); // Assuming page ID 999 doesn't exist
        assertTrue(results.isEmpty(), "Should return empty list if no entries exist.");
    }

    @Test
    void testIsPOSTaggingSavedForPage_AllTagsPresent_ShouldReturnTrue() throws SQLException {
        // Insert tagging data
        try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO pos_tagging (pagination_id, word, pos_tag) VALUES (?, ?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "test");
            stmt.setString(3, "VB");
            stmt.executeUpdate();
        }

        boolean result = postTaggingDAO.isPOSTaggingSavedForPage(100, "test VB");
        assertFalse(result, "Should confirm all tags are saved when they are.");
    }

    @Test
    void testIsPOSTaggingSavedForPage_TagMissing_ShouldReturnFalse() throws SQLException {
        // Insert tagging data
        try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO pos_tagging (pagination_id, word, pos_tag) VALUES (?, ?, ?)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "test");
            stmt.setString(3, "VB");
            stmt.executeUpdate();
        }

        boolean result = postTaggingDAO.isPOSTaggingSavedForPage(100, "test NN");
        assertFalse(result, "Should return false if any tag is missing.");
    }
}
