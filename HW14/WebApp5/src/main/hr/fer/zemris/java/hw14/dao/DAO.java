package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.model.PollOption;
import hr.fer.zemris.java.hw14.model.Poll;

import java.util.List;

/**
 * Interface towards data persistence subsystem.
 */
public interface DAO {

	/**
	 * Gets all database entries, but only fills fields for id and title of the poll.
	 * @return	List of all database entries.
	 * @throws DAOException	if error occurs while reading database.
	 */
	List<Poll> getBasicEntryList() throws DAOException;

	/**
	 * Gets Poll for given id. If entry does not exist returs null.
	 * @param id		id of the requested poll.
	 * @return			requested poll.
	 * @throws DAOException		if error occurs while reading database.
	 */
	Poll getEntry(long id) throws DAOException;

	/**
	 * Updates votes count for the given {@link PollOption}.
	 * @param option			poll option for which votes count is updated.
	 * @throws DAOException		if error occurs while manipulating database.
	 */
	void updateVotes(PollOption option) throws DAOException;

	/**
	 * Creates new tables in the database, if they don't exist.
	 */
	boolean createTablesIfNotExists();

	/**
	 * Adds list of polls and corresponding poll options to respective tables.
	 * @param polls list of polls to fill in database tables.
	 */
	void addPolls(List<Poll> polls);
}
