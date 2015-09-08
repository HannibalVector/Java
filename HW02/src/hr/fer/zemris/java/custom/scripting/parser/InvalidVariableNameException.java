package hr.fer.zemris.java.custom.scripting.parser;

public class InvalidVariableNameException extends SmartScriptParserException {

	private static final long serialVersionUID = 1L;

	public InvalidVariableNameException() {
		super("Invalid variable or function name!");
	}
}
