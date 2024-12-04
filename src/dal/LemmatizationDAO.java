
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

import dto.LemmatizationDTO;
import util.DatabaseConnection;

public class LemmatizationDAO implements ILemmatizationDAO{
   private Connection conn;

public LemmatizationDAO(Connection conn)
   {
	   this.conn=conn;
   }
	@Override
	public void addLemmatization(LemmatizationDTO lemmatization) {
	    String checkQuery = "SELECT COUNT(*) FROM lemmatization WHERE pagination_id = ? AND word = ? AND lemma = ? AND root = ?";
	    String insertQuery = "INSERT INTO lemmatization (pagination_id, word, lemma, root) VALUES (?, ?, ?, ?)";

	    try ( 
	         PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
	        checkStatement.setInt(1, lemmatization.getPaginationId());
	        checkStatement.setString(2, lemmatization.getWord());
	        checkStatement.setString(3, lemmatization.getLemma());
	        checkStatement.setString(4, lemmatization.getRoot());

	        try (ResultSet rs = checkStatement.executeQuery()) {
	            if (rs.next() && rs.getInt(1) == 0) { 
	                try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
	                    insertStatement.setInt(1, lemmatization.getPaginationId());
	                    insertStatement.setString(2, lemmatization.getWord());
	                    insertStatement.setString(3, lemmatization.getLemma());
	                    insertStatement.setString(4, lemmatization.getRoot());
	                    insertStatement.executeUpdate();
	                }
	            }
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error while adding lemmatization entry.", e);
	    }
	}


	@Override
    public List<LemmatizationDTO> getLemmatizationForPage(int pageId) {
        String query = "SELECT id, pagination_id, word, lemma, root FROM lemmatization WHERE pagination_id = ?";
        List<LemmatizationDTO> lemmatizations = new ArrayList<>();

        try ( 
				PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, pageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                	int id=resultSet.getInt("id");
                    String word=resultSet.getString("word");
                    String lemma=resultSet.getString("lemma");
                    String root=resultSet.getString("root");
                    LemmatizationDTO dto = new LemmatizationDTO(id,pageId,word,lemma,root);
                    
                    lemmatizations.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving lemmatization entries for page ID: " + pageId, e);
        }
        return lemmatizations;
    }

	@Override
	public boolean isLemmatizationSavedForPage(int pageId, String newContent) {
	    String query = "SELECT word, lemma, root FROM lemmatization WHERE pagination_id = ?";
	    String[] words = newContent.split("\\s+");
	    List<String> existingWords = new ArrayList<>();

	    
	    try ( 
	         PreparedStatement statement = conn.prepareStatement(query)) {
	        statement.setInt(1, pageId);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                String word = resultSet.getString("word");
	                String lemma = resultSet.getString("lemma");
	                String root = resultSet.getString("root");
	                existingWords.add(word+":"+lemma + ":" + root);  
	            }
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error while checking if lemmatization is saved for page ID: " + pageId, e);
	    }

	    for (String word : words) {
	        
	        String lemmaAndRoot = getLemmaAndRootForWord(word);

	        if (lemmaAndRoot == null || !existingWords.contains(word+":"+lemmaAndRoot)) {
	            return false;  
	        }
	    }

	    return true; 
	}

	private String getLemmaAndRootForWord(String word) {
	    try {
	        
	        File jarFile = new File("/mnt/data/AlKhalil-2.1.21.jar");
	        URLClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURI().toURL() });

	        Class<?> analyzerClass = classLoader.loadClass("AlKhalil2.AnalyzedWords");
	        Object analyzerInstance = analyzerClass.getDeclaredConstructor().newInstance();

	        Method analyzedWordsMethod = analyzerClass.getMethod("analyzedWords", String.class);
	        List<?> analyzedResults = (List<?>) analyzedWordsMethod.invoke(analyzerInstance, word);

	        String lemma = null;
	        String root = null;

	      
	        if (!analyzedResults.isEmpty()) {
	            
	            for (Object result : analyzedResults) {
	                Method getVoweledWordMethod = result.getClass().getMethod("getVoweledWord");
	                String voweledWord = (String) getVoweledWordMethod.invoke(result);

	                if (voweledWord != null && word.equals(voweledWord)) {
	                    
	                    Method getRootMethod = result.getClass().getMethod("getWordRoot");
	                    root = (String) getRootMethod.invoke(result);

	                    Method getLemmaMethod = result.getClass().getMethod("getStem");
	                    lemma = (String) getLemmaMethod.invoke(result);

	                    
	                    classLoader.close();
	                    return lemma + ":" + root;
	                }
	            }

	            
	            Object defaultResult = analyzedResults.get(0);
	            Method getRootMethod = defaultResult.getClass().getMethod("getWordRoot");
	            root = (String) getRootMethod.invoke(defaultResult);

	            Method getLemmaMethod = defaultResult.getClass().getMethod("getStem");
	            lemma = (String) getLemmaMethod.invoke(defaultResult);

	            
	            classLoader.close();
	            return lemma + ":" + root;
	        }

	        
	        classLoader.close();
	    } catch (Exception e) {
	        throw new RuntimeException("Error during lemmatization and root extraction for word: " + word, e);
	    }

	    return null;  
	}


}

