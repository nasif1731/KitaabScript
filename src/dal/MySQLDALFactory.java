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

	@Override
	public ITransliterationDAO getTransliterationDAO() {
	
		return new TransliterationDAO();
	}

	@Override
	public ILemmatizationDAO getLemmatizationDAO() {
		// TODO Auto-generated method stub
		return new LemmatizationDAO();
	}

	@Override
	public IPOSTaggingDAO getPOSTaggingDAO() {
		// TODO Auto-generated method stub
		return new POSTaggingDAO();
	}
}
