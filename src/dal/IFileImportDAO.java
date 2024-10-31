package dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dto.FileDTO;

public interface IFileImportDAO {

	String importFile(String filePath);
    
	List<String> bulkImportFiles(List<String> filePaths);

	boolean doesHashExist(String hash);

	int insertFileIntoDatabase(FileDTO file, String content);

}
