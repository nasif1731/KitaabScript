package dal;


import java.util.List;

import dto.FileDTO;

public interface IFileDAO {

	void createFile(String name, String content) ;

	void deleteFile(String name);

	void updateFile(String name, String newContent);

	String createdAt(String name);

	String updatedAt(String name);

	List<FileDTO> getAllFiles();

	FileDTO getOneFile(String fileName);

	int getWordCount(String fileName);

}
