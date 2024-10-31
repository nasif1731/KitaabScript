package bll;

import dal.IPaginationDAO;
import dto.PageDTO;
import java.util.List;

public class FilePaginationBO {

    private IPaginationDAO paginationDAO;

    public FilePaginationBO(IPaginationDAO paginationDAO) {
        this.paginationDAO = paginationDAO;
    }

    public List<PageDTO> paginateContent(int fileId, String content) {
        return paginationDAO.paginateContent(fileId, content);
    }

    public void insertContent(List<PageDTO> paginatedContent) {
        paginationDAO.insertContent(paginatedContent);
    }


    public PageDTO getPageContent(int fileId, int pageNumber) {
        return paginationDAO.getPage(fileId, pageNumber);
    }
    public int getTotalPages(int fileId) {
		return paginationDAO.getTotalPages(fileId);
	}
    
}
