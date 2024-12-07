package bll;

import java.util.HashMap;
import java.util.Map;

import dal.IDALFacade;
import dto.TransliterationDTO;
import interfaces.ITransliterationBO;

public class TransliterationBO implements ITransliterationBO{
	private final IDALFacade dalFacade;
    private static final Map<Character, String> Mapping = new HashMap<>();

    
    static {
        initializeTransliterationMap();
    }

    public TransliterationBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
    }
    
    private static void initializeTransliterationMap() {
        Mapping.put('أ', "a");
        Mapping.put('ب', "b");
        Mapping.put('ت', "t");
        Mapping.put('ث', "th");
        Mapping.put('ج', "j");
        Mapping.put('ح', "ḥ");
        Mapping.put('خ', "kh");
        Mapping.put('د', "d");
        Mapping.put('ذ', "dh");
        Mapping.put('ر', "r");
        Mapping.put('ز', "z");
        Mapping.put('س', "s");
        Mapping.put('ش', "sh");
        Mapping.put('ص', "ṣ");
        Mapping.put('ض', "ḍ");
        Mapping.put('ط', "ṭ");
        Mapping.put('ظ', "ẓ");
        Mapping.put('ع', "`");
        Mapping.put('غ', "gh");
        Mapping.put('ف', "f");
        Mapping.put('ق', "q");
        Mapping.put('ك', "k");
        Mapping.put('ل', "l");
        Mapping.put('م', "m");
        Mapping.put('ن', "n");
        Mapping.put('ه', "h");
        Mapping.put('و', "w");
        Mapping.put('ي', "y");
        Mapping.put('ء', "'");
        Mapping.put('ة', "t"); 
        Mapping.put('ا', "aa");  
        Mapping.put('َ', "a");
        Mapping.put('ُ', "u");  
        Mapping.put('ِ', "i");
        Mapping.put('ّ', ""); 
        Mapping.put('ْ', "");
        Mapping.put('ً', "an"); 
        Mapping.put('ٌ', "un"); 
        Mapping.put('ٍ', "in"); 
        Mapping.put('ٱ', "");
    }
    @Override
    public String transliterateToLatin(String arabicText) {
        StringBuilder latinText = new StringBuilder();

        for (int i = 0; i < arabicText.length(); i++) {
            char arabicChar = arabicText.charAt(i);
            String latinEquivalent = Mapping.get(arabicChar);

           
            if (latinEquivalent != null) {
                latinText.append(latinEquivalent);
            } else {
                latinText.append(arabicChar);  
            }
            if (arabicChar == 'ّ' && i > 0) {
                char previousChar = arabicText.charAt(i - 1);
                String doubledChar = Mapping.get(previousChar);
                if (doubledChar != null) {
                    latinText.append(doubledChar);  
                }
            }
        }
        return latinText.toString();
    }

    @Override
    public void saveTransliterationIfNotExists(int pageId, String pageContent) {
        if (!dalFacade.isTransliterationSavedForPage(pageId,pageContent)) {
            String transliteratedContent = transliterateToLatin(pageContent);
            TransliterationDTO transliteration = new TransliterationDTO( pageId, pageContent, transliteratedContent);
            dalFacade.addTransliteration(transliteration);
        }
    }

    @Override
    public String getTransliterationForText(String text) {
        return transliterateToLatin(text);
    }

//    
//    public int countWords(String text) {
//        return (text == null || text.trim().isEmpty()) ? 0 : text.trim().split("\\s+").length;
//    }
//
//    // Generates a preview of the transliterated text, capped at maxLength
//    public String generatePreview(String arabicText, int maxLength) {
//        String transliteratedText = transliterateToLatin(arabicText);
//        return transliteratedText.length() > maxLength ? transliteratedText.substring(0, maxLength) + "..." : transliteratedText;
//    }
}
