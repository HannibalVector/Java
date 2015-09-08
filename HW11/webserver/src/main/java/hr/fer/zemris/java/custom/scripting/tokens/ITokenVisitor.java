package hr.fer.zemris.java.custom.scripting.tokens;

import java.util.List;

/**
 * Abstract token visitor. Used for evaluation of expressions made of one or more tokens.
 * @author ilijan
 */
public interface ITokenVisitor {
    /**
     * Visitor for token representing double constant.
     * @param token token representing double constant.
     */
    void visitTokenConstantDouble(TokenConstantDouble token);

    /**
     * Visitor for token representing integer constant.
     * @param token token representing integer constant.
     */
    void visitTokenConstantInteger(TokenConstantInteger token);

    /**
     * Visitor for token representing function.
     * @param token token representing function.
     */
    void visitTokenFunction(TokenFunction token);

    /**
     * Visitor for token representing operator.
     * @param token token representing operator.
     */
    void visitTokenOperator(TokenOperator token);

    /**
     * Visitor for token representing string.
     * @param token token representing string.
     */
    void visitTokenString(TokenString token);

    /**
     * Visitor for token representing variable.
     * @param token token representing variable.
     */
    void visitTokenVariable(TokenVariable token);

    /**
     * Evaluates expression represented by {@code Iterable} of tokens.
     * @param tokens iterable object consisting of tokens.
     * @return  list of objects remaining on stack after evaluation.
     */
    List<Object> evaluateTokenExpression(Iterable<Token> tokens);
}
