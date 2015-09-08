package hr.fer.zemris.java.tecaj.hw3;

/**
 * Exception to be thrown if exception occurs while iterating over {@link IntegerSequence}.
 * Created by ilijan on 4/1/15.
 */
public class IntegerSequenceException extends RuntimeException {
    public IntegerSequenceException() {
    }

    public IntegerSequenceException(String message) {
        super(message);
    }
}
