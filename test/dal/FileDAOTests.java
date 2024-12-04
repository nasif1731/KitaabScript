package dal;

import dto.FileDTO;
import dto.PageDTO;
import org.junit.*;
import util.MockDatabaseConnection;

import java.sql.*;
import static org.junit.Assert.*;

public class FileDAOTests {

    private Connection mockConnection;
    private FileDAO fileDAO;

    @Before
    public void setup() throws SQLException, InterruptedException {
        mockConnection = MockDatabaseConnection.getInstance().getConnection();
        fileDAO = new FileDAO(mockConnection);
        clearTestData();
    }

    @After
    public void tearDown() {
        clearTestData();
    }

    private void clearTestData() {
        try (Statement stmt = mockConnection.createStatement()) {
            stmt.executeUpdate("DELETE FROM text_files");
            stmt.executeUpdate("DELETE FROM pagination");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateFile_withValidContent() {
        String name = "testFile.txt";
        String content = "This is a test file content.";
        PageDTO pageDTO = fileDAO.createFile(name, content);

        assertNotNull("PageDTO should not be null", pageDTO);
        assertEquals("testFile.txt", fileDAO.getFileName(pageDTO.getTextFileId()));
    }

    @Test
    public void testUpdateFile_withValidData() {
        String name = "existingFile.txt";
        String content = "Updated content for the file.";
        fileDAO.createFile(name, content);

        String newContent = "New content to update the file.";
        int pageNumber = 1;
        PageDTO updatedPageDTO = fileDAO.updateFile(name, pageNumber, newContent);

        assertNotNull("Updated pageDTO should not be null", updatedPageDTO);
        assertEquals(newContent, updatedPageDTO.getPageContent());
    }
    @Test
    public void testUpdateFile_withInvalidFileName() {
        String name = "nonExistentFile.txt";
        String content = "Some content.";
        int pageNumber = 1;

        PageDTO updatedPageDTO = fileDAO.updateFile(name, pageNumber, content);
        assertNull("Updating a non-existent file should return null", updatedPageDTO);
    }

    @Test
    public void testGetWordCount_withExistingFile() {
    
        String name = "wordCountFile.txt";
        String content = "This is a simple test file content.";
        fileDAO.createFile(name, content);

        int wordCount = fileDAO.getWordCount(name);
        assertEquals("Word count should be 7", 7, wordCount);
    }

    @Test
    public void testGetWordCount_withNonExistentFile() {
  
        String name = "nonExistentFile.txt";
        int wordCount = fileDAO.getWordCount(name);
        assertEquals("Word count for non-existent file should be 0", 0, wordCount);
    }

    @Test
    public void testGetOneFile_withValidFile() {
    
        String name = "existingFile.txt";
        String content = "Content for the file.";
        fileDAO.createFile(name, content);

        FileDTO fileDTO = fileDAO.getOneFile(name);
        assertNotNull("FileDTO should not be null", fileDTO);
        assertEquals("existingFile.txt", fileDTO.getFilename());
        assertTrue("Content should not be empty", fileDTO.getContent().length() > 0);
    }
}
