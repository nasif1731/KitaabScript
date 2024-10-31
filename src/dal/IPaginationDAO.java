package dal;

import dto.PageDTO;
import java.util.List;

public interface IPaginationDAO {
    List<PageDTO> paginateContent(int fileId, String content);
    void insertContent(List<PageDTO> paginatedContent);
   PageDTO getPage(int fileId, int pageNumber); 
    boolean contentExistsForFile(int fileId, int pageNumber);
	int getTotalPages(int fileId);
}
