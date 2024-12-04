package dal;


import java.sql.SQLException;
import java.util.List;

import dto.FileDTO;
import dto.PageDTO;

public interface IFileDAO {

	PageDTO createFile(String name, String content) ;

	void deleteFile(String name);

	String createdAt(String name);

	String updatedAt(String name);

	List<FileDTO> getAllFiles();
	int fetchFileIdByName(String name) throws SQLException ;
	FileDTO getOneFile(String fileName);

	int getWordCount(String fileName);

	List<Integer> getAllFileIds();

	String getFileName(int fileId);

	PageDTO updateFile(String name, int pageNumber, String newContent);
	



}
