package dto;

import java.sql.Timestamp;
import java.util.List;

public class FileDTO {
    private int id;
    private String filename;
    private String content;
    private String language;
    private Timestamp createdAt;
    private Timestamp updatedAt; 
    private String hash;
    private int wordCount;
    private List<PageDTO> paginatedContent;


	public FileDTO(String filename, String content, String language, String hash, int wordCount) {
        this.filename = filename;
        this.content = content;
        this.language = language;
        this.hash = hash;
        this.wordCount = wordCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt; 
    }

    public void setUpdatedAt(Timestamp updatedAt) { 
        this.updatedAt = updatedAt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
    public List<PageDTO> getPaginatedContent() {
  		return paginatedContent;
  	}

  	public void setPaginatedContent(List<PageDTO> paginatedContent) {
  		this.paginatedContent = paginatedContent;
  	}
}
