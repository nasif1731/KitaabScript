package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dto.TransliterationDTO;
import util.DatabaseConnection;

public class TransliterationDAO implements ITransliterationDAO {

    @Override
    public void addTransliteration(TransliterationDTO transliteration) {
        String query = "INSERT INTO transliterations (pagination_id, original_text, transliterated_text) VALUES (?, ?, ?)";
        try (Connection conn =DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transliteration.getPaginationId());
            stmt.setString(2, transliteration.getOriginalText());
            stmt.setString(3, transliteration.getTransliteratedText());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TransliterationDTO> getTransliterationsForPage(int pageId) {
        List<TransliterationDTO> transliterations = new ArrayList<>();
        String query = "SELECT * FROM transliterations WHERE pagination_id=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pageId);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                TransliterationDTO transliteration = new TransliterationDTO(
                        result.getInt("id"),
                        result.getInt("pagination_id"),
                        result.getString("original_text"),
                        result.getString("transliterated_text")
                );
                transliterations.add(transliteration);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transliterations;
    }

    @Override
    
    public boolean isTransliterationSavedForPage(int pageId, String newContent) {
        String queryCheck = "SELECT COUNT(*) AS count, original_text FROM transliterations WHERE pagination_id=?";
        String queryDelete = "DELETE FROM transliterations WHERE pagination_id=?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(queryCheck);
             PreparedStatement stmtDelete = conn.prepareStatement(queryDelete)) {
            
            stmtCheck.setInt(1, pageId);
            ResultSet result = stmtCheck.executeQuery();
            
            if (result.next()) {
                int count = result.getInt("count");
                String existingContent = result.getString("original_text");
                
                if (count > 0 && existingContent.equals(newContent)) {
                    return true; 
                } else if (count > 0 && !existingContent.equals(newContent)) {
                    
                    stmtDelete.setInt(1, pageId);
                    stmtDelete.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  
    }


}
