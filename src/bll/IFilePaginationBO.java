package bll;

import java.util.List;

import dto.PageDTO;

public interface IFilePaginationBO {

	List<PageDTO> paginateContent(int fileId, String content);

	void insertContent(List<PageDTO> paginatedContent);

	PageDTO getPageContent(int fileId, int pageNumber);

	int getTotalPages(int fileId);

	int getPageID(int fileId, int pageNumber);

}
