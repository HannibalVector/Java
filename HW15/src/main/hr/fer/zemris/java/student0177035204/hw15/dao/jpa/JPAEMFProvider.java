package hr.fer.zemris.java.student0177035204.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * JPA entity manager factory provider.
 */
public class JPAEMFProvider {

	/** Entity manager factory. */
	public static EntityManagerFactory emf;

	/**
	 * Getter for the entity manager factory.
	 * @return entity manager factory.
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * Setter for the entity manager factory.
	 * @param emf	new entity manager factory.
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
