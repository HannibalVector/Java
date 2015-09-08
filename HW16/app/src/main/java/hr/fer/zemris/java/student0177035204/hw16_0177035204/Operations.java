package hr.fer.zemris.java.student0177035204.hw16_0177035204;

/**
 * Represents all available arithmetic operations.
 */
public enum Operations {
    ADD(new Operation() {
        @Override
        public int performOperation(int x, int y) {
            return x + y;
        }
    }, R.string.addition, "+"),
    SUBTRACT(new Operation() {
        @Override
        public int performOperation(int x, int y) {
            return x - y;
        }
    }, R.string.subtraction, "-"),
    MULTIPLY(new Operation() {
        @Override
        public int performOperation(int x, int y) {
            return x*y;
        }
    }, R.string.multiplication, "*"),
    DIVIDE(new Operation() {
        @Override
        public int performOperation(int x, int y) {
            return x/y;
        }
    }, R.string.division, "/");

    /** Arithmetic operation. */
    private Operation operation;
    /** ID used to fetch operation name from string resource. */
    private int nameID;
    /** Operation symbol. */
    private String symbol;

    /**
     * Costructor initializes new {@link Operations} object using provided parameters.
     * @param operation arithmetic operation.
     * @param nameID    id of string resource representing name.
     * @param symbol    operation symbol.
     */
    Operations(Operation operation, int nameID, String symbol) {
        this.operation = operation;
        this.nameID = nameID;
        this.symbol = symbol;
    }

    /**
     * Represents arithmetic operation.
     */
    public interface Operation {
        /**
         * Performs arithmetic operation using provided operands.
         * @param x the first operand.
         * @param y the second operand.
         * @return  result of the operation.
         */
        int performOperation(int x, int y);
    }

    /**
     * Gets arithmetic operation modelled by interface {@link Operation}.
     * @return arithmetic operation.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Gets id used to fetch string resource.
     * @return name id.
     */
    public int nameID() {
        return nameID;
    }

    /**
     * Gets operation symbol.
     * @return operation symbol.
     */
    public String getSymbol() {
        return symbol;
    }

}
