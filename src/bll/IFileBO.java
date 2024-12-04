package bll;

import java.util.List;

import dto.FileDTO;

public interface IFileBO {

	void createFile(String name, String content);

	void printFileTimestamps(String name);

	

	void deleteFile(String name);

	List<FileDTO> getAllFiles();

	FileDTO getOneFile(String fileName);

	int getWordCount(String fileName);

	FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO);

	String getFileName(int fileID);

	

	int getFileID(String filename);


	void updateFile(String name, int pageNumber, String content);

}
