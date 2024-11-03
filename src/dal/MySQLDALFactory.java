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

	@Override
	public IPaginationDAO getPaginationDAO() {
		return new PaginationDAO();
	}
	@Override
    public ISearchResultDAO getSearchResultDAO() {
        
        return new SearchResultDAO(getPaginationDAO(), getFileDAO());
    }

}
