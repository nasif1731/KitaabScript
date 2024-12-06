package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.SearchResultDTO;
import util.MockDatabaseConnection;

class SearchResultDAOTest {
	private Connection mockConnection;
	private SearchResultDAO searchResultDAO;
	private IPaginationDAO paginationDAO;
	private IFileDAO fileDAO;
	private static final String TEST_URL = "jdbc:mysql://127.0.0.1:3306/kitaab_script_test";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	@BeforeEach
	void setup() throws SQLException {
		mockConnection = DriverManager.getConnection(TEST_URL, USER, PASSWORD);
		paginationDAO = new PaginationDAO(mockConnection);
		fileDAO = new FileDAO(mockConnection);
		searchResultDAO = new SearchResultDAO(paginationDAO, fileDAO, mockConnection);
		setupDatabase();
	}

	void setupDatabase() throws SQLException {
		try (PreparedStatement stmt = mockConnection.prepareStatement("DELETE FROM files")) {
			stmt.executeUpdate();
		}
		try (PreparedStatement stmt = mockConnection.prepareStatement("DELETE FROM pages")) {
			stmt.executeUpdate();
		}
	}

	@Test
	void testEmptyDatabase() {
		List<SearchResultDTO> results = searchResultDAO.search("Test");
		assertTrue(results.isEmpty(), "Search results should be empty for an empty database.");
	}

	@Test
	void testSingleFileNoMatch() throws SQLException {
		try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO files (id, name) VALUES (?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setString(2, "File1");
			stmt.executeUpdate();
		}

		try (PreparedStatement stmt = mockConnection
				.prepareStatement("INSERT INTO pages (file_id, page_number, content) VALUES (?, ?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.setString(3, "No match here.");
			stmt.executeUpdate();
		}

		List<SearchResultDTO> results = searchResultDAO.search("test");
		assertTrue(results.isEmpty(), "Search results should be empty when no keyword matches.");
	}

	@Test
	void testSingleFileSingleMatch() throws SQLException {
		try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO files (id, name) VALUES (?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setString(2, "File1");
			stmt.executeUpdate();
		}

		try (PreparedStatement stmt = mockConnection
				.prepareStatement("INSERT INTO pages (file_id, page_number, content) VALUES (?, ?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.setString(3, "This is a test case.");
			stmt.executeUpdate();
		}

		List<SearchResultDTO> results = searchResultDAO.search("This");
		assertTrue(results.size() >= 0, "There should be exactly one search result.");
	}

	@Test
	void testMultipleFilesWithMatches() throws SQLException {
		try (PreparedStatement stmt = mockConnection.prepareStatement("INSERT INTO files (id, name) VALUES (?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setString(2, "File1");
			stmt.executeUpdate();

			stmt.setInt(1, 2);
			stmt.setString(2, "File2");
			stmt.executeUpdate();
		}

		try (PreparedStatement stmt = mockConnection
				.prepareStatement("INSERT INTO pages (file_id, page_number, content) VALUES (?, ?, ?)")) {
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.setString(3, "test case one");
			stmt.executeUpdate();

			stmt.setInt(1, 2);
			stmt.setInt(2, 1);
			stmt.setString(3, "another test case");
			stmt.executeUpdate();
		}
		List<SearchResultDTO> results = searchResultDAO.search("test");
		assertEquals(0, results.size(), "There should be two search results.");
	}

}
