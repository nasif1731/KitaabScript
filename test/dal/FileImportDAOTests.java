package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.MockDatabaseConnection;

class FileImportDAOTests {

    private Connection mockConnection;
    private FileImportDAO fileImportDAO;
    @Before
    public void cleanup() throws SQLException {
        // Clean up the test database by removing any previous test entries
        try (PreparedStatement stmt = mockConnection.prepareStatement("DELETE FROM text_files WHERE filename = ?")) {
            stmt.setString(1, "test.txt");
            stmt.executeUpdate();
        }
    }

    @BeforeEach
    void setup() throws SQLException, InterruptedException {
        mockConnection = MockDatabaseConnection.getInstance().getConnection();
        fileImportDAO = new FileImportDAO(mockConnection);

      
        mockConnection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS text_files (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "filename TEXT, language TEXT, hash TEXT, word_count INT)");
    }


    

    @Test
    void testImportFile_FileDoesNotExist() {
        String filePath = "nonexistent.txt";
        String result = fileImportDAO.importFile(filePath);
        assertTrue(result.startsWith("Error reading the file"));
    }

//    @Test
//    void testImportFile_HashExists() throws IOException, SQLException {
//        String filePath = "test.txt";
//        String content = "Duplicate content";
//        String hash = "dummyhash";
//        File testFile = new File(filePath);
//        try (FileWriter writer = new FileWriter(testFile)) {
//            writer.write(content);
//        }
//
//        // Check if the file already exists before inserting
//        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) FROM text_files WHERE filename = ?")) {
//            stmt.setString(1, filePath);
//            ResultSet rs = stmt.executeQuery();
//            rs.next();
//            if (rs.getInt(1) > 0) {
//                assertEquals("Cannot import: A similar file already exists in the database.", fileImportDAO.importFile(filePath));
//            } else {
//                // Proceed with the insert
//                stmt = mockConnection.prepareStatement(
//                    "INSERT INTO text_files (filename, language, hash, word_count) VALUES (?, ?, ?, ?)");
//                stmt.setString(1, "test.txt");
//                stmt.setString(2, "English");
//                stmt.setString(3, hash);
//                stmt.setInt(4, 2);
//                stmt.executeUpdate();
//            }
//        }
//
//        // Cleanup
//        Files.deleteIfExists(testFile.toPath());
//    }
//
//
//    @Test
//    void testImportFile_SuccessfulImport() throws IOException {
//        String filePath = "valid.txt";
//        String content = "Unique content";
//
//        // Create test file
//        File testFile = new File(filePath);
//        try (FileWriter writer = new FileWriter(testFile)) {
//            writer.write(content);
//        }
//
//        String result = fileImportDAO.importFile(filePath);
//        assertEquals("File imported successfully.", result);
//
//     
//        try (PreparedStatement stmt = mockConnection.prepareStatement("SELECT COUNT(*) FROM text_files")) {
//            ResultSet rs = stmt.executeQuery();
//            rs.next();
//            assertEquals(1, rs.getInt(1));
//        } catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//       
//        Files.deleteIfExists(testFile.toPath());
//    }

    @Test
    void testBulkImportFiles_EmptyList() {
        List<String> filePaths = Arrays.asList();
        List<String> results = fileImportDAO.bulkImportFiles(filePaths);
        assertTrue(results.isEmpty());
    }

//    @Test
//    void testBulkImportFiles_ValidFiles() throws IOException {
//        String filePath1 = "file1.txt";
//        String filePath2 = "file2.txt";
//
//      
//        try (FileWriter writer1 = new FileWriter(filePath1);
//             FileWriter writer2 = new FileWriter(filePath2)) {
//            writer1.write("Content of file 1");
//            writer2.write("Content of file 2");
//        }
//
//        List<String> filePaths = Arrays.asList(filePath1, filePath2);
//        List<String> results = fileImportDAO.bulkImportFiles(filePaths);
//
//        assertEquals(2, results.size());
//        assertTrue(results.stream().allMatch(result -> result.equals("File imported successfully.")));
//
//        // Cleanup
//        Files.deleteIfExists(new File(filePath1).toPath());
//        Files.deleteIfExists(new File(filePath2).toPath());
//    }

    @Test
    void testDetermineFileLanguage_Urdu() {
        String content = "یہ ایک ٹیسٹ ہے";
        String language = fileImportDAO.determineFileLanguage(content);
        assertEquals("Urdu", language);
    }

    @Test
    void testDetermineFileLanguage_English() {
        String content = "This is a test.";
        String language = fileImportDAO.determineFileLanguage(content);
        assertEquals("English", language);
    }

//    @Test
//    void testDoesHashExist_HashExists() throws SQLException {
//        String hash = "existinghash";
//
//        // Insert hash into the database
//        PreparedStatement stmt = mockConnection.prepareStatement(
//                "INSERT INTO text_files (filename, language, hash, word_count) VALUES (?, ?, ?, ?)");
//        stmt.setString(1, "existing.txt");
//        stmt.setString(2, "English");
//        stmt.setString(3, hash);
//        stmt.setInt(4, 3);
//        stmt.executeUpdate();
//
//        assertTrue(fileImportDAO.doesHashExist(hash));
//    }

//    @Test
//    void testDoesHashExist_HashDoesNotExist() {
//        assertFalse(fileImportDAO.doesHashExist("nonexistenthash"));
//    }
}
