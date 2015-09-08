package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.tokens.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Token visitor implementation.
 * @author ilijan
 */
public class ExecutorTokenVisitor implements ITokenVisitor {

    /** Temporary stack for evaluating token expressions. */
    private Stack<Object> tempStack;
    /** {@link RequestContext} for communication with client.*/
    private RequestContext requestContext;
    /** Stack for storing parameters. */
    private ObjectMultistack multistack;
    /** Map of all available functions. */
    private Map<String, Consumer<Stack<Object>>> functions;
    /** Map of all available operators. */
    private static Map<String, BiFunction<Double, Double, Number>> operators = populateOperators();

    /**
     * Populates map of operators.
     * @return  map of operators.
     */
    private static Map<String, BiFunction<Double, Double, Number>> populateOperators() {
        Map<String, BiFunction<Double, Double, Number>> operators = new HashMap<>();

        operators.put("+", (x, y) -> x + y);
        operators.put("-", (x, y) -> x - y);
        operators.put("*", (x, y) -> x * y);
        operators.put("/", (x, y) -> x / y);

        return operators;
    }

    /**
     * Constructor initializes visitor using context for communication with client and stack
     * used for storing parameters.
     * @param requestContext    used for communication with client.
     * @param multistack        used ror storing parameters.
     */
    public ExecutorTokenVisitor(RequestContext requestContext, ObjectMultistack multistack) {
        this.requestContext = requestContext;
        this.multistack = multistack;
        populateFunctionsMap();
    }

    /**
     * Populates map of functions.
     */
    private void populateFunctionsMap() {
        functions = new HashMap<>();

        functions.put("sin", stack -> {
            Number x = Util.getNumber(stack.pop());
            stack.push(Math.sin(x.doubleValue()));
        });

        functions.put("decfmt", stack -> {
            DecimalFormat f = new DecimalFormat((String)stack.pop());
            Number x = (Number) stack.pop();
            stack.push(f.format(x));
        });

        functions.put("dup", stack -> {
            Object x = stack.peek();
            stack.push(x);
        });

        functions.put("swap", stack -> {
            Object a = stack.pop();
            Object b = stack.pop();
            stack.push(a);
            stack.push(b);
        });

        functions.put("setMimeType", stack -> {
            String x = (String) stack.pop();
            requestContext.setMimeType(x);
        });

        functions.put("paramGet", stack -> getOrUseDefault(stack, requestContext::getParameter));
        functions.put("pparamGet", stack -> getOrUseDefault(stack, requestContext::getPersistentParameter));
        functions.put("tparamGet", stack -> getOrUseDefault(stack, requestContext::getTemporaryParameter));
        functions.put("pparamSet", stack -> setParameter(stack, requestContext::setPersistentParameter));
        functions.put("tparamSet", stack -> setParameter(stack, requestContext::setTemporaryParameter));
        functions.put("pparamDel", stack -> deleteParameter(stack, requestContext::removePersistentParameter));
        functions.put("tparamDel", stack -> deleteParameter(stack, requestContext::removeTemporaryParameter));
    }

    /**
     * Gets parameter or uses supplied default.
     * @param stack     stack to retrieve default parameter from.
     * @param getter    getter for getting parameter.
     */
    private void getOrUseDefault(Stack<Object> stack, Function<String, Object> getter) {
        Object defaultValue = stack.pop();
        String name = (String) stack.pop();
        Object value = getter.apply(name);
        if (value == null) {
            stack.push(defaultValue);
        } else {
            stack.push(value);
        }
    }

    /**
     * Sets parameter on value provided from stack, using given setter.
     * @param stack     stack providing value.
     * @param setter    setter for the value.
     */
    private void setParameter(Stack<Object> stack, BiConsumer<String, String> setter) {
        String name = (String) stack.pop();
        String value = String.valueOf(stack.pop());
        setter.accept(name, value);
    }

    /**
     * Deletes parameter whose name is specified on stack.
     * @param stack     stack holding name of parameter to be deleted.
     * @param deleter   function used for deleting parameter.
     */
    private void deleteParameter(Stack<Object> stack, Consumer<String> deleter) {
        String name = (String) stack.pop();
        deleter.accept(name);
    }
    
    @Override
    public void visitTokenConstantDouble(TokenConstantDouble token) {
        tempStack.push(token.getValue());
    }
    
    @Override
    public void visitTokenConstantInteger(TokenConstantInteger token) {
        tempStack.push(token.getValue());
    }
    
    @Override
    public void visitTokenFunction(TokenFunction token) {
        String functionName = token.getName();
        functionName = functionName.substring(1, functionName.length());
        functions.get(functionName).accept(tempStack);
    }
    
    @Override
    public void visitTokenOperator(TokenOperator token) {
        Object y = tempStack.pop();
        Object x = tempStack.pop();
        Number result = Util.operate(x, y, operators.get(token.getSymbol()));
        tempStack.push(result);
    }
    
    @Override
    public void visitTokenString(TokenString token) {
        tempStack.push(token.getValue());
    }
    
    @Override
    public void visitTokenVariable(TokenVariable token) {
        Object variableValue = multistack.peek(token.getName()).getValue();
        tempStack.push(variableValue);
    }

    @Override
    public List<Object> evaluateTokenExpression(Iterable<Token> tokens) {
        tempStack = new Stack<>();
        for (Token token : tokens) {
            token.accept(this);
        }

        List<Object> remainingOnStack = Collections.unmodifiableList(tempStack);
        return remainingOnStack;
    }
}
