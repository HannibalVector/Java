package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;

/**
 * Stores database connections in {@link ThreadLocal} object.
 */
public class SQLConnectionProvider {

	/**
	 * Map of connections mapped to threads.
	 */
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Sets connection for current thread, or deletes map entry for current thread if parameter {@code con}
	 * is {@code null}.
	 * @param con	database connection to be mapped to current thread.
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Gets connection mapped to current thread.
	 * @return	database connection mapped to current thread.
	 */
	public static Connection getConnection() {
		return connections.get();
	}
}
