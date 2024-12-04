package dal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dto.PageDTO;
import dto.SearchResultDTO;

public class SearchResultDAO implements ISearchResultDAO {
    private final IPaginationDAO paginationDAO;
    private final IFileDAO fileDAO;
    private Connection conn;
    public SearchResultDAO(IPaginationDAO paginationDAO, IFileDAO fileDAO, Connection conn) {
    	this.setConn(conn);
        this.paginationDAO = paginationDAO;
        this.fileDAO = fileDAO;
    }


	@Override
    public List<SearchResultDTO> search(String keyword) {
        List<SearchResultDTO> searchResults = new ArrayList<>();
        List<Integer> fileIds = fileDAO.getAllFileIds();

        for (int fileId : fileIds) {
            int pageNumber = 1;
            PageDTO page;

            while ((page = paginationDAO.getPage(fileId, pageNumber)) != null) {
                String content = page.getPageContent().toLowerCase();
                String lowerKeyword = keyword.toLowerCase();

                int matchIndex = content.indexOf(lowerKeyword);
                if (matchIndex != -1) {
                    String before = content.substring(Math.max(0, matchIndex - 10), matchIndex);
                    String after = content.substring(matchIndex + lowerKeyword.length(), Math.min(content.length(), matchIndex + lowerKeyword.length() + 10));
                    String matchedWord = content.substring(matchIndex, matchIndex + lowerKeyword.length());

                    SearchResultDTO result = new SearchResultDTO(
                            fileDAO.getFileName(fileId), pageNumber, matchedWord, before, after );
                    searchResults.add(result);
                }
                pageNumber++;
            }
        }

        return searchResults;
    }


	public Connection getConn() {
		return conn;
	}


	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
