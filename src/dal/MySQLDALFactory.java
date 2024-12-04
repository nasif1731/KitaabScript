package dal;

import java.sql.Connection;
import java.sql.SQLException;

import util.DatabaseConnection;

public class MySQLDALFactory extends AbstractDALFactory{
	private Connection conn;
	public MySQLDALFactory() {
		try {
			try {
				conn = DatabaseConnection.getInstance().getConnection();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public IFileDAO getFileDAO() {
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new FileDAO(conn);
	}

	@Override
	public IFileImportDAO getFileImportDAO() {
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new FileImportDAO(conn);
	}

	@Override
	public IPaginationDAO getPaginationDAO() {
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new PaginationDAO(conn);
	}
	@Override
    public ISearchResultDAO getSearchResultDAO() {
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return new SearchResultDAO(getPaginationDAO(), getFileDAO(),conn);
    }

	@Override
	public ITransliterationDAO getTransliterationDAO() {
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new TransliterationDAO(conn);
	}

	@Override
	public ILemmatizationDAO getLemmatizationDAO() {
		// TODO Auto-generated method stub
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new LemmatizationDAO(conn);
	}

	@Override
	public IPOSTaggingDAO getPOSTaggingDAO() {
		// TODO Auto-generated method stub
		try {
			if (conn == null || conn.isClosed()) {
			    try {
					try {
						conn = DatabaseConnection.getInstance().getConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new POSTaggingDAO(conn);
	}
}
