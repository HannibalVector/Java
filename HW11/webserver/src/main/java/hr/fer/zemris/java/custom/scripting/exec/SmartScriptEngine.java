package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Implements engine for execution of commands written in SmartScript scripting language.
 * @author ilijan
 */
public class SmartScriptEngine {
    /** Node representing whole document. */
    private DocumentNode documentNode;
    /** Request context used for communication with client. */
    private RequestContext requestContext;
    /** Stack for storing parameters. */
    private ObjectMultistack multistack = new ObjectMultistack();
    /** Counter for opened for loops. */
    private int openedForLoops;
    /** Implementation of node visitor. */
    private INodeVisitor visitor;

    /**
     * Constructor takes document node and {@link RequestContext} for communication with client.
     * @param documentNode      document to execute.
     * @param requestContext    used to communicate with client.
     */
    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
        initializeVisitor();
    }

    /**
     * Executes document.
     */
    public void execute() {
        documentNode.accept(visitor);
    }

    /**
     * Initializes node visitor.
     */
    private void initializeVisitor() {
        visitor = new INodeVisitor() {
            ExecutorTokenVisitor tokenVisitor = new ExecutorTokenVisitor(requestContext, multistack);

            @Override
            public void visitTextNode(TextNode node) {
                try {
                    requestContext.write(node.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitForLoopNode(ForLoopNode node) {
                List<Object> evaluatedStartExpression = tokenVisitor.evaluateTokenExpression(
                        Arrays.asList(node.getStartExpression()));
                List<Object> evaluatedEndExpression = tokenVisitor.evaluateTokenExpression(
                        Arrays.asList(node.getEndExpression()));

                List<Object> evaluatedStepExpression = null;
                if (node.getStepExpression() != null) {
                    evaluatedStepExpression = tokenVisitor.evaluateTokenExpression(
                            Arrays.asList(node.getStepExpression()));
                }

                if (evaluatedStartExpression.size() != 1 || evaluatedEndExpression.size() != 1
                        || evaluatedStepExpression != null && evaluatedStepExpression.size() != 1) {
                    throw new IllegalArgumentException("Invalid for loop format.");
                }

                Object start = evaluatedStartExpression.get(0);
                Object end = evaluatedEndExpression.get(0);
                Object step;
                if (evaluatedStepExpression != null) {
                    step = evaluatedStepExpression.get(0);
                } else {
                    step = new Integer(1);
                }

                openedForLoops++;

                String varId = node.getVariable().getName();
                String endId = "forLoopEnd" + openedForLoops;
                String stepId = "forLoopStep" + openedForLoops;

                multistack.push(varId, new ValueWrapper(start));
                multistack.push(endId, new ValueWrapper(end));
                multistack.push(stepId, new ValueWrapper(step));

                while (multistack.peek(varId).numCompare(multistack.peek(endId).getValue()) < 0) {
                    visitNodesGroup(node);

                    multistack.peek(varId).increment(multistack.peek(stepId).getValue());
                }

                multistack.pop(varId);
                multistack.pop(endId);
                multistack.pop(stepId);

                openedForLoops--;
            }

            @Override
            public void visitEchoNode(EchoNode node) {

                List<Object> remainingOnStack =
                        tokenVisitor.evaluateTokenExpression(Arrays.asList(node.getTokens()));

                for (Object obj : remainingOnStack) {
                    try {
                        requestContext.write(obj.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void visitDocumentNode(DocumentNode node) {
                visitNodesGroup(node);
            }
        };
    }
}
