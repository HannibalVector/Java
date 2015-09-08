package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Exception thrown if invalid number of arguments is passed to FOR tag.
 */
public class ForLoopArgumentsException extends SmartScriptParserException {

	private static final long serialVersionUID = 1L;

	public ForLoopArgumentsException() {
		super("Invalid FOR loop arguments.");
	}
}
