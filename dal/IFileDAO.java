package dal;


import java.sql.SQLException;
import java.util.List;

import dto.FileDTO;

public interface IFileDAO {

	void createFile(String name, String content) ;

	void deleteFile(String name);

	int updateFile(String name, String newContent);

	String createdAt(String name);

	String updatedAt(String name);

	List<FileDTO> getAllFiles();
	int fetchFileIdByName(String name) throws SQLException ;
	FileDTO getOneFile(String fileName);

	int getWordCount(String fileName);

	List<Integer> getAllFileIds();

	String getFileName(int fileId);
	
	int getFileID();



}
