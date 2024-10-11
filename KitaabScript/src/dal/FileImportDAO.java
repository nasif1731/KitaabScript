package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import dto.FileDTO;
import util.DatabaseConnection;
import util.HashGenerator;

public class FileImportDAO {

	public void importFile(FileDTO file) throws SQLException {
		String query = "INSERT INTO text_files (filename, content, language, hash, word_count) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			String fileHash = HashGenerator.generateHashFromContent(file.getContent());
			file.setHash(fileHash);
			stmt.setString(1, file.getFilename());
			stmt.setString(2, file.getContent());
			stmt.setString(3, file.getLanguage());
			stmt.setString(4, file.getHash());
			stmt.setInt(5, file.getWordCount());

			stmt.executeUpdate();
		}
	}

	public void bulkImportFiles(List<FileDTO> files) throws SQLException {
		String query = "INSERT INTO text_files (filename, content, language, hash, word_count) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			for (FileDTO file : files) {

				String fileHash = HashGenerator.generateHashFromContent(file.getContent());
				file.setHash(fileHash);

				stmt.setString(1, file.getFilename());
				stmt.setString(2, file.getContent());
				stmt.setString(3, file.getLanguage());
				stmt.setString(4, file.getHash());
				stmt.setInt(5, file.getWordCount());

				stmt.addBatch();
			}

			stmt.executeBatch();
		}
	}
	public boolean doesHashExist(String hash) throws SQLException {
	    String query = "SELECT COUNT(*) FROM text_files WHERE hash = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, hash);
	        try (ResultSet rs = stmt.executeQuery()) {
	            return rs.next() && rs.getInt(1) > 0;
	        }
	    }
	}

}
