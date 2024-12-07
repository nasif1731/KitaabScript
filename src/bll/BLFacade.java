package bll;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.FileDTO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;
import dto.PageDTO;
import dto.SearchResultDTO;
import interfaces.IBLFacade;
import interfaces.IFileBO;
import interfaces.IFileImportBO;
import interfaces.IFilePaginationBO;
import interfaces.IKLAnalysisBO;
import interfaces.ILemmatizationBO;
import interfaces.IPMIAnalysisBO;
import interfaces.IPOSTaggingBO;
import interfaces.ISearchResultBO;
import interfaces.ITFIDFAnalysisBO;
import interfaces.ITransliterationBO;

public class BLFacade extends UnicastRemoteObject implements IBLFacade {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

    public BLFacade(IFileBO fileBO, IFileImportBO fileImportBO, IFilePaginationBO filePaginateBO, ISearchResultBO searchResultBO, ITransliterationBO transliterationBO,ILemmatizationBO lemmatizationBO,IPOSTaggingBO postaggingBO,ITFIDFAnalysisBO tfidfAnalysisBO,IPMIAnalysisBO pmiAnalysisBO,IKLAnalysisBO klAnalysisBO) throws RemoteException {
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
    public void createFile(String name, String content) throws RemoteException{
        fileBO.createFile(name, content);
    }

    @Override
    public void printFileTimestamps(String name) throws RemoteException{
        fileBO.printFileTimestamps(name);
    }


    @Override
    public void deleteFile(String name)throws RemoteException {
        fileBO.deleteFile(name);
    }

    @Override
    public List<FileDTO> getAllFiles() throws RemoteException{
        return fileBO.getAllFiles();
    }

    @Override
    public FileDTO getOneFile(String fileName) {
        try {
			return fileBO.getOneFile(fileName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    @Override
    public int getWordCount(String fileName) throws RemoteException{
        return fileBO.getWordCount(fileName);
    }

    @Override
    public FileDTO paginatedFile(String fileName, IFilePaginationBO paginationBO) throws RemoteException{
        return fileBO.paginatedFile(fileName, paginationBO);
    }

    @Override
    public String transliterateToLatin(String arabicText) throws RemoteException{
        return transliterationBO.transliterateToLatin(arabicText);
    }

    @Override
    public void saveTransliterationIfNotExists(int pageId, String pageContent) throws RemoteException{
        transliterationBO.saveTransliterationIfNotExists(pageId, pageContent);
    }

    @Override
    public String getTransliterationForText(String text) throws RemoteException{
        return transliterationBO.getTransliterationForText(text);
    }

    @Override
    public List<String> bulkImportFiles(List<String> filePaths) throws RemoteException{
        return fileImportBO.bulkImportFiles(filePaths);
    }

    @Override
    public String importFile(String filePath) throws RemoteException{
        return fileImportBO.importFile(filePath);
    }

    @Override
    public List<PageDTO> paginateContent(int fileId, String content) throws RemoteException{
        return filePaginateBO.paginateContent(fileId, content);
    }

    @Override
    public void insertContent(List<PageDTO> paginatedContent) throws RemoteException{
        filePaginateBO.insertContent(paginatedContent);
    }

    @Override
    public PageDTO getPageContent(int fileId, int pageNumber) throws RemoteException{
        return filePaginateBO.getPageContent(fileId, pageNumber);
    }

    @Override
    public int getTotalPages(int fileId)throws RemoteException {
        return filePaginateBO.getTotalPages(fileId);
    }

    @Override
    public List<SearchResultDTO> search(String keyword) throws RemoteException{
        return searchResultBO.search(keyword);
    }

    @Override
    public void printSearchResultsWithContext(String keyword)throws RemoteException {
        searchResultBO.printSearchResultsWithContext(keyword);
    }

    @Override
    public int getPageID(int fileID, int pageNumber)throws RemoteException {
        return filePaginateBO.getPageID(fileID, pageNumber);
    }

    @Override
    public String getFileName(int fileID) throws RemoteException{
        return fileBO.getFileName(fileID);
    }

    @Override
    public int getFileID(String filename)throws RemoteException {
        return fileBO.getFileID(filename);
    }

	@Override
	public void processLemmatizationForPage(int pageId, String pageContent)throws RemoteException {
		lemmatizationBO.processLemmatizationForPage(pageId, pageContent);
		
	}

	@Override
	public LinkedList<LemmatizationDTO> getLemmatizationForPage(int pageId) throws RemoteException{
		// TODO Auto-generated method stub
		return lemmatizationBO.getLemmatizationForPage(pageId);
	}

	@Override
	public void processPOSTaggingForPage(int pageId, String pageContent) throws RemoteException{
		// TODO Auto-generated method stub
		postaggingBO.processPOSTaggingForPage(pageId, pageContent);
		
		
	}

	@Override
	public LinkedList<POSTaggingDTO> getPOSTaggingForPage(int pageId) throws RemoteException{
		// TODO Auto-generated method stub
		return postaggingBO.getPOSTaggingForPage(pageId);
	}

	@Override
	public POSTaggingDTO analyzedWord(String word, int pageId) throws RemoteException{
		// TODO Auto-generated method stub
		return postaggingBO.analyzedWord(word, pageId);
	}

	@Override
	public LemmatizationDTO analyzeWord(String word, int pageId)throws RemoteException {
		// TODO Auto-generated method stub
		return lemmatizationBO.analyzeWord(word, pageId);
	}

	@Override
	public Map<String, Double> performTFIDFAnalysisForWord(String searchWord) throws RemoteException{
		// TODO Auto-generated method stub
		return tfidfAnalysisBO.performTFIDFAnalysisForWord(searchWord);
	}

	@Override
	public Map<String, Double> performPMIAnalysisForWord(String searchWord) throws RemoteException{
		// TODO Auto-generated method stub
		return pmiAnalysisBO.performPMIAnalysisForWord(searchWord);
	}

	@Override
	public Map<String, Double> performKLAnalysisForWord(String searchWord) throws RemoteException{
		// TODO Auto-generated method stub
		return klAnalysisBO.performKLAnalysisForWord(searchWord);
	}


	@Override
	public void updateFile(String name, int pageNumber, String content)throws RemoteException {
		 fileBO.updateFile(name,pageNumber, content);
		
	}
	

	
}
