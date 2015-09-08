package hr.fer.zemris.java.student0177035204.hw15.dao.jpa;

import hr.fer.zemris.java.student0177035204.hw15.dao.DAO;
import hr.fer.zemris.java.student0177035204.hw15.dao.DAOException;
import hr.fer.zemris.java.student0177035204.hw15.model.BlogEntry;

/**
 * Implementation of the {@link DAO} interface.
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}
}
