package dto;

public class PageDTO {
    private int textFileId;
    private int pageNumber;
    private String pageContent;

    public PageDTO(int textFileId, int pageNumber, String pageContent) {
        this.textFileId = textFileId;
        this.pageNumber = pageNumber;
        this.pageContent = pageContent;
    }

    public int getTextFileId() {
        return textFileId;
    }

    public void setTextFileId(int textFileId) {
        this.textFileId = textFileId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

}
