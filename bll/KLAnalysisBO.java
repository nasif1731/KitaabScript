package bll;

import dal.IDALFacade;
import dto.PageDTO;
import util.FarasaPreProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KLAnalysisBO implements IKLAnalysisBO {
    private IDALFacade dalFacade;
    private FarasaPreProcessor farasaPreprocessor;

    public KLAnalysisBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
        this.farasaPreprocessor = new FarasaPreProcessor(); 
    }

    @Override
    
    public Map<String, Double> performKLAnalysisForWord(String searchWord) {
        Map<String, Double> klDivergenceResults = new HashMap<>();
        List<Integer> textFileIds = dalFacade.getAllFileIds();
        Map<String, Double> termFrequency = new HashMap<>();
        Map<String, Double> termProbability = new HashMap<>();
        int totalTerms = 0;

        
        for (int textFileId : textFileIds) {
            List<PageDTO> pages = dalFacade.getPagesByTextFileId(textFileId);
            StringBuilder documentContent = new StringBuilder();

           
            for (PageDTO page : pages) {
                documentContent.append(page.getPageContent()).append(" ");
            }

            
            String normalizedText = farasaPreprocessor.normalizeText(documentContent.toString());
            List<String> segmentedWords = farasaPreprocessor.segmentText(normalizedText);

            
            for (String word : segmentedWords) {
                termFrequency.put(word, termFrequency.getOrDefault(word, 0.0) + 1);
                totalTerms++;
            }
        }

        
        for (Map.Entry<String, Double> entry : termFrequency.entrySet()) {
            termProbability.put(entry.getKey(), entry.getValue() / totalTerms);
        }

        
        for (String term : termFrequency.keySet()) {
            double termProb = termProbability.getOrDefault(term, 1.0 / totalTerms); 
            double klDivergence = Math.log(termProb / termFrequency.get(term));
            if (term.equals(searchWord)) {
                klDivergenceResults.put(term, klDivergence);
            }
        }

        return klDivergenceResults; 
    }
}
