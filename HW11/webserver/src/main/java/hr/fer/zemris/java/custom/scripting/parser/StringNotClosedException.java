package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown if string is not properly closed inside a tag.
 */
public class StringNotClosedException extends SmartScriptParserException {

	private static final long serialVersionUID = 1L;

	public StringNotClosedException() {
		super("String is not properly closed.");
	}
}
