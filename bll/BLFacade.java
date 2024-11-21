package bll;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.FileDTO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;
import dto.PageDTO;
import dto.SearchResultDTO;

public class BLFacade implements IBLFacade {
    private final IFileBO fileBO;
    private final IFileImportBO fileImportBO;
    private final IFilePaginationBO filePaginateBO;
    private final ISearchResultBO searchResultBO;
    private final ITransliterationBO transliterationBO;
    private final ILemmatizationBO lemmatizationBO;
    private final IPOSTaggingBO postaggingBO;
    private final ITFIDFAnalysisBO tfidfAnalysisBO;
    private final IPMIAnalysisBO pmiAnalysisBO;
    private final IKLAnalysisBO klAnalysisBO;

    public BLFacade(IFileBO fileBO, IFileImportBO fileImportBO, IFilePaginationBO filePaginateBO, ISearchResultBO searchResultBO, ITransliterationBO transliterationBO,ILemmatizationBO lemmatizationBO,IPOSTaggingBO postaggingBO,ITFIDFAnalysisBO tfidfAnalysisBO,IPMIAnalysisBO pmiAnalysisBO,IKLAnalysisBO klAnalysisBO) {
        this.fileBO = fileBO;
        this.fileImportBO = fileImportBO;
        this.filePaginateBO = filePaginateBO;
        this.searchResultBO = searchResultBO;
        this.transliterationBO = transliterationBO;
		this.lemmatizationBO = lemmatizationBO;
		this.postaggingBO = postaggingBO;
		this.tfidfAnalysisBO = tfidfAnalysisBO;
		this.pmiAnalysisBO = pmiAnalysisBO;
		this.klAnalysisBO = klAnalysisBO;
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
    public int getFileID(String filename) {
        return fileBO.getFileID(filename);
    }

	@Override
	public void processLemmatizationForPage(int pageId, String pageContent) {
		lemmatizationBO.processLemmatizationForPage(pageId, pageContent);
		
	}

	@Override
	public LinkedList<LemmatizationDTO> getLemmatizationForPage(int pageId) {
		// TODO Auto-generated method stub
		return lemmatizationBO.getLemmatizationForPage(pageId);
	}

	@Override
	public void processPOSTaggingForPage(int pageId, String pageContent) {
		// TODO Auto-generated method stub
		postaggingBO.processPOSTaggingForPage(pageId, pageContent);
		
		
	}

	@Override
	public LinkedList<POSTaggingDTO> getPOSTaggingForPage(int pageId) {
		// TODO Auto-generated method stub
		return postaggingBO.getPOSTaggingForPage(pageId);
	}

	@Override
	public POSTaggingDTO analyzedWord(String word, int pageId) {
		// TODO Auto-generated method stub
		return postaggingBO.analyzedWord(word, pageId);
	}

	@Override
	public LemmatizationDTO analyzeWord(String word, int pageId) {
		// TODO Auto-generated method stub
		return lemmatizationBO.analyzeWord(word, pageId);
	}

	@Override
	public Map<String, Double> performTFIDFAnalysisForWord(String searchWord) {
		// TODO Auto-generated method stub
		return tfidfAnalysisBO.performTFIDFAnalysisForWord(searchWord);
	}

	@Override
	public Map<String, Double> performPMIAnalysisForWord(String searchWord) {
		// TODO Auto-generated method stub
		return pmiAnalysisBO.performPMIAnalysisForWord(searchWord);
	}

	@Override
	public Map<String, Double> performKLAnalysisForWord(String searchWord) {
		// TODO Auto-generated method stub
		return klAnalysisBO.performKLAnalysisForWord(searchWord);
	}


	@Override
	public void updateFile(String name, int pageNumber, String content) {
		 fileBO.updateFile(name,pageNumber, content);
		
	}
	

	
}
