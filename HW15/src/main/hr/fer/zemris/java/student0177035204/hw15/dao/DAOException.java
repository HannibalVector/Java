package hr.fer.zemris.java.student0177035204.hw15.dao;

/**
 * Exception thrown if error occurs in communication with data persistence layer.
 */
public class DAOException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor initializes new exception using message and cause.
	 * @param message	message for the user.
	 * @param cause		cause of the exception.
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
