
package dto;

public class LemmatizationDTO {
	private int id;
	private int paginationId;
	private String word;
	private String root;
	private String lemma;
	public LemmatizationDTO(int id, int paginationId, String word, String lemma,String root) {
        this.id = id;
        this.paginationId = paginationId;
        this.word = word;
        this.root = root;
        this.lemma=lemma;
    }
	public LemmatizationDTO(int paginationId, String word, String lemma,String root) {
        this.paginationId = paginationId;
        this.word = word;
        this.root = root;
        this.lemma=lemma;
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

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

    @Override
    public String toString() {
        return "LemmatizationDTO{" +
               "id=" + id +
               ", paginationId=" + paginationId +
               ", word='" + word + '\'' +
               ", lemma='" + lemma + '\'' +
               ", root='" + root + '\'' +
               '}';
    }
}

