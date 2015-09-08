package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Token represents a function by storing its name.
 * Inherits from class {@link Token}.
 */
public class TokenFunction extends Token {

    /** Function name */
	private String name;

    /**
     * Returns the name of the function represented by {@code TokenFunction}.
     * @return  The name of the function represented by token.
     */
	public String getName() {
		return name;
	}

    /**
     * Constructor that takes function name as input parameter.
     * @param name  Name of function which token represents.
     */
	public TokenFunction(String name) {
		this.name = name;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this
     * {@code TokenFunction}.
     * Overrides method {@code asText()} from parent class {@link Token}.
     * @return  {@code String} that represents code which would get parsed to this {@code TokenFunction}.
     */
	@Override
	public String asText() {
		return name;
	}

    /**
     * Checks if an object is semantically equal to the token (if an object is of type {@code TokenFunction}
     * and has the same name as the token).
     * Overrides method {@code equals()} from parent class {@link Token}.
     * @return  {@code true} if an object is semantically equal to the token, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TokenFunction)) {
            return false;
        }
        TokenFunction other = (TokenFunction) object;
        return name.equals(other.getName());
    }


    @Override
    public void accept(ITokenVisitor visitor) {
        visitor.visitTokenFunction(this);
    }
}
