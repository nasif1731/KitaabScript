package bll;

import dal.DALFacade;
import dal.FileDAO;
import dal.FileImportDAO;
import dal.IDALFacade;
import dal.IFileDAO;
import dal.IPaginationDAO;
import dal.LemmatizationDAO;
import dal.POSTaggingDAO;
import dal.PaginationDAO;
import dal.SearchResultDAO;
import dal.TransliterationDAO;
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
        
        String normalizedSearchWord = farasaPreprocessor.normalizeText(searchWord);
        List<String> searchWordSegments = farasaPreprocessor.segmentText(normalizedSearchWord);

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

        
        double smoothingFactor = 1.0 / (totalTerms + termFrequency.size());  

        
        for (String searchSegment : searchWordSegments) {
            double searchTermProb = termProbability.getOrDefault(searchSegment, smoothingFactor); 

            for (String term : termFrequency.keySet()) {
                
                double termProb = termProbability.getOrDefault(term, smoothingFactor); 
                
                
                if (termProb > 0 && searchTermProb > 0) {
                    double klDivergence = Math.log(searchTermProb / termProb);
                    klDivergenceResults.put(term, klDivergence);
                } else {
                    
                    double klDivergence = Double.MIN_VALUE;  
                    klDivergenceResults.put(term, klDivergence);
                }
            }
        }

        return klDivergenceResults;
    }

    
//    public static void main(String[] args) {
//    	IPaginationDAO pg=new PaginationDAO();
//    	IFileDAO fd=new FileDAO();
//        
//        IDALFacade dalFacade = new DALFacade(fd,new FileImportDAO(),pg,new SearchResultDAO(pg,fd),new TransliterationDAO(),new LemmatizationDAO(),new POSTaggingDAO());
//
//        
//        KLAnalysisBO tfidfAnalysisBO = new KLAnalysisBO(dalFacade);
//
//        
//        String searchWord = "الْعَرَبِيَّةُ";
//        Map<String, Double> tfidfResults = tfidfAnalysisBO.performKLAnalysisForWord(searchWord);
//
//        
//        System.out.println("KL-Analysis results for the word '" + searchWord + "':");
//        for (Map.Entry<String, Double> entry : tfidfResults.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }
//    }
}
