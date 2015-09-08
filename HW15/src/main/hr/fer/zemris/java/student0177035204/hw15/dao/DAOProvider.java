package hr.fer.zemris.java.student0177035204.hw15.dao;

import hr.fer.zemris.java.student0177035204.hw15.dao.jpa.JPADAOImpl;

/**
 * Singleton providing access to data persistence layer.
 */
public class DAOProvider {

	/** Single instance of the DAO. */
	private static DAO dao = new JPADAOImpl();

	/**
	 * Getter for the DAO.
	 * @return	DAO.
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}
