package dal;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.POSTaggingDTO;

public class POSTaggingDAO implements IPOSTaggingDAO {
	private Connection conn;
	private static final Logger logger  = LogManager.getLogger(POSTaggingDAO.class);
	public POSTaggingDAO(Connection conn) {
		this.conn=conn;
	}
    @Override
    public void addPOSTagging(POSTaggingDTO posTagging) {
        String checkQuery = "SELECT COUNT(*) FROM pos_tagging WHERE pagination_id = ? AND word = ? AND pos_tag = ?";
        String insertQuery = "INSERT INTO pos_tagging (pagination_id, word, pos_tag) VALUES (?, ?, ?)";

        try ( 
             PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, posTagging.getPaginationId());
            checkStatement.setString(2, posTagging.getWord());
            checkStatement.setString(3, posTagging.getPosTag());

            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) { 
                    try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, posTagging.getPaginationId());
                        insertStatement.setString(2, posTagging.getWord());
                        insertStatement.setString(3, posTagging.getPosTag());
                        insertStatement.executeUpdate();
//                        System.out.println("Inserted record against "+posTagging.getWord());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding POS tagging entry.", e);
        }
    }

    @Override
    public List<POSTaggingDTO> getPOSTaggingForPage(int pageId) {
        String query = "SELECT id, word, pos_tag FROM pos_tagging WHERE pagination_id = ?";
        List<POSTaggingDTO> posTaggings = new ArrayList<>();

        try ( 
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, pageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String word = resultSet.getString("word");
                    String tag = resultSet.getString("pos_tag");
//                    System.out.println("Got record against "+word);
                    POSTaggingDTO dto = new POSTaggingDTO(id, pageId, word, tag);
                    posTaggings.add(dto);
                }
            }
        } catch (SQLException e) {
        	logger.error("Error retrieving POS tagging entries for pageId: {}", pageId, e);

            throw new RuntimeException("Error while retrieving POS tagging entries for page ID: " + pageId, e);
        }
        return posTaggings;
    }

    @Override
    public boolean isPOSTaggingSavedForPage(int pageId, String newContent) {
        String query = "SELECT word, pos_tag FROM pos_tagging WHERE pagination_id = ?";
       //String[] words = newContent.split("\\s+");
        List<String> existingWords = new ArrayList<>();

        
        try ( 
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, pageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String word = resultSet.getString("word");
                    String posTag = resultSet.getString("pos_tag");
                    existingWords.add(word + ":" + posTag);

                	
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking if POS tagging is saved for page ID: " + pageId, e);
        }

        if(existingWords.size() == 0)
        {
        	return false;
        }
       
        for (String word : existingWords) {
            
            String posTag = getPosTagForWord(word);
//            if(posTag==null) System.out.println("POS Tag is null");
//            if(!existingWords.contains(word + ":" + posTag)) System.out.println("contains");
            if (posTag == null || !existingWords.contains(word + ":" + posTag)) {
//            	System.out.println("POS Tagging not saved");
                return false; 
            }
        }

        return true; 
    }

    private String getPosTagForWord(String word) {
        try {
            
            File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
            try (URLClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() })) {
                
                Class<?> posTaggerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
                Object posTaggerInstance = posTaggerClass.getDeclaredConstructor().newInstance();

                
                Method tagMethod = posTaggerClass.getMethod("analyzedWords", String.class);
                List<?> posTaggedResults = (List<?>) tagMethod.invoke(posTaggerInstance, word);

                if (!posTaggedResults.isEmpty()) {
                    for (Object result : posTaggedResults) {
                        Method getWordMethod = result.getClass().getMethod("getVoweledWord");
                        String voweledWord = (String) getWordMethod.invoke(result);

                        if (word.equals(voweledWord)) {
                        	//
                            Method getWordTypeMethod = result.getClass().getMethod("getWordType");
                            String wordType = (String) getWordTypeMethod.invoke(result);

                            return wordType; 
                        }
                    }

                    
                    Object defaultResult = posTaggedResults.get(0);
                 //   Method getWordMethod = defaultResult.getClass().getMethod("getVoweledWord");
                    Method getWordTypeMethod = defaultResult.getClass().getMethod("getWordType");

                  //  String voweledWord = (String) getWordMethod.invoke(defaultResult);
                    String wordType = (String) getWordTypeMethod.invoke(defaultResult);

                    return wordType;  
                }

            } catch (Exception e) {
                throw new RuntimeException("Error during POS tagging analysis for word: " + word, e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading Al Khalil library or performing POS tagging.", e);
        }

        return null;  
    }
}
