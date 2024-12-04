package bll;

public interface ITransliterationBO {

	String transliterateToLatin(String arabicText);

	void saveTransliterationIfNotExists(int pageId, String pageContent);

	String getTransliterationForText(String text);
	

}
