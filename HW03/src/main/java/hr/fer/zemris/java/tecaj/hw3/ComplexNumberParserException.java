package hr.fer.zemris.java.tecaj.hw3;

/**
 * Exception thrown if ComplexNumberParser cannot parse given string.
 * Created by ilijan on 3/31/15.
 */
public class ComplexNumberParserException extends RuntimeException {
    public ComplexNumberParserException() {
    }

    public ComplexNumberParserException(String message) {
        super(message);
    }
}
