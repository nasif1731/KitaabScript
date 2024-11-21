package bll;


import dal.IDALFacade;
import dal.IFileDAO;
import dto.FileDTO;
import dto.PageDTO;

import java.sql.SQLException;
import java.util.List;

public class FileBO implements IFileBO {
	

	private final IDALFacade dalFacade;
    public FileBO(IDALFacade dalFacade) {
        this.dalFacade = dalFacade;
    }
    @Override
    public void createFile(String name, String content) {
    	
        try {
        	dalFacade.createFile(name, content); 
        } catch (Exception e) {
            throw new RuntimeException("Error in file creation: " + e.getMessage(), e);
        }
    }
    @Override
    public void printFileTimestamps(String name) {
        try {
            String createdAt = dalFacade.createdAt(name);
            String updatedAt = dalFacade.updatedAt(name);
            System.out.println("Created At: " + createdAt);
            System.out.println("Updated At: " + updatedAt);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving timestamps: " + e.getMessage(), e);
        }
    }
    @Override
    public void updateFile(String name, String content) {
        try {
//        	System.out.println(name);
//    		System.out.println(content);
        	dalFacade.updateFile(name, content);
        } catch (Exception e) {
            throw new RuntimeException("Error updating file: " + e.getMessage(), e);
        }
    }
    @Override
    public void deleteFile(String name)  {
        try {
        	dalFacade.deleteFile(name);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }
    @Override
    public List<FileDTO> getAllFiles() {
        return dalFacade.getAllFiles();
    }
    @Override
    public FileDTO getOneFile(String fileName) {
        try {
            return dalFacade.getOneFile(fileName);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file: " + e.getMessage(), e);
        }
    }
    @Override
    public int getWordCount(String fileName) {
        return dalFacade.getWordCount(fileName);
    }
    @Override
	public FileDTO paginatedFile(String fileName, FilePaginationBO paginationBO) {
		 FileDTO fileDTO = getOneFile(fileName);
	        if (fileDTO != null) {
	            List<PageDTO> paginatedContent = paginationBO.paginateContent(fileDTO.getId(), fileDTO.getContent());
	            fileDTO.setPaginatedContent(paginatedContent); 
	        }
	        return fileDTO;
	    }
	@Override
	public String getFileName(int fileId) {
		return dalFacade.getFileName(fileId);
	}
	@Override
	public int getFileID(String filename) {
		// TODO Auto-generated method stub
		try {
			return dalFacade.fetchFileIdByName(filename);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	}
