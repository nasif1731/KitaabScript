package dal;

import dto.PageDTO;
import java.util.List;

public interface IPaginationDAO {
    void insertContent(List<PageDTO> paginatedContent);
    
    boolean contentExistsForFile(int fileId, int pageNumber);
	int getTotalPages(int fileId);
	List<PageDTO> paginateContent(int fileId, String content);

	PageDTO getPage(int fileId, int pageNumber);

	int getPageID(int fileId, int pageNumber);

	List<PageDTO> getPagesByTextFileId(int textFileId);
}
