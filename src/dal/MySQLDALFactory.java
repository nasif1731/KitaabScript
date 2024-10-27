package dal;

public class MySQLDALFactory extends AbstractDALFactory{

	@Override
	public IFileDAO getFileDAO() {
		return new FileDAO();
	}

	@Override
	public IFileImportDAO getFileImportDAO() {
		return new FileImportDAO();
	}

}
