package bll;

import java.util.List;

import dto.FileDTO;

public interface IFileBO {

	void createFile(String name, String content);

	void printFileTimestamps(String name);

	void updateFile(String name, String content);

	void deleteFile(String name);

	List<FileDTO> getAllFiles();

	FileDTO getOneFile(String fileName);

	int getWordCount(String fileName);

	FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO);

	String getFileName(int fileID);

	int getFileID();

}
