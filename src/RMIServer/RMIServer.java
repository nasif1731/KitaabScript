package RMIServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import javax.swing.JOptionPane;

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
import interfaces.IBLFacade;
public class RMIServer {

    public static void main(String[] args) throws RemoteException {
        try {
            // Load configurations
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));

            String host = config.getProperty("server.host");
            int port = Integer.parseInt(config.getProperty("server.port"));

            // Initialize DAL and BLL components
            IDALFactory dalFactory = AbstractDALFactory.getInstance();
            IDALFacade dalFacade = new DALFacade(
                dalFactory.getFileDAO(), 
                dalFactory.getFileImportDAO(), 
                dalFactory.getPaginationDAO(), 
                dalFactory.getSearchResultDAO(), 
                dalFactory.getTransliterationDAO(), 
                dalFactory.getLemmatizationDAO(), 
                dalFactory.getPOSTaggingDAO()
            );

            IBLFacade blFacade = new BLFacade(
                new FileBO(dalFacade), 
                new FileImportBO(dalFacade), 
                new FilePaginationBO(dalFacade), 
                new SearchResultBO(dalFacade), 
                new TransliterationBO(dalFacade), 
                new LemmatizationBO(dalFacade), 
                new POSTaggingBO(dalFacade), 
                new TFIDFAnalysisBO(dalFacade), 
                new PMIAnalysisBO(dalFacade), 
                new KLAnalysisBO(dalFacade)
            );

            // Start the RMI server
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("BLFacade", blFacade);
            JOptionPane.showMessageDialog(null, "Server is running");

        } catch (IOException e) {
        	JOptionPane.showMessageDialog(null, "Server exception");
            e.printStackTrace();
        }
    }
}
