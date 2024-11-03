package dto;

public class SearchResultDTO {
    private String fileName;
    private int pageNumber;
    private String matchedWord;
    private String beforeContext;
    private String afterContext;

    public SearchResultDTO(String fileName, int pageNumber, String matchedWord, String beforeContext, String afterContext) {
        this.fileName = fileName;
        this.pageNumber = pageNumber;
        this.matchedWord = matchedWord;
        this.beforeContext = beforeContext;
        this.afterContext = afterContext;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getMatchedWord() {
        return matchedWord;
    }

    public String getBeforeContext() {
        return beforeContext;
    }

    public String getAfterContext() {
        return afterContext;
    }
}
