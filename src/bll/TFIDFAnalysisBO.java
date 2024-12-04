package bll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dal.IDALFacade;

import dto.PageDTO;
import util.FarasaPreProcessor;

public class TFIDFAnalysisBO implements ITFIDFAnalysisBO {
    private IDALFacade dalFacade;
    private FarasaPreProcessor farasaPreprocessor;

    public TFIDFAnalysisBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
        this.farasaPreprocessor = new FarasaPreProcessor(); 
    }

    
    
    @Override
    public Map<String, Double> performTFIDFAnalysisForWord(String searchWord) {
        String normalizedSearchWord = farasaPreprocessor.normalizeText(searchWord);
        List<String> searchWordSegments = farasaPreprocessor.segmentText(normalizedSearchWord);
        Map<String, Double> tfidfResults = new HashMap<>();
        List<Integer> textFileIds = dalFacade.getAllFileIds();
        Map<String, Double> idfScores = new HashMap<>();
        Map<Integer, Map<String, Double>> tfScores = new HashMap<>();
        int totalDocuments = textFileIds.size();

        for (int textFileId : textFileIds) {
            List<PageDTO> pages = dalFacade.getPagesByTextFileId(textFileId);
            StringBuilder documentContent = new StringBuilder();

            
            for (PageDTO page : pages) {
                documentContent.append(page.getPageContent()).append(" ");
            }

            
            String normalizedText = farasaPreprocessor.normalizeText(documentContent.toString());
            List<String> segmentedWords = farasaPreprocessor.segmentText(normalizedText);

            
            Map<String, Integer> termFrequency = new HashMap<>();
            Map<String, Boolean> termInDocument = new HashMap<>();
            for (String word : segmentedWords) {
                termFrequency.put(word, termFrequency.getOrDefault(word, 0) + 1);
                termInDocument.put(word, true);
            }

           
            Map<String, Double> tf = new HashMap<>();
            String[] terms = segmentedWords.toArray(new String[0]);
            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                String term = entry.getKey();
                double tfValue = (double) entry.getValue() / terms.length;
                tf.put(term, tfValue);
            }
            tfScores.put(textFileId, tf);

            
            for (String term : termInDocument.keySet()) {
                idfScores.put(term, idfScores.getOrDefault(term, 0.0) + 1);
            }
        }

        
        for (String term : idfScores.keySet()) {
            double df = idfScores.get(term);
            double idfValue = Math.log((double) totalDocuments / (1 + df));
            idfScores.put(term, idfValue);
        }

        
        for (int textFileId : tfScores.keySet()) {
            Map<String, Double> tf = tfScores.get(textFileId);
            for (String term : tf.keySet()) {
                double tfidfValue = tf.get(term) * idfScores.get(term);

                
                boolean matchFound = searchWordSegments.stream().anyMatch(segment -> segment.equals(term));
                if (matchFound) {
                    tfidfResults.put(term, tfidfValue);
                }
            }
        }

        return tfidfResults;
    }

    
//    public static void main(String[] args) {
//    	IPaginationDAO pg=new PaginationDAO();
//    	IFileDAO fd=new FileDAO();
//        // Create an instance of IDALFacade (you need to implement this)
//        IDALFacade dalFacade = new DALFacade(fd,new FileImportDAO(),pg,new SearchResultDAO(pg,fd),new TransliterationDAO(),new LemmatizationDAO(),new POSTaggingDAO());
//
//        // Create an instance of TFIDFAnalysisBO
//        TFIDFAnalysisBO tfidfAnalysisBO = new TFIDFAnalysisBO(dalFacade);
//
//        // Perform TF-IDF analysis for a specific word
//        String searchWord = "هَذِهِ"; // Replace with the word you want to analyze
//        Map<String, Double> tfidfResults = tfidfAnalysisBO.performTFIDFAnalysisForWord(searchWord);
//
//        // Print the results
//        System.out.println("TF-IDF results for the word '" + searchWord + "':");
//        for (Map.Entry<String, Double> entry : tfidfResults.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//    }
}
