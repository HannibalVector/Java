package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

/**
 * A node representing a single for-loop construct.
 * It inherits from class {@link Node}.
 */
public class ForLoopNode extends Node {

    /** Represents the loop iterator. */
	private TokenVariable variable;

    /** Represents the start expression in a for loop. */
	private Token startExpression;

    /** Represents the end expression in a for loop.*/
	private Token endExpression;

    /** Represents the step expression in a for loop.*/
	private Token stepExpression;

    /**
     * Returns the loop iterator.
     * @return  Variable of type {@link TokenVariable} that represents the loop iterator.
     */
	public TokenVariable getVariable() {
		return variable;
	}

    /**
     * Returns the start expression.
     * @return  Variable of type {@link Token} that represents the start expression.
     */
	public Token getStartExpression() {
		return startExpression;
	}

    /**
     * Returns the end expression.
     * @return  Variable of type {@link Token} that represents the end expression.
     */
	public Token getEndExpression() {
		return endExpression;
	}

    /**
     * Returns the step expression.
     * @return  Variable of type {@link Token} that represents the step expression.
     */
	public Token getStepExpression() {
		return stepExpression;
	}

    /**
     * The most general constructor that initializes object of class {@code ForLoopNode} using all possible parameters
     * (loop variable, start, end and step expressions).
     *
     * @param variable          Parameter of type {@link TokenVariable} that represents a loop iterator.
     * @param startExpression   Parameter of type {@link Token} that represents a start expression.
     * @param endExpression     Parameter of type {@link Token} that represents an end expression.
     * @param stepExpression    Parameter of type {@link Token} that represents a step expression.
     */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression, Token stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

    /**
     * Constructor that initializes {@code ForLoopNode} using all only parameters for loop variable and start and end expressions.
     * @param variable          Parameter of type {@link TokenVariable} that represents loop iterator.
     * @param startExpression   Parameter of type {@link Token} that represents start expression.
     * @param endExpression     Parameter of type {@link Token} that represents end expression.
     */
	public ForLoopNode(TokenVariable variable, Token startExpression,
			Token endExpression) {
		this(variable, startExpression, endExpression, null);
	}

    /**
     * Checks if an object is semantically equal to the node (if an object is of type {@code ForLoopNode}
     * it has the same start expression, end expression, step expression (if they both have one),
     * and contains the same children nodes in the same order as the node).
     * Overrides method {@code equals()} from parent class {@link Node}.
     * @return  {@code true} if an object is semantically equal to the node, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        }
        if (!(object instanceof ForLoopNode)) {
            return false;
        }
        ForLoopNode other = (ForLoopNode) object;
        if (!variable.equals(other.getVariable()) ||
                !startExpression.equals(other.getStartExpression()) ||
                !endExpression.equals(other.getEndExpression())) {
            return false;
        }
        if (stepExpression == null && other.getStepExpression() == null) {
            return true;
        }
        if (stepExpression == null || other.getStepExpression() == null) {
            return false;
        }

        return stepExpression.equals(other.getStepExpression());
    }

    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitForLoopNode(this);
    }
}
