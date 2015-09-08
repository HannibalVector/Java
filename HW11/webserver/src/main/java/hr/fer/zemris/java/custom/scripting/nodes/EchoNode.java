package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;

/**
 * Node representing a command which generates some textual output dynamically.
 * It inherits from class {@link Node}.
 */
public class EchoNode extends Node {

    /** Array of tokens that EchoNode uses to dynamically generate textual output. */
	private Token[] tokens;

    /**
     * Returns tokens that EchoNode uses to dynamically generate textual output.
     * @return  Array of type {@link Token} that EchoNode uses to dynamically generate textual output.
     */
	public Token[] getTokens() {
		return tokens;
	}

    /**
     * Constructor that initializes object of class {@code EchoNode} using array of tokens.
     * @param tokens    Array of type {@link Token} that EchoNode uses to dynamically generate textual output.
     */
	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}

    /**
     * Checks if an object is semantically equal to the node (if an object is of type {@code EchoNode}
     * and contains the same tokens in the same order as the node).
     * Overrides method {@code equals()} from parent class {@link Node}.
     * @return  {@code true} if an object is semantically equal to the node, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EchoNode)) {
            return false;
        }
        EchoNode other = (EchoNode) object;
        Token[] otherTokens = other.getTokens();
        if(tokens==null && otherTokens==null) {
            return true;
        }
        if(tokens==null || otherTokens == null) {
            return false;
        }

        if(tokens.length != otherTokens.length) {
            return false;
        }

        for (int i = 0; i < tokens.length; i++) {
            if(!tokens[i].equals(otherTokens[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitEchoNode(this);
    }
}
