package dal;

import java.util.List;

import dto.TransliterationDTO;

public interface ITransliterationDAO {

	List<TransliterationDTO> getTransliterationsForPage(int pageId);

	boolean isTransliterationSavedForPage(int pageId);

	void addTransliteration(TransliterationDTO transliteration);

}
