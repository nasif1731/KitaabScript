package bll;

import java.util.List;

import dto.FileDTO;
import dto.PageDTO;
import dto.SearchResultDTO;

public class BLFacade implements IBLFacade{
	private final IFileBO fileBO;
	private final IFileImportBO fileImportBO;
	private final IFilePaginationBO filePaginateBO;
	private final ISearchResultBO searchResultBO;
	private final ITransliterationBO transliterationBO;

	public BLFacade(IFileBO fileBO, IFileImportBO fileImporBO,IFilePaginationBO filePaginateBO,ISearchResultBO searchResultBO,ITransliterationBO transliterationBO) {
		this.fileBO = fileBO;
		this.fileImportBO = fileImporBO;
		this.filePaginateBO = filePaginateBO;
		this.searchResultBO = searchResultBO;
		this.transliterationBO = transliterationBO;
	}
	@Override
	public void createFile(String name, String content) {
		// TODO Auto-generated method stub
		fileBO.createFile(name, content);
		
	}

	@Override
	public void printFileTimestamps(String name) {
		// TODO Auto-generated method stub
		fileBO.printFileTimestamps(name);
		
	}

	@Override
	public void updateFile(String name, String content) {
		// TODO Auto-generated method stub
		fileBO.updateFile(name, content);
		
	}

	@Override
	public void deleteFile(String name) {
		// TODO Auto-generated method stub
		fileBO.deleteFile(name);
		
	}

	@Override
	public List<FileDTO> getAllFiles() {
		// TODO Auto-generated method stub
		return fileBO.getAllFiles();
	}

	@Override
	public FileDTO getOneFile(String fileName) {
		// TODO Auto-generated method stub
		return fileBO.getOneFile(fileName);
	}

	@Override
	public int getWordCount(String fileName) {
		// TODO Auto-generated method stub
		return fileBO.getWordCount(fileName);
	}

	@Override
	public FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO) {
		// TODO Auto-generated method stub
		return fileBO.paginatedFile(fileName, paginationBO);
		
	}

	@Override
	public String transliterateToLatin(String arabicText) {
		// TODO Auto-generated method stub
		return transliterationBO.transliterateToLatin(arabicText);
	}

	@Override
	public void saveTransliterationIfNotExists(int pageId, String pageContent) {
		// TODO Auto-generated method stub
		transliterationBO.saveTransliterationIfNotExists(pageId, pageContent);
		
	}

	@Override
	public String getTransliterationForText(String text) {
		// TODO Auto-generated method stub
		return transliterationBO.getTransliterationForText(text);
	}

	@Override
	public List<String> bulkImportFiles(List<String> filePaths) {
		// TODO Auto-generated method stub
		return fileImportBO.bulkImportFiles(filePaths);
	}

	@Override
	public String importFile(String filePath) {
		// TODO Auto-generated method stub
		return fileImportBO.importFile(filePath);
	}
	

	@Override
	public List<PageDTO> paginateContent(int fileId, String content) {
		// TODO Auto-generated method stub
		return filePaginateBO.paginateContent(fileId, content);
	}

	@Override
	public void insertContent(List<PageDTO> paginatedContent) {
		// TODO Auto-generated method stub
		filePaginateBO.insertContent(paginatedContent);
		
	}

	@Override
	public PageDTO getPageContent(int fileId, int pageNumber) {
		// TODO Auto-generated method stub
		return filePaginateBO.getPageContent(fileId, pageNumber);
	}

	@Override
	public int getTotalPages(int fileId) {
		// TODO Auto-generated method stub
		return filePaginateBO.getTotalPages(fileId);
	}

	@Override
	public List<SearchResultDTO> search(String keyword) {
		// TODO Auto-generated method stub
		return searchResultBO.search(keyword);
	}

	@Override
	public void printSearchResultsWithContext(String keyword) {
		// TODO Auto-generated method stub
		searchResultBO.printSearchResultsWithContext(keyword);
		
	}
	@Override
	public int getPageID(int fileID,int pageNumber) {
		return filePaginateBO.getPageID(fileID, pageNumber);
	}
	@Override
	public String getFileName(int fileID) {
		return fileBO.getFileName(fileID);
	}
	@Override
	public int getFileID() {
		return fileBO.getFileID();
	}

}
