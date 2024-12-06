package bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dal.IDALFacade;
import dto.PageDTO;
import util.FarasaPreProcessor;

public class PMIAnalysisBO implements IPMIAnalysisBO {
    private IDALFacade dalFacade;
    private FarasaPreProcessor farasaPreprocessor;

    public PMIAnalysisBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
        this.farasaPreprocessor = new FarasaPreProcessor();
    }

    @Override
    public Map<String, Double> performPMIAnalysisForWord(String searchWord) {
        String normalizedSearchWord = farasaPreprocessor.normalizeText(searchWord);
        List<String> searchWordSegments = farasaPreprocessor.segmentText(normalizedSearchWord);

       
        Map<String, Double> pmiResults = new HashMap<>();
        Map<String, Integer> wordCount = new HashMap<>();
        Map<String, Integer> bigramCount = new HashMap<>();
        int totalWords = 0;

        
        List<Integer> textFileIds = dalFacade.getAllFileIds();

       
        for (int textFileId : textFileIds) {
            List<PageDTO> pages = dalFacade.getPagesByTextFileId(textFileId);
            StringBuilder documentContent = new StringBuilder();

            
            for (PageDTO page : pages) {
                if (page.getPageContent() != null) {
                    documentContent.append(page.getPageContent()).append(" ");
                }
            }

         
            String normalizedText = farasaPreprocessor.normalizeText(documentContent.toString());
            List<String> segmentedWords = farasaPreprocessor.segmentText(normalizedText);

            if (segmentedWords.isEmpty()) {
//                System.out.println("Warning: No words found in file ID " + textFileId);
                continue;
            }

            
            for (int i = 0; i < segmentedWords.size(); i++) {
                String word = segmentedWords.get(i);
                if (!word.isEmpty() && !isPunctuation(word)) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    totalWords++;

                    
                    if (i < segmentedWords.size() - 1) { 
                        String nextWord = segmentedWords.get(i + 1);
                        if (!nextWord.isEmpty() && !isPunctuation(nextWord)) {
                            String forwardBigram = word + "," + nextWord;
                            bigramCount.put(forwardBigram, bigramCount.getOrDefault(forwardBigram, 0) + 1);
                        }
                    }
                    if (i > 0) { 
                        String prevWord = segmentedWords.get(i - 1);
                        if (!prevWord.isEmpty() && !isPunctuation(prevWord)) {
                            String backwardBigram = prevWord + "," + word;
                            bigramCount.put(backwardBigram, bigramCount.getOrDefault(backwardBigram, 0) + 1);
                        }
                    }
                }
            }
        }

        
        for (Map.Entry<String, Integer> entry : bigramCount.entrySet()) {
            String bigram = entry.getKey();
            int bigramOccurrence = entry.getValue();
            String[] words = bigram.split(",");

            if (words.length < 2) {
//                System.out.println("Warning: Skipping invalid bigram '" + bigram + "'");
                continue;
            }

            String word1 = words[0];
            String word2 = words[1];

            
            boolean matchFound = searchWordSegments.stream().anyMatch(segment -> segment.equals(word1) || segment.equals(word2));

            if (matchFound) {
                if (wordCount.containsKey(word1) && wordCount.containsKey(word2)) {
                    double pWord1 = (double) wordCount.get(word1) / totalWords;
                    double pWord2 = (double) wordCount.get(word2) / totalWords;
                    double pBigram = (double) bigramOccurrence / totalWords;

                    if (pWord1 > 0 && pWord2 > 0) { 
                        double pmi = Math.log(pBigram / (pWord1 * pWord2));
                        pmiResults.put(bigram, pmi);
                    } 
                }
            }
        }

        return pmiResults;
    }

    
    private boolean isPunctuation(String word) {
        return word.matches("[.,!?;:\"'(){}\\[\\]]"); 
    }

//    public static void main(String[] args) {
//        
//        IPaginationDAO paginationDAO = new PaginationDAO();
//        IFileDAO fileDAO = new FileDAO();
//
//        
//        IDALFacade dalFacade = new DALFacade(
//            fileDAO,
//            new FileImportDAO(),
//            paginationDAO,
//            new SearchResultDAO(paginationDAO, fileDAO),
//            new TransliterationDAO(),
//            new LemmatizationDAO(),
//            new POSTaggingDAO()
//        );
//
//        
//        PMIAnalysisBO pmiAnalysisBO = new PMIAnalysisBO(dalFacade);
//
//        
//        String searchWord = "هَذِهِ"; 
//
//        
//        Map<String, Double> pmiResults = pmiAnalysisBO.performPMIAnalysisForWord(searchWord);
//
//        
//        System.out.println("PMI results for the word '" + searchWord + "':");
//        for (Map.Entry<String, Double> entry : pmiResults.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//    }
}
