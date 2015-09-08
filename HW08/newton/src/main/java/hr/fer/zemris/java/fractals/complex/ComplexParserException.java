package hr.fer.zemris.java.fractals.complex;

/**
 * Exception thrown if ComplexParser cannot parse given string.
 * Created by ilijan on 3/31/15.
 */
public class ComplexParserException extends RuntimeException {
    public ComplexParserException() {
    }

    public ComplexParserException(String message) {
        super(message);
    }
}
