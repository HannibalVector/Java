package hr.fer.zemris.java.custom.collections;

/**
 * Exception is thrown when program tries to read from empty stack.
 * @author ilijan
 *
 */
public class EmptyStackException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmptyStackException() {
		super();
	}

	public EmptyStackException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EmptyStackException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyStackException(String message) {
		super(message);
	}

	public EmptyStackException(Throwable cause) {
		super(cause);
	}
	
	

}
