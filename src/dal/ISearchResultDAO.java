package dal;

import dto.SearchResultDTO;
import java.util.List;

public interface ISearchResultDAO {
    List<SearchResultDTO> search(String keyword);
    
}