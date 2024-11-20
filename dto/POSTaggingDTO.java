package dto;

public class POSTaggingDTO {
	private int id;
	private int paginationId;
	private String word;
	private String posTag;
	public POSTaggingDTO(int id, int paginationId, String word, String posTag) {
        this.id = id;
        this.paginationId = paginationId;
        this.word = word;
        this.posTag = posTag;
    }
    public POSTaggingDTO(int paginationId, String word, String posTag) {
    	this.paginationId = paginationId;
        this.word = word;
        this.posTag = posTag;
    }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPaginationId() {
		return paginationId;
	}

	public void setPaginationId(int paginationId) {
		this.paginationId = paginationId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPosTag() {
		return posTag;
	}

	public void setPosTag(String posTag) {
		this.posTag = posTag;
	}
}
