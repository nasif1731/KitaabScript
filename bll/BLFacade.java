package bll;

import java.util.List;
import dto.FileDTO;
import dto.PageDTO;
import dto.SearchResultDTO;

public class BLFacade implements IBLFacade {
    private final IFileBO fileBO;
    private final IFileImportBO fileImportBO;
    private final IFilePaginationBO filePaginateBO;
    private final ISearchResultBO searchResultBO;
    private final ITransliterationBO transliterationBO;

    public BLFacade(IFileBO fileBO, IFileImportBO fileImportBO, IFilePaginationBO filePaginateBO, ISearchResultBO searchResultBO, ITransliterationBO transliterationBO) {
        this.fileBO = fileBO;
        this.fileImportBO = fileImportBO;
        this.filePaginateBO = filePaginateBO;
        this.searchResultBO = searchResultBO;
        this.transliterationBO = transliterationBO;
    }

    @Override
    public void createFile(String name, String content) {
        fileBO.createFile(name, content);
    }

    @Override
    public void printFileTimestamps(String name) {
        fileBO.printFileTimestamps(name);
    }

    @Override
    public void updateFile(String name, String content) {
        fileBO.updateFile(name, content);
    }

    @Override
    public void deleteFile(String name) {
        fileBO.deleteFile(name);
    }

    @Override
    public List<FileDTO> getAllFiles() {
        return fileBO.getAllFiles();
    }

    @Override
    public FileDTO getOneFile(String fileName) {
        return fileBO.getOneFile(fileName);
    }

    @Override
    public int getWordCount(String fileName) {
        return fileBO.getWordCount(fileName);
    }

    @Override
    public FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO) {
        return fileBO.paginatedFile(fileName, paginationBO);
    }

    @Override
    public String transliterateToLatin(String arabicText) {
        return transliterationBO.transliterateToLatin(arabicText);
    }

    @Override
    public void saveTransliterationIfNotExists(int pageId, String pageContent) {
        transliterationBO.saveTransliterationIfNotExists(pageId, pageContent);
    }

    @Override
    public String getTransliterationForText(String text) {
        return transliterationBO.getTransliterationForText(text);
    }

    @Override
    public List<String> bulkImportFiles(List<String> filePaths) {
        return fileImportBO.bulkImportFiles(filePaths);
    }

    @Override
    public String importFile(String filePath) {
        return fileImportBO.importFile(filePath);
    }

    @Override
    public List<PageDTO> paginateContent(int fileId, String content) {
        return filePaginateBO.paginateContent(fileId, content);
    }

    @Override
    public void insertContent(List<PageDTO> paginatedContent) {
        filePaginateBO.insertContent(paginatedContent);
    }

    @Override
    public PageDTO getPageContent(int fileId, int pageNumber) {
        return filePaginateBO.getPageContent(fileId, pageNumber);
    }

    @Override
    public int getTotalPages(int fileId) {
        return filePaginateBO.getTotalPages(fileId);
    }

    @Override
    public List<SearchResultDTO> search(String keyword) {
        return searchResultBO.search(keyword);
    }

    @Override
    public void printSearchResultsWithContext(String keyword) {
        searchResultBO.printSearchResultsWithContext(keyword);
    }

    @Override
    public int getPageID(int fileID, int pageNumber) {
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
