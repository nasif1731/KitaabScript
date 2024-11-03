package dal;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public abstract class AbstractDALFactory implements IDALFactory {
    private static IDALFactory instance = null;

    public static final IDALFactory getInstance() {
        if (instance == null) {
            // Load the properties from config file
            loadConfigProperties();

            // Get the factory class name from the system property
            String factoryClassName = System.getProperty("dal.factoryClass");

            if (factoryClassName == null) {
                throw new IllegalArgumentException("Factory class name is not set in system properties.");
            }

            try {
                Class<?> clazz = Class.forName(factoryClassName);
                instance = (IDALFactory) clazz.getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace(); // Proper logging can be added here
            }
        }
        return instance;
    }

    
    private static void loadConfigProperties() {
        try (FileInputStream input = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            System.setProperty("dal.factoryClass", properties.getProperty("dal.factoryClass"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public ISearchResultDAO getSearchResultDAO() {
		// TODO Auto-generated method stub
		return null;
	}
}
