package bll;

import dal.FileDAO;
import dto.FileDTO;

import java.sql.SQLException;
import java.util.List;

public class FileBO {
    private FileDAO fileDAO;

    public FileBO() {
        fileDAO = new FileDAO();
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
