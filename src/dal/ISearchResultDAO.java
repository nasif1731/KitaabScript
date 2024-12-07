package dal;

import java.util.List;

import dto.SearchResultDTO;

public interface ISearchResultDAO {
    List<SearchResultDTO> search(String keyword);
    
}