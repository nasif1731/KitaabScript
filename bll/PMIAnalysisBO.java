package bll;

import dal.IDALFacade;
import dto.PageDTO;
import util.FarasaPreProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PMIAnalysisBO implements IPMIAnalysisBO {
    private IDALFacade dalFacade;
    private FarasaPreProcessor farasaPreprocessor;

    public PMIAnalysisBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
        this.farasaPreprocessor = new FarasaPreProcessor(); 
    }

    @Override
    public Map<String, Double> performPMIAnalysisForWord(String searchWord) {
        Map<String, Double> pmiResults = new HashMap<>();
        List<Integer> textFileIds = dalFacade.getAllFileIds();
        Map<String, Integer> wordCount = new HashMap<>();
        Map<String, Integer> coOccurrenceCount = new HashMap<>();
        int totalWords = 0;

        for (int textFileId : textFileIds) {
            List<PageDTO> pages = dalFacade.getPagesByTextFileId(textFileId);
            StringBuilder documentContent = new StringBuilder();

            
            for (PageDTO page : pages) {
                documentContent.append(page.getPageContent()).append(" ");
            }

            
            String normalizedText = farasaPreprocessor.normalizeText(documentContent.toString());
            List<String> segmentedWords = farasaPreprocessor.segmentText(normalizedText);

          
            for (int i = 0; i < segmentedWords.size(); i++) {
                String word = segmentedWords.get(i);
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                totalWords++;

                
                for (int j = i + 1; j < segmentedWords.size(); j++) {
                    String coWord = segmentedWords.get(j);
                    String pair = word + "," + coWord;
                    coOccurrenceCount.put(pair, coOccurrenceCount.getOrDefault(pair, 0) + 1);
                }
            }
        }

        
        for (Map.Entry<String, Integer> entry : coOccurrenceCount.entrySet()) {
            String pair = entry.getKey();
            int coOccurrence = entry.getValue();
            String[] words = pair.split(",");

            String word1 = words[0];
            String word2 = words[1];

            
            if (word1.equals(searchWord) || word2.equals(searchWord)) {
                double pmi = Math.log((double) coOccurrence / (wordCount.get(word1) * wordCount.get(word2)) * totalWords);
                pmiResults.put(pair, pmi);
            }
        }

        return pmiResults; 
    }
}
