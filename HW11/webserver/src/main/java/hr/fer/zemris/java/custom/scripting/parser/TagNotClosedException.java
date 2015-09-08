package hr.fer.zemris.java.custom.scripting.parser;

public class TagNotClosedException extends SmartScriptParserException {

	private static final long serialVersionUID = 1L;

	public TagNotClosedException() {
		super("Tag is not properly closed.");
	}
}
