package hr.fer.zemris.java.tecaj.hw07.shell;

/**
 * Represents exception which is thrown if error occurs during
 * execution of shell command.
 * @author ilijan
 */
public class ShellCommandException extends RuntimeException {
    /**
     * Constructor takes message to be printed to user if exception occurs.
     * @param message message to be written to user.
     */
    public ShellCommandException(String message) {
        super(message);
    }
}
