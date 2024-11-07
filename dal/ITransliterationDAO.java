package dal;

import java.util.List;

import dto.TransliterationDTO;

public interface ITransliterationDAO {

	List<TransliterationDTO> getTransliterationsForPage(int pageId);

	void addTransliteration(TransliterationDTO transliteration);

	boolean isTransliterationSavedForPage(int pageId, String newContent);

}
