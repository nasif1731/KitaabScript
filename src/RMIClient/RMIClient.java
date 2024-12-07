package RMIClient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.IBLFacade;
import pl.MainScreen;

public class RMIClient {
	public static void main(String[] args) {
		try {
			Registry registry=LocateRegistry.getRegistry("localhost", 1099);
			IBLFacade blFacade = (IBLFacade) registry.lookup("BLFacade");
			 MainScreen mainFrame = new MainScreen(blFacade);
             mainFrame.setVisible(true);
		}catch(Exception e) {
				e.printStackTrace();
		}	
	}
}
