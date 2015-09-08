package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.dao.sql.SQLDAO;

/**
 * Singleton class which returns DAO provider used to access data persistence subsystem.
 */
public class DAOProvider {

	/**
	 * Data persistence provider.
	 */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Gets single instance of DAO provider.
	 * 
	 * @return DAO provider.
	 */
	public static DAO getDao() {
		return dao;
	}
	
}
