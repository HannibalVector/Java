package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents operator by its symbol.
 * Inherits from class {@link Token}.
 *
 */
public class TokenOperator extends Token {

    /** Operator symbol. */
	private String symbol;

    /**
     * Returns symbol of the operator represented by the token.
     * @return  Symbol of the operator.
     */
	public String getSymbol() {
		return symbol;
	}

    /**
     * Constructor that takes operator symbol as input parameter.
     * @param symbol  Symbol of the operator that token represents.
     */
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenOperator}.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenOperator}.
     */
	@Override
	public String asText() {
		return symbol;
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenOperator}
     * and has the same symbol as the token).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenOperator)) {
            return false;
        }
        TokenOperator other = (TokenOperator) object;
        return symbol.equals(other.getSymbol());
    }
}
