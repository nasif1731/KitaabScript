package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.FileDTO;
import dto.PageDTO;
import util.MockDatabaseConnection;

class FileDAOTests {

    private FileDAO fileDAO;
    private Connection connection;


    @BeforeEach
    void setUp() throws SQLException, InterruptedException {
        connection = MockDatabaseConnection.getInstance().getConnection();
        
        fileDAO = new FileDAO(connection);
        
        connection.prepareStatement("DELETE FROM text_files").executeUpdate();
        connection.prepareStatement("DELETE FROM pagination").executeUpdate();
  
        connection.prepareStatement(
                "INSERT INTO text_files (id, filename, created_at, updated_at, hash, word_count, language) " +
                        "VALUES (1, 'testfile1.txt', NOW(), NOW(), 'hash1', 1000, 'EN')").executeUpdate();
        connection.prepareStatement(
                "INSERT INTO text_files (id, filename, created_at, updated_at, hash, word_count, language) " +
                        "VALUES (2, 'testfile2.txt', NOW(), NOW(), 'hash2', 2000, 'FR')").executeUpdate();
        
        
        connection.prepareStatement(
                "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (1, 1, 'Page 1 content for testfile1')").executeUpdate();
        connection.prepareStatement(
                "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (1, 2, 'Page 2 content for testfile1')").executeUpdate();
        connection.prepareStatement(
                "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (2, 1, 'Page 1 content for testfile2')").executeUpdate();
        connection.prepareStatement(
                "INSERT INTO pagination (text_file_id, page_number, page_content) VALUES (2, 2, 'Page 2 content for testfile2')").executeUpdate();
        
   
        
    }


    @AfterEach
    void tearDown() throws SQLException {
        connection.prepareStatement("DELETE FROM text_files").executeUpdate();
        connection.prepareStatement("DELETE FROM pagination").executeUpdate();
        MockDatabaseConnection.getInstance().releaseConnection(connection);
    }


    @Test
    void testCreateFile() {
        String fileName = "newfile1.txt";
        String content = "This is the content of the new file.";
        PageDTO firstPage = fileDAO.createFile(fileName, content);
        
        assertNotNull(firstPage);
        assertEquals(fileName, fileDAO.getFileName(firstPage.getTextFileId()));
        assertTrue(fileDAO.getWordCount(fileName) > 0);
    }

    @Test
    void testDeleteFile() {
        String fileName = "testfile2.txt";
        fileDAO.deleteFile(fileName);

        List<Integer> fileIds = fileDAO.getAllFileIds();
        assertFalse(fileIds.contains(2));
    }

    @Test
    void testUpdateFile() {
        String fileName = "testfile1.txt";
        int pageNumber = 1;
        String newContent = "Updated content for page 1.";

        PageDTO updatedPage = fileDAO.updateFile(fileName, pageNumber, newContent);
        assertNotNull(updatedPage);
        assertEquals(newContent, updatedPage.getPageContent());
    }

    @Test
    void testGetOneFile() {
        String fileName = "testfile1.txt";
        FileDTO file = fileDAO.getOneFile(fileName);
        
        assertNotNull(file); 
        assertEquals(fileName, file.getFilename());
        assertEquals(1000, file.getWordCount()); 
        assertEquals("EN", file.getLanguage()); 
    }



    @Test
    void testGetAllFiles() {
        List<FileDTO> files = fileDAO.getAllFiles();

        assertEquals(2, files.size());
        assertTrue(files.stream().anyMatch(f -> f.getFilename().equals("testfile1.txt")));
    }

    @Test
    void testGetWordCount() {
        String fileName = "testfile1.txt";
        int wordCount = fileDAO.getWordCount(fileName);

        assertEquals(1000, wordCount);
    }

    @Test
    void testFetchFileIdByName() throws SQLException {
        String fileName = "testfile1.txt";
        int fileId = fileDAO.fetchFileIdByName(fileName);

        assertEquals(1, fileId);
    }

    @Test
    void testGetFileName() {
        int fileId = 1;
        String fileName = fileDAO.getFileName(fileId);

        assertEquals("testfile1.txt", fileName);
    }

    @Test
    void testGetAllFileIds() {
        List<Integer> fileIds = fileDAO.getAllFileIds();

        assertTrue(fileIds.contains(1));
        assertTrue(fileIds.contains(2));
    }

    @Test
    void testCreatedAt() {
        String fileName = "testfile1.txt";
        String createdAt = fileDAO.createdAt(fileName);

        assertNotNull(createdAt);
    }

    @Test
    void testUpdatedAt() {
        String fileName = "testfile1.txt";
        String updatedAt = fileDAO.updatedAt(fileName);

        assertNotNull(updatedAt);
    }
}
