package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents variable by its name.
 * Inherits from class {@link Token}.
 */
public class TokenVariable extends Token {

    /** Name of the variable. */
	private String name;

    /**
     * Returns name of the variable represented by the token.
     * @return  Name of the variable.
     */
	public String getName() {
		return name;
	}

    /**
     * Constructor that takes variable name as input parameter.
     * @param name  Name of the variable that token represents.
     */
	public TokenVariable(String name) {
		this.name = name;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenVariable}.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenVariable}.
     */
	@Override
	public String asText() {
		return name;
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenVariable}
     * and has the same name as the token).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenVariable)) {
            return false;
        }
        TokenVariable other = (TokenVariable) object;
        return name.equals(other.getName());
    }


    @Override
    public void accept(ITokenVisitor visitor) {
        visitor.visitTokenVariable(this);
    }
}
