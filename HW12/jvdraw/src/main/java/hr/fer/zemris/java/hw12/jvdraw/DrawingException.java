package hr.fer.zemris.java.hw12.jvdraw;

/**
 * Thrown when exceptions in handling drawings of type {@link Drawing} occur.
 * @author ilijan
 */
public class DrawingException extends RuntimeException {
    /**
     * Default constructor.
     */
    public DrawingException() {
    }

    /**
     * Initializes exception using message to be shown to the user.
     * @param message   message to be shown.
     */
    public DrawingException(String message) {
        super(message);
    }
}
