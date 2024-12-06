package dal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import dto.FileDTO;
import util.HashGenerator;
import util.MockDatabaseConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileImportDAOTest {

    private Connection conn;
    private FileImportDAO fileImportDAO;

    @BeforeEach
    void setUp() throws SQLException {
        try {
			conn = MockDatabaseConnection.getInstance().getConnection();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        fileImportDAO = new FileImportDAO(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM text_files");
            stmt.executeUpdate("DELETE FROM pagination");
        }
    }

    @Test
    void testImportFileWithValidFile() {
        String filePath = "valid.txt";
        String result = fileImportDAO.importFile(filePath);
        assertEquals("File imported successfully.", result);
    }

    

    @Test
    void testImportFileWithDuplicateHash() throws SQLException {
        String filePath = "file1.txt";
        String hash = HashGenerator.generateHashFromContent("Content of file 1");
        String insertQuery = "INSERT INTO text_files (filename, hash) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, "file1.txt");
            stmt.setString(2, hash);
            stmt.executeUpdate();
        }
        String result = fileImportDAO.importFile(filePath);
        assertEquals("Cannot import: A similar file already exists in the database.", result);
    }

    @Test
    void testBulkImportFilesWithValidPaths() {
        List<String> filePaths = new ArrayList<>();
        filePaths.add("file1.txt"); 
        filePaths.add("file2.txt");
        List<String> results = fileImportDAO.bulkImportFiles(filePaths);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(msg -> msg.equals("File imported successfully.")));
    }

    

    @Test
    void testInsertFileIntoDatabase() {
        FileDTO fileDTO = new FileDTO("testFile.txt", null, "English", "test_hash", 100);
        int fileId = fileImportDAO.insertFileIntoDatabase(fileDTO, "Sample content for testing.");
        assertTrue(fileId > 0);
    }

   

    @Test
    void testDoesHashExistWithExistingHash() throws SQLException {
    	
        String hash = HashGenerator.generateHashFromContent("Content of file 1");
        String insertQuery = "INSERT INTO text_files (filename, hash) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, "file1.txt");
            stmt.setString(2, hash);
            stmt.executeUpdate();
        }
        
        
        assertTrue(fileImportDAO.doesHashExist(hash));
    }

    @Test
    void testDoesHashExistWithNonExistingHash() {
        assertFalse(fileImportDAO.doesHashExist("non_existing_hash"));
    }

    @Test
    void testDetermineFileLanguageWithUrduContent() {
        String content = "یہ ایک اردو متن ہے۔"; 
        String language = fileImportDAO.determineFileLanguage(content);
        assertEquals("Urdu", language);
    }

    @Test
    void testDetermineFileLanguageWithArabicContent() {
        String content = "هذا نص عربي."; 
        String language = fileImportDAO.determineFileLanguage(content);
        assertEquals("Arabic", language);
    }

    @Test
    void testDetermineFileLanguageWithEnglishContent() {
        String content = "This is an English text.";
        String language = fileImportDAO.determineFileLanguage(content);
        assertEquals("English", language);
    }

    @Test
    void testCountWordsWithEmptyContent() {
        int wordCount = fileImportDAO.countWords("");
        assertEquals(0, wordCount);
    }

    @Test
    void testCountWordsWithContent() {
        String content = "This is a sample content with several words.";
        int wordCount = fileImportDAO.countWords(content);
        assertEquals(8, wordCount);
    }
}
