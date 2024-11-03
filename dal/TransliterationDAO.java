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
        try (Connection conn = DatabaseConnection.getConnection();
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
        try (Connection conn = DatabaseConnection.getConnection();
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
    public boolean isTransliterationSavedForPage(int pageId) {
        String query = "SELECT COUNT(*) AS count FROM transliterations WHERE pagination_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, pageId);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
