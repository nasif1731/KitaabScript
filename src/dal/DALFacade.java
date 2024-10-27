package dal;

import java.util.List;
import dto.FileDTO;

public class DALFacade implements IDALFacade {
	private final IFileDAO fileDAO;
	private final IFileImportDAO fileImportDAO;

	public DALFacade(IFileDAO fileDAO, IFileImportDAO fileImportDAO) {
		this.fileDAO = fileDAO;
		this.fileImportDAO = fileImportDAO;
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
		// Retrieve a specific file from the database
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
	public void insertFileIntoDatabase(FileDTO file) {

		fileImportDAO.insertFileIntoDatabase(file);
	}

	@Override
	public boolean doesHashExist(String hash) {

		return fileImportDAO.doesHashExist(hash);
	}
}
