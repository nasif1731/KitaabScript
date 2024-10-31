package bll;


import dal.IFileDAO;
import dto.FileDTO;
import dto.PageDTO;

import java.util.List;

public class FileBO {
	private IFileDAO fileDAO;

   
    public FileBO(IFileDAO fileDAO) {
        this.fileDAO = fileDAO;
    }
    
    public void createFile(String name, String content) {
    	
        try {
        	fileDAO.createFile(name, content); 
        } catch (Exception e) {
            throw new RuntimeException("Error in file creation: " + e.getMessage(), e);
        }
    }

    public void printFileTimestamps(String name) {
        try {
            String createdAt = fileDAO.createdAt(name);
            String updatedAt = fileDAO.updatedAt(name);
            System.out.println("Created At: " + createdAt);
            System.out.println("Updated At: " + updatedAt);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving timestamps: " + e.getMessage(), e);
        }
    }

    public void updateFile(String name, String content) {
        try {
//        	System.out.println(name);
//    		System.out.println(content);
        	fileDAO.updateFile(name, content);
        } catch (Exception e) {
            throw new RuntimeException("Error updating file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String name)  {
        try {
        	fileDAO.deleteFile(name);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }

    public List<FileDTO> getAllFiles() {
        return fileDAO.getAllFiles();
    }

    public FileDTO getOneFile(String fileName) {
        try {
            return fileDAO.getOneFile(fileName);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file: " + e.getMessage(), e);
        }
    }

    public int getWordCount(String fileName) {
        return fileDAO.getWordCount(fileName);
    }
	public FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO) {
		 FileDTO fileDTO = getOneFile(fileName);
	        if (fileDTO != null) {
	            List<PageDTO> paginatedContent = paginationBO.paginateContent(fileDTO.getId(), fileDTO.getContent());
	            fileDTO.setPaginatedContent(paginatedContent); 
	        }
	        return fileDTO;
	    }
	}
