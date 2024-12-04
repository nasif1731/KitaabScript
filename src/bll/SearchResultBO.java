package bll;

import dal.IDALFacade;
import dto.SearchResultDTO;

import java.util.List;

public class SearchResultBO implements ISearchResultBO{
	private final IDALFacade dalFacade;

    public SearchResultBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
    }
    @Override
    public List<SearchResultDTO> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty.");
        }

        return dalFacade.search(keyword);
    }

    @Override
    public void printSearchResultsWithContext(String keyword) {
        List<SearchResultDTO> results = search(keyword);

        if (results.isEmpty()) {
            System.out.println("No results found for keyword: " + keyword);
            return;
        }

       
        for (SearchResultDTO result : results) {
            System.out.println("File: " + result.getFileName());
            System.out.println("Page: " + result.getPageNumber());
            System.out.println("Match: ..." + result.getBeforeContext() + result.getMatchedWord() + result.getAfterContext() + "...");
            System.out.println("--------");
        }
    }
}
