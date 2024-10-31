package dal;

public interface IDALFactory {
	public IFileDAO getFileDAO();
	public IFileImportDAO getFileImportDAO();
	public IPaginationDAO getPaginationDAO();
}
