package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown if {@link CharReader} tries to read after the end of string.
 * @author ilijan
 *
 */
public class CharReaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CharReaderException() {
		super();
	}

	public CharReaderException(String message, Throwable cause,
                               boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CharReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	public CharReaderException(String message) {
		super(message);
	}

	public CharReaderException(Throwable cause) {
		super(cause);
	}
}
