package RMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import bll.BLFacade;
import bll.FileBO;
import bll.FileImportBO;
import bll.FilePaginationBO;
import bll.KLAnalysisBO;
import bll.LemmatizationBO;
import bll.PMIAnalysisBO;
import bll.POSTaggingBO;
import bll.SearchResultBO;
import bll.TFIDFAnalysisBO;
import bll.TransliterationBO;
import dal.AbstractDALFactory;
import dal.DALFacade;
import dal.IDALFacade;
import dal.IDALFactory;
import dal.IFileDAO;
import dal.IFileImportDAO;
import dal.ILemmatizationDAO;
import dal.IPOSTaggingDAO;
import dal.IPaginationDAO;
import dal.ISearchResultDAO;
import dal.ITransliterationDAO;
import interfaces.IBLFacade;
import interfaces.IFileBO;
import interfaces.IFileImportBO;
import interfaces.IFilePaginationBO;
import interfaces.IKLAnalysisBO;
import interfaces.ILemmatizationBO;
import interfaces.IPMIAnalysisBO;
import interfaces.IPOSTaggingBO;
import interfaces.ISearchResultBO;
import interfaces.ITFIDFAnalysisBO;
import interfaces.ITransliterationBO;

public class RMIServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IDALFactory dalFactory = AbstractDALFactory.getInstance();
        
        IFileDAO fileDAO = dalFactory.getFileDAO();
        IFileImportDAO fileImportDAO = dalFactory.getFileImportDAO();
        IPaginationDAO paginationDAO = dalFactory.getPaginationDAO();
        ISearchResultDAO searchResultDAO=dalFactory.getSearchResultDAO();

        ITransliterationDAO transliterationDAO=dalFactory.getTransliterationDAO();  
        ILemmatizationDAO lemmatizationDAO=dalFactory.getLemmatizationDAO();
        IPOSTaggingDAO postaggingDAO=dalFactory.getPOSTaggingDAO();
        
        
        IDALFacade dalFacade = new DALFacade(fileDAO, fileImportDAO, paginationDAO,searchResultDAO,transliterationDAO,lemmatizationDAO,postaggingDAO);

        
        IFileBO fileBO = new FileBO(dalFacade);
        IFileImportBO fileImportBO = new FileImportBO(dalFacade);
        IFilePaginationBO filePaginationBO = new FilePaginationBO(dalFacade);
        ISearchResultBO searchResultBO=new SearchResultBO(dalFacade);

        ITransliterationBO transliterationBO=new TransliterationBO(dalFacade);
        ILemmatizationBO lemmatizationBO=new LemmatizationBO(dalFacade);
        IPOSTaggingBO posTaggingBO =new POSTaggingBO (dalFacade);
        ITFIDFAnalysisBO tfidfAnalysisBO=new TFIDFAnalysisBO(dalFacade);
        IPMIAnalysisBO pmiAnalysisBO=new PMIAnalysisBO(dalFacade);
        IKLAnalysisBO klAnalysisBO=new KLAnalysisBO(dalFacade);
        IBLFacade blFacade;
		try {
			blFacade = new BLFacade(fileBO,fileImportBO,filePaginationBO,searchResultBO,transliterationBO,lemmatizationBO,posTaggingBO,tfidfAnalysisBO,pmiAnalysisBO,klAnalysisBO);
			Registry registry;
			registry = LocateRegistry.getRegistry(1099);
			registry.rebind("BLFacade", blFacade);
			System.out.println("Server is running...");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	
		
	}

}
