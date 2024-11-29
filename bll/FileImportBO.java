package bll;

import java.util.ArrayList;
import java.util.List;

import dal.IDALFacade;

public class FileImportBO implements IFileImportBO {

	private final IDALFacade dalFacade;
	
    public FileImportBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
    }
    @Override
    public String importFile(String filePath) {
        return dalFacade.importFile(filePath);
    }
    @Override
    public List<String> bulkImportFiles(List<String> filePaths) {
        List<String> results = new ArrayList<>();
        for (String filePath : filePaths) {
            results.add(importFile(filePath)); // Using the importFile method to handle individual file imports
        }
        return results;
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
