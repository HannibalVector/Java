package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents string by storing its contents.
 * Inherits from class {@link Token}.
 */
public class TokenString extends Token {

    /** String contained in token. */
	private String value;

    /**
     * Returns text contained in token.
     * @return Text contained in token.
     */
	public String getValue() {
		return value;
	}

    /**
     * Constructor that takes the text to be represented by token as single input parameter.
     * @param value Text to be stored in token.
     */
	public TokenString(String value) {
		this.value = value;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenString}.
     * The method generates escape characters where needed to ensure that generated text
     * can be again parsed to the semantically equal node.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenString}.
     */
	@Override
	public String asText() {
        return "\""+value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenString}
     * and contains the same text as the node).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenString)) {
            return false;
        }
        TokenString other = (TokenString) object;
        return value.equals(other.getValue());
    }
}
