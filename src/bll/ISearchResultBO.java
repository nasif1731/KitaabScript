package bll;

import java.util.List;

import dto.SearchResultDTO;

public interface ISearchResultBO {

	List<SearchResultDTO> search(String keyword);

	void printSearchResultsWithContext(String keyword);

}
