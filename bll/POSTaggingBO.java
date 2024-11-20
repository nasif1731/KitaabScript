package bll;

import dal.IPOSTaggingDAO;
import dto.POSTaggingDTO;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class POSTaggingBO  implements IPOSTaggingBO{
    private final IPOSTaggingDAO dao;

    public POSTaggingBO(IPOSTaggingDAO dalFacade) {
        this.dao = dalFacade;
    }

    @Override
    public void processPOSTaggingForPage(int pageId, String pageContent) {
        if (!dao.isPOSTaggingSavedForPage(pageId, pageContent)) {
            try {
                String[] words = pageContent.split("\\s+");

                for (String word : words) {
                    POSTaggingDTO posTagging = analyzedWord(word,pageId);
                    if (posTagging != null) {
                        dao.addPOSTagging(posTagging);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while processing POS tagging for page: " + pageId, e);
            }
        }
    }

    @Override
    public POSTaggingDTO analyzedWord(String word, int pageId) {
        try {
            File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
            Class<?> posTaggerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
            Object posTaggerInstance = posTaggerClass.getDeclaredConstructor().newInstance();

            Method tagMethod = posTaggerClass.getMethod("analyzedWords", String.class);
            List<?> posTaggedResults = (List<?>) tagMethod.invoke(posTaggerInstance, word);

            if (!posTaggedResults.isEmpty()) {
                
                for (Object result : posTaggedResults) {
                    Method getWordMethod = result.getClass().getMethod("getVoweledWord");
                    String voweledWord = (String) getWordMethod.invoke(result);

                    
                    if (word.equals(voweledWord)) {
                        Method getWordTypeMethod = result.getClass().getMethod("getWordType");
                        String wordType = (String) getWordTypeMethod.invoke(result);

                        classLoader.close();
                        return new POSTaggingDTO(pageId, voweledWord, wordType);
                    }
                }

                
                Object defaultResult = posTaggedResults.get(0);
                Method getWordMethod = defaultResult.getClass().getMethod("getVoweledWord");
                Method getWordTypeMethod = defaultResult.getClass().getMethod("getWordType");

                String voweledWord = (String) getWordMethod.invoke(defaultResult);
                String wordType = (String) getWordTypeMethod.invoke(defaultResult);

                classLoader.close();
                return new POSTaggingDTO(pageId, voweledWord, wordType);
            }

            classLoader.close();
        } catch (Exception e) {
            throw new RuntimeException("Error during POS tagging analysis for word: " + word, e);
        }

        return null; 
    }

    @Override
    public LinkedList<POSTaggingDTO> getPOSTaggingForPage(int pageId) {
        return new LinkedList<>(dao.getPOSTaggingForPage(pageId));
    }
}
