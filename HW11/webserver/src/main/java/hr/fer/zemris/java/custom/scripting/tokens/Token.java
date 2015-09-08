package hr.fer.zemris.java.custom.scripting.tokens;

/**
 *  Base class for all tokens. Tokens are used for the representation of expressions.
 *
 */
public abstract class Token {

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this {@code Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code Token}.
     */
	public String asText() {
		return "";
	}

	/**
	 * Accepts visitor by calling appropriate method in {@link ITokenVisitor} interface.
	 * @param visitor instance of {@link ITokenVisitor} interface.
	 */
	public abstract void accept (ITokenVisitor visitor);
}
