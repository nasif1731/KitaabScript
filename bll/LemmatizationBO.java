package bll;

import dal.IDALFacade;
import dal.ILemmatizationDAO;
import dto.LemmatizationDTO;
import dto.POSTaggingDTO;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

public class LemmatizationBO implements ILemmatizationBO {
	private final IDALFacade dalFacade;

	public LemmatizationBO(IDALFacade dalFacade) {
		this.dalFacade = dalFacade;
	}

	@Override
	public void processLemmatizationForPage(int pageId, String pageContent) {
		if (!dalFacade.isLemmatizationSavedForPage(pageId, pageContent)) {
			try {
				String[] words = pageContent.split("\\s+");
				for (String word : words) {
					LemmatizationDTO lemmatization = analyzeWord(word, pageId);
					if (lemmatization != null) {
						dalFacade.addLemmatization(lemmatization);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Error while processing lemmatization for page: " + pageId, e);
			}
		}
	}

	@Override
	public LemmatizationDTO analyzeWord(String word, int pageId) {
	    try {
	        File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
	        URLClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() });

	        Class<?> lemmatizerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
	        Object lemmatizerInstance = lemmatizerClass.getDeclaredConstructor().newInstance();

	        Method analyzeMethod = lemmatizerClass.getMethod("analyzedWords", String.class);
	        List<?> lemmatizationResults = (List<?>) analyzeMethod.invoke(lemmatizerInstance, word);

	        if (!lemmatizationResults.isEmpty()) {
	            
	            for (Object result : lemmatizationResults) {
	                Method getVoweledWordMethod = result.getClass().getMethod("getVoweledWord");
	                String voweledWord = (String) getVoweledWordMethod.invoke(result);

	                
	                if (word.equals(voweledWord)) {
	                    Method getLemmaMethod = result.getClass().getMethod("getStem");
	                    Method getRootMethod = result.getClass().getMethod("getWordRoot");

	                    String lemma = (String) getLemmaMethod.invoke(result);
	                    String root = (String) getRootMethod.invoke(result);

	                    classLoader.close();
	                    return new LemmatizationDTO(pageId, voweledWord, lemma, root);
	                }
	            }

	            
	            Object defaultResult = lemmatizationResults.get(0);
	            Method getVoweledWordMethod = defaultResult.getClass().getMethod("getVoweledWord");
	            Method getLemmaMethod = defaultResult.getClass().getMethod("getStem");
	            Method getRootMethod = defaultResult.getClass().getMethod("getWordRoot");

	            String voweledWord = (String) getVoweledWordMethod.invoke(defaultResult);
	            String lemma = (String) getLemmaMethod.invoke(defaultResult);
	            String root = (String) getRootMethod.invoke(defaultResult);

	            classLoader.close();
	            return new LemmatizationDTO(pageId, voweledWord, lemma, root);
	        }

	        classLoader.close();
	    } catch (Exception e) {
	        throw new RuntimeException("Error during lemmatization analysis for word: " + word, e);
	    }

	    return null;
	}


	public LinkedList<LemmatizationDTO> getLemmatizationForPage(int pageId) {
		return new LinkedList<>(dalFacade.getLemmatizationForPage(pageId));
	}
}
