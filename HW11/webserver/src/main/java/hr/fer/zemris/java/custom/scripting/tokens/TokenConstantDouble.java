package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents single constant number of type {@code Double}.
 * It inherits from class {@link Token}.
 */
public class TokenConstantDouble extends Token {

    /** Value represented by token. */
	private double value;

    /**
     * Returns value represented by token {@code TokenConstantDouble}.
     * @return Value represented by token.
     */
	public double getValue() {
		return value;
	}

    /**
     * Constructor initializes object of class {@link TokenConstantDouble} using value.
     * @param value Value of type {@code Double} which token represents.
     */
	public TokenConstantDouble(double value) {
		this.value = value;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenConstantDouble}.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenConstantDouble}.
     */
	@Override
	public String asText() {
		return String.valueOf(value);
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenConstantDouble}
     * and represents the same real value as the token).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenConstantDouble)) {
            return false;
        }
        TokenConstantDouble other = (TokenConstantDouble) object;
        double epsilon = 1.0e-10;
        if (Math.abs(value - other.getValue()) < epsilon) {
            return true;
        }
        return false;
    }

    @Override
    public void accept(ITokenVisitor visitor) {
        visitor.visitTokenConstantDouble(this);
    }
}
