package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown if invalid tag name is used.
 */
public class InvalidTagNameException extends SmartScriptParserException {

	private static final long serialVersionUID = 1L;

	public InvalidTagNameException() {
		super("Invalid tag name!");
	}
}
