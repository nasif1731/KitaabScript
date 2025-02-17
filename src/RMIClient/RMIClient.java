package RMIClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import interfaces.IBLFacade;
import pl.MainScreen;

public class RMIClient {
    public static void main(String[] args) {
        try {
            
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));

            String host = config.getProperty("server.host");
            int port = Integer.parseInt(config.getProperty("server.port"));

            
            Registry registry = LocateRegistry.getRegistry(host, port);
            IBLFacade blFacade = (IBLFacade) registry.lookup("BLFacade");

           
            MainScreen mainFrame = new MainScreen(blFacade);
            mainFrame.setVisible(true);
        } catch (IOException e) {
            System.err.println("Error reading configuration file.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
