package hr.fer.zemris.java.hw14.dao.sql;

import hr.fer.zemris.java.hw14.model.PollOption;
import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.Poll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the DAO subsystem using SQL technology. Connection is provided using
 * {@link SQLConnectionProvider} class.
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getBasicEntryList() throws DAOException {
		List<Poll> entries = new ArrayList<>();
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement preparedStatement
					 = connection.prepareStatement("select id, title from Polls order by id");
				ResultSet resultSet = preparedStatement.executeQuery()){

			while (resultSet != null && resultSet.next()) {
				Poll poll = new Poll();
				poll.setID(resultSet.getLong("id"));
				poll.setTitle(resultSet.getString("title"));
				entries.add(poll);
			}
		} catch(Exception ex) {
			throw new DAOException("Error while reading list of polls.", ex);
		}
		return entries;
	}

	@Override
	public Poll getEntry(long id) throws DAOException {
		Poll poll = null;
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pollStatement =
					 connection.prepareStatement("select id, title, message from Polls where id=?");
			 PreparedStatement optionsStatement =
					 connection.prepareStatement(
							 "select id, optionTitle, optionLink, votesCount, pollID from PollOptions where pollID=?")){
			pollStatement.setLong(1, Long.valueOf(id));
			optionsStatement.setLong(1, Long.valueOf(id));
			try (ResultSet pollResultSet = pollStatement.executeQuery();
			ResultSet optionsResultSet = optionsStatement.executeQuery()){
				if(pollResultSet!=null && pollResultSet.next()) {
					poll = new Poll();
					poll.setID(pollResultSet.getLong("id"));
					poll.setTitle(pollResultSet.getString("title"));
					poll.setMessage(pollResultSet.getString("message"));
					while (optionsResultSet != null && optionsResultSet.next()) {
						PollOption option
								= new PollOption(
								optionsResultSet.getLong("id"),
								optionsResultSet.getString("optionTitle"),
								optionsResultSet.getString("optionLink"),
								optionsResultSet.getInt("votesCount")
						);
						poll.getPollOptions().add(option);
					}
				}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while getting poll data.", ex);
		}
		return poll;
	}

	@Override
	public void updateVotes(PollOption option) throws DAOException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement updateStatement =
					 connection.prepareStatement(
							 "UPDATE PollOptions set votesCount=? WHERE id=?")){
			updateStatement.setLong(2, option.getID());
			updateStatement.setInt(1, option.getVotes());
			updateStatement.executeUpdate();
		} catch(Exception ex) {
			throw new DAOException("Error while updating poll votes.", ex);
		}
	}

	/**
	 * Creates table in the database if it doesn't exist.
	 * @param name			name of the table to be created.
	 * @param createCommand	SQL command used to create new table.
	 * @return				{@code true} if table already exists, {@code false} if new table is created.
	 * @throws SQLException	if error occurs while manipulating database.
	 */
	private boolean createTableIfNotExists(String name, String createCommand) throws SQLException {
		Connection connection = SQLConnectionProvider.getConnection();
		try (PreparedStatement createStatement =
					 connection.prepareStatement(createCommand, Statement.NO_GENERATED_KEYS)) {

			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet results = metaData.getTables(null, null, name.toUpperCase(), null);
			if (results.next()) {
				return true;
			} else {
				createStatement.execute();
				return false;
			}
		}
	}

	@Override
	public boolean createTablesIfNotExists() {
		try {
			boolean pollsExist = createTableIfNotExists("Polls",
					"CREATE TABLE Polls\n" +
					"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
					"title VARCHAR(150) NOT NULL,\n" +
					"message CLOB(2048) NOT NULL\n" +
					")");
			boolean optionsExist = createTableIfNotExists("PollOptions",
					"CREATE TABLE PollOptions\n" +
					"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
					"optionTitle VARCHAR(100) NOT NULL,\n" +
					"optionLink VARCHAR(150) NOT NULL,\n" +
					"pollID BIGINT,\n" +
					"votesCount BIGINT,\n" +
					"FOREIGN KEY (pollID) REFERENCES Polls(id)\n" +
					")");
			return pollsExist && optionsExist;
		} catch (Exception ex) {
			throw new DAOException("Error while creating tables.", ex);
		}
	}

	@Override
	public void addPolls(List<Poll> polls) {
		Connection connection = SQLConnectionProvider.getConnection();
		for (Poll poll : polls) {
			try (PreparedStatement insertStatement =
						 connection.prepareStatement(
								 "INSERT INTO Polls (title, message) values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
				insertStatement.setString(1, poll.getTitle());
				insertStatement.setString(2, poll.getMessage());
				insertStatement.executeUpdate();
				ResultSet generated = insertStatement.getGeneratedKeys();
				if (generated != null && generated.next()) {
					long pollID = generated.getLong(1);
					for (PollOption option : poll.getPollOptions()) {
						try (PreparedStatement insertOptions =
									 connection.prepareStatement(
											 "INSERT INTO PollOptions (optionTitle, optionLink, pollID) values (?,?,?)",
											 Statement.RETURN_GENERATED_KEYS)) {
							insertOptions.setString(1, option.getName());
							insertOptions.setString(2, option.getLink());
							insertOptions.setLong(3, pollID);
							insertOptions.executeUpdate();
						}
					}
				}
			} catch (Exception ex) {
				throw new DAOException("Error while adding polls.", ex);
			}
		}
	}
}
