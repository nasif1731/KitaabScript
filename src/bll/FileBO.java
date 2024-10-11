package bll;

import dal.FileDAO;
import dto.FileDTO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class FileBO {
    private FileDAO fileDAO;

    public FileBO() {
        fileDAO = new FileDAO();
    }
    public void createFile(String name, String content) throws NoSuchAlgorithmException, SQLException {
        try {
        	fileDAO.createFile(name, content); 
        } catch (IOException e) {
            throw new RuntimeException("Error in file creation: " + e.getMessage(), e);
        }
    }

    public void printFileTimestamps(String name) {
        try {
            String createdAt = fileDAO.createdAt(name);
            String updatedAt = fileDAO.updatedAt(name);
            System.out.println("Created At: " + createdAt);
            System.out.println("Updated At: " + updatedAt);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving timestamps: " + e.getMessage(), e);
        }
    }

    public void updateFile(String name, String content) throws IOException, NoSuchAlgorithmException, SQLException {
        try {
        	fileDAO.updateFile(name, content);
        } catch (IOException | NoSuchAlgorithmException | SQLException e) {
            throw new RuntimeException("Error updating file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String name) throws IOException, SQLException {
        try {
        	fileDAO.deleteFile(name);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }

    public List<FileDTO> getAllFiles() {
        return fileDAO.getAllFiles();
    }

    public FileDTO getOneFile(String fileName) throws SQLException{
        return fileDAO.getOneFile(fileName);
    }

    public int getWordCount(String fileName) {
        return fileDAO.getWordCount(fileName);
    }
}
