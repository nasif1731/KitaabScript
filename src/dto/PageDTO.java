package dto;

public class PageDTO {
	
    private int textFileId;
    private int pageNumber;
    private String pageContent;
    private int pageId;
    public PageDTO(int textFileId, int pageNumber, String pageContent) {
    	
        this.textFileId = textFileId;
        this.pageNumber = pageNumber;
        this.pageContent = pageContent;
    }
    public PageDTO(int pageId,int textFileId, int pageNumber, String pageContent) {
    	this.pageId=pageId;
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

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	@Override
	public String toString() {
	    return "PageDTO{" +
	            "textFileId=" + textFileId +
	            ", pageNumber=" + pageNumber +
	            ", pageContent='" + pageContent + '\'' +
	            '}';
	}

	}

