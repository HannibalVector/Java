package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents single constant number of type {@code int}.
 * It inherits from class {@link Token}.
 */
public class TokenConstantInteger extends Token {

    /** Value of integer represented by token. */
	private int value;

    /**
     * Getter for stored integer.
     * @return {@code int} value stored in token.
     */
	public int getValue() {
		return value;
	}

    /**
     * Constructor that takes integer to be stored in token.
     * @param value {@code int} value to be stored in token.
     */
	public TokenConstantInteger(int value) {
		this.value = value;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenConstantInteger}.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenConstantInteger}.
     */
	@Override
	public String asText() {
		return String.valueOf(value);
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenConstantInteger}
     * and has the same value as the token).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenConstantInteger)) {
            return false;
        }
        TokenConstantInteger other = (TokenConstantInteger) object;
        return value == other.getValue();
    }
}
