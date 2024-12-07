package bll;

import java.util.List;

import dal.IDALFacade;
import dto.PageDTO;
import interfaces.IFilePaginationBO;

public class FilePaginationBO implements IFilePaginationBO{

	private final IDALFacade dalFacade;
    
    public FilePaginationBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
    }
    @Override
    public List<PageDTO> paginateContent(int fileId, String content) {
        return dalFacade.paginateContent(fileId, content);
    }
    @Override
    public void insertContent(List<PageDTO> paginatedContent) {
    	dalFacade.insertContent(paginatedContent);
    }
    @Override
    public PageDTO getPageContent(int fileId, int pageNumber) {
        return dalFacade.getPage(fileId, pageNumber);
    }
    @Override
    public int getTotalPages(int fileId) {
		return dalFacade.getTotalPages(fileId);
	}
    @Override
    public int getPageID(int fileId,int pageNumber) {
    	return dalFacade.getPageID(fileId, pageNumber);
    }
    
}
