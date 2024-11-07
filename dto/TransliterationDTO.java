package dto;

public class TransliterationDTO {
    private int id;
    private int paginationId;
    private String originalText;
    private String transliteratedText;

    public TransliterationDTO(int id, int paginationId, String originalText, String transliteratedText) {
        this.id = id;
        this.paginationId = paginationId;
        this.originalText = originalText;
        this.transliteratedText = transliteratedText;
    }
    public TransliterationDTO(int paginationId, String originalText, String transliteratedText) {
    	this.paginationId = paginationId;
        this.originalText = originalText;
        this.transliteratedText = transliteratedText;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPaginationId() { return paginationId; }
    public void setPaginationId(int paginationId) { this.paginationId = paginationId; }

    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }

    public String getTransliteratedText() { return transliteratedText; }
    public void setTransliteratedText(String transliteratedText) { this.transliteratedText = transliteratedText; }


	
}
