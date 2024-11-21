package dal;

import java.sql.SQLException;
import java.util.List;
import dto.FileDTO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;
import dto.PageDTO;
import dto.SearchResultDTO;

import dto.TransliterationDTO;


public class DALFacade implements IDALFacade {
    private final IFileDAO fileDAO;
    private final IFileImportDAO fileImportDAO;
    private final IPaginationDAO paginationDAO;
    private final ISearchResultDAO searchResultDAO;
    private final ITransliterationDAO transliterationDAO;
    private final ILemmatizationDAO lemmatizationDAO;
    private final IPOSTaggingDAO postaggingDAO;

    public DALFacade(IFileDAO fileDAO, IFileImportDAO fileImportDAO, IPaginationDAO paginationDAO,ISearchResultDAO searchResultDAO,ITransliterationDAO transliterationDAO,ILemmatizationDAO lemmatizationDAO,IPOSTaggingDAO postaggingDAO) {
        this.fileDAO = fileDAO;
        this.fileImportDAO = fileImportDAO;
        this.paginationDAO = paginationDAO;
		this.searchResultDAO = searchResultDAO;
		this.transliterationDAO = transliterationDAO;
		this.lemmatizationDAO = lemmatizationDAO;
		this.postaggingDAO = postaggingDAO;
    }


    @Override
    public PageDTO createFile(String name, String content) {
        return fileDAO.createFile(name, content);
    }

    @Override
    public void deleteFile(String name) {
        fileDAO.deleteFile(name);
    }

   

    @Override
    public String createdAt(String name) {
        return fileDAO.createdAt(name);
    }

    @Override
    public String updatedAt(String name) {
        return fileDAO.updatedAt(name);
    }

    @Override
    public List<FileDTO> getAllFiles() {
        return fileDAO.getAllFiles();
    }

    @Override
    public FileDTO getOneFile(String fileName) {
        return fileDAO.getOneFile(fileName);
    }

    @Override
    public int getWordCount(String fileName) {
        return fileDAO.getWordCount(fileName);
    }

    @Override
    public String importFile(String filePath) {
        return fileImportDAO.importFile(filePath);
    }

    @Override
    public List<String> bulkImportFiles(List<String> filePaths) {
        return fileImportDAO.bulkImportFiles(filePaths);
    }

    @Override
    public int insertFileIntoDatabase(FileDTO file, String content) {
        return fileImportDAO.insertFileIntoDatabase(file, content);
    }

    @Override
    public boolean doesHashExist(String hash) {
        return fileImportDAO.doesHashExist(hash);
    }

    @Override
    public List<PageDTO> paginateContent(int fileId, String content) {
        return paginationDAO.paginateContent(fileId, content); 
    }

    @Override
    public void insertContent(List<PageDTO> paginatedContent) {
        paginationDAO.insertContent(paginatedContent);
    }

    @Override
    public PageDTO getPage(int fileId, int pageNumber) {
        return paginationDAO.getPage(fileId, pageNumber); 
    }

    @Override
    public boolean contentExistsForFile(int fileId, int pageNumber) {
        return paginationDAO.contentExistsForFile(fileId, pageNumber); 
    }

	@Override
	   public int getTotalPages(int fileId) {
			return paginationDAO.getTotalPages(fileId);
		}

	@Override
	public int fetchFileIdByName(String name) {
		try {
			return fileDAO.fetchFileIdByName(name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Integer> getAllFileIds() {
		return fileDAO.getAllFileIds();
	
	}

	@Override
	public String getFileName(int fileId) {
		return fileDAO.getFileName(fileId);
		
	}

	@Override
	public List<SearchResultDTO> search(String keyword) {
		// TODO Auto-generated method stub
		return searchResultDAO.search(keyword);
		
	}

	@Override
	public List<TransliterationDTO> getTransliterationsForPage(int pageId) {
		// TODO Auto-generated method stub
		return transliterationDAO.getTransliterationsForPage(pageId);
	}

	@Override
	public boolean isTransliterationSavedForPage(int pageId, String content) {
		// TODO Auto-generated method stub
		return transliterationDAO.isTransliterationSavedForPage(pageId,content);
	}

	@Override
	public void addTransliteration(TransliterationDTO transliteration) {
		// TODO Auto-generated method stub
		transliterationDAO.addTransliteration(transliteration);
		
	}

	@Override
	public int getPageID(int fileId, int pageNumber) {
		// TODO Auto-generated method stub
		return paginationDAO.getPageID(fileId,pageNumber);
	}

	


	@Override
	public List<PageDTO> getPagesByTextFileId(int textFileId) {
		// TODO Auto-generated method stub
		return paginationDAO.getPagesByTextFileId(textFileId);
	}


	@Override
	public void addLemmatization(LemmatizationDTO lemmatization) {
		lemmatizationDAO.addLemmatization(lemmatization);
		
	}


	@Override
	public List<LemmatizationDTO> getLemmatizationForPage(int pageId) {
		// TODO Auto-generated method stub
		return lemmatizationDAO.getLemmatizationForPage(pageId);
	}


	@Override
	public boolean isLemmatizationSavedForPage(int pageId, String newContent) {
		// TODO Auto-generated method stub
		return lemmatizationDAO.isLemmatizationSavedForPage(pageId, newContent);
	}


	@Override
	public void addPOSTagging(POSTaggingDTO posTagging) {
		// TODO Auto-generated method stub
		postaggingDAO.addPOSTagging(posTagging);
		
	}


	@Override
	public List<POSTaggingDTO> getPOSTaggingForPage(int pageId) {
		// TODO Auto-generated method stub
		return postaggingDAO.getPOSTaggingForPage(pageId);
	}


	@Override
	public boolean isPOSTaggingSavedForPage(int pageId, String newContent) {
		// TODO Auto-generated method stub
		return postaggingDAO.isPOSTaggingSavedForPage(pageId, newContent);
	}


	@Override
	public PageDTO updateFile(String name, int pageNumber, String newContent) {
		return fileDAO.updateFile(name,pageNumber, newContent);
		
	}
	 
}
