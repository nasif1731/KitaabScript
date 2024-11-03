package bll;

import java.util.List;

public interface IFileImportBO {

	String importFile(String filePath);

	List<String> bulkImportFiles(List<String> filePaths);

}
