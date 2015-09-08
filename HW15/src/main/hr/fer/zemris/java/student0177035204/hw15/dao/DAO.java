package hr.fer.zemris.java.student0177035204.hw15.dao;


import hr.fer.zemris.java.student0177035204.hw15.model.BlogEntry;

/**
 * Data Access Object provides abstract interface for persistence mechanism.
 */
public interface DAO {

	/**
	 * Gets blog entry with given {@code id}, or {@code null} if there is no such entry
	 * in the database.
	 * @param id	key for the table entry.
	 * @return		entry or {@code null} if no such entry is found.
	 * @throws DAOException	if error occurs while fetching data.
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;
}
