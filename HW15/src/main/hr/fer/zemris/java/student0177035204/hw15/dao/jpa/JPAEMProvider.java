package hr.fer.zemris.java.student0177035204.hw15.dao.jpa;

import hr.fer.zemris.java.student0177035204.hw15.dao.DAOException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

/**
 * JPA entity manager provider.
 */
public class JPAEMProvider {

	/** Maps threads to {@link LocalData} objects. */
	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Getter for the entity manager.
	 * @return	entity manager.
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Commits pending transaction and closes entity manager.
	 * @throws DAOException	if error occurs while communicating with the database.
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}

	/**
	 * Class wraps single {@link EntityManager} object.
	 */
	private static class LocalData {
		EntityManager em;
	}
	
}
