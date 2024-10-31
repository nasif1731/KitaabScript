package dal;

import java.util.List;
import dto.FileDTO;
import dto.PageDTO;

public class DALFacade implements IDALFacade {
    private final IFileDAO fileDAO;
    private final IFileImportDAO fileImportDAO;
    private final IPaginationDAO paginationDAO;

    public DALFacade(IFileDAO fileDAO, IFileImportDAO fileImportDAO, IPaginationDAO paginationDAO) {
        this.fileDAO = fileDAO;
        this.fileImportDAO = fileImportDAO;
        this.paginationDAO = paginationDAO;
    }

    @Override
    public void createFile(String name, String content) {
        fileDAO.createFile(name, content);
    }

    @Override
    public void deleteFile(String name) {
        fileDAO.deleteFile(name);
    }

    @Override
    public void updateFile(String name, String newContent) {
        fileDAO.updateFile(name, newContent);
    }

    @Override
    public String createdAt(String name) {
        return fileDAO.createdAt(name);
    }

    @Override
    public String updatedAt(String name) {
        return fileDAO.updatedAt(name);
    }

    @Override
    public List<FileDTO> getAllFiles() {
        return fileDAO.getAllFiles();
    }

    @Override
    public FileDTO getOneFile(String fileName) {
        return fileDAO.getOneFile(fileName);
    }

    @Override
    public int getWordCount(String fileName) {
        return fileDAO.getWordCount(fileName);
    }

    @Override
    public String importFile(String filePath) {
        return fileImportDAO.importFile(filePath);
    }

    @Override
    public List<String> bulkImportFiles(List<String> filePaths) {
        return fileImportDAO.bulkImportFiles(filePaths);
    }

    @Override
    public int insertFileIntoDatabase(FileDTO file, String content) {
        return fileImportDAO.insertFileIntoDatabase(file, content);
    }

    @Override
    public boolean doesHashExist(String hash) {
        return fileImportDAO.doesHashExist(hash);
    }

    @Override
    public List<PageDTO> paginateContent(int fileId, String content) {
        return paginationDAO.paginateContent(fileId, content); 
    }

    @Override
    public void insertContent(List<PageDTO> paginatedContent) {
        paginationDAO.insertContent(paginatedContent);
    }

    @Override
    public PageDTO getPage(int fileId, int pageNumber) {
        return paginationDAO.getPage(fileId, pageNumber); 
    }

    @Override
    public boolean contentExistsForFile(int fileId, int pageNumber) {
        return paginationDAO.contentExistsForFile(fileId, pageNumber); 
    }

	@Override
	   public int getTotalPages(int fileId) {
			return paginationDAO.getTotalPages(fileId);
		}
}
