package bll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dal.FileImportDAO;
import dto.FileDTO;
import util.HashGenerator;

public class FileImportBO {

	private final FileImportDAO fileImportDAO;

	public FileImportBO() {
		this.fileImportDAO = new FileImportDAO();
	}

	public String importFile(File file) throws SQLException, IOException {
		String hash = HashGenerator.generateHashFromContent(getFileContent(file));

		if (fileImportDAO.doesHashExist(hash)) {
			return "Cannot import: A similar file already exists in the database.";
		}

		FileDTO fileDTO = createFileDTO(file, hash);
		fileImportDAO.importFile(fileDTO);
		return "File imported successfully.";
	}

	public List<String> bulkImportFiles(List<File> files) throws SQLException, IOException {
		List<String> messages = new ArrayList<>();

		for (File file : files) {
			String hash = HashGenerator.generateHashFromContent(getFileContent(file));

			if (fileImportDAO.doesHashExist(hash)) {
				messages.add("Cannot import: " + file.getName() + " - A similar file already exists in the database.");
			} else {
				FileDTO fileDTO = createFileDTO(file, hash);
				fileImportDAO.importFile(fileDTO);
				messages.add("File imported successfully: " + file.getName());
			}
		}

		return messages;
	}
	public static String getFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

	private FileDTO createFileDTO(File file, String hash) throws IOException {
		String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));

		String language = determineFileLanguage(file);
		int wordCount = countWords(content);

		return new FileDTO(file.getName(), content, language, hash, wordCount);
	}

	private String determineFileLanguage(File file) throws IOException {

		String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
		if (isUrdu(content)) {
			return "Urdu";
		} else if (isArabic(content)) {
			return "Arabic";
		} else {
			return "English";
		}
	}

	private boolean isUrdu(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch == 'ٹ' || ch == 'ڈ' || ch == 'ڑ' || ch == 'ں' || ch == 'ے' || ch == 'ؤ' || ch == 'ۓ') {
				return true;
			}
		}
		return false;
	}

	private boolean isArabic(String content) {
		for (int i = 0; i < content.length(); i++) {
			char ch = content.charAt(i);
			if (ch >= 0x0600 && ch <= 0x06FF && !isUrdu(String.valueOf(ch))) {
				return true;
			}
		}
		return false;
	}

	private int countWords(String content) {
		if (content.isEmpty()) {
			return 0;
		} else {
			return content.split("\\s+").length;
		}
	}

//	public static void main(String[] args) {
//		FileImportBO fileImportBO = new FileImportBO();
//
//		File testFile = new File("C:\\Users\\nasif\\Downloads\\فائل.txt");
//		try {
//			String result = fileImportBO.importFile(testFile);
//			System.out.println(result);
//		} catch (SQLException | IOException e) {
//			e.printStackTrace();
//		}
//
////		File file1 = new File("file1.txt");
////		File file2 = new File("file2.txt");
////		File file3 = new File("file3.txt");
////
////		List<File> files = new ArrayList<>();
////		files.add(file1);
////		files.add(file2);
////		files.add(file3);
////
////		try {
////			List<String> results = fileImportBO.bulkImportFiles(files);
////			for (String message : results) {
////				System.out.println(message);
////			}
////		} catch (SQLException | IOException e) {
////			e.printStackTrace();
////		}
//	}
}
