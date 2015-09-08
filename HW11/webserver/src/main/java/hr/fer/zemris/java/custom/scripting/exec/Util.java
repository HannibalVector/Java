package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * @author ilijan
 */
public class Util {
    /**
     * Gets number from general {@code Object} stored in value, when
     * numerical operations are attempted on the wrapper. {@code Integer}
     * should be passed where possible, otherwise type {@code Double} is
     * assumed.
     * Throws appropriate exception if conversion is unsuccessful.
     * @param value value to be transformed to number.
     * @return {@code Number} stored as value.
     * @throws RuntimeException if stored value cannot be converted to number.
     */
    public static Number getNumber(Object value) {
        if (value instanceof Integer || value instanceof Double) {
            return (Number)value;
        }
        if (value == null) {
            return 0;
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (Exception e1) {
                try {
                    return Double.parseDouble((String) value);
                } catch (Exception e2) {
                    // do nothing
                }
            }
        }
        throw new RuntimeException("Supplied type is not supported.");
    }

    /**
     * Performs given binary operation between given objects.
     *
     * @param first     first operand.
     * @param second    second operand.
     * @param operator  operation to be performed.
     * @return          result of operation.
     */
    public static Number operate(Object first, Object second, BiFunction<Double, Double, Number> operator) {
        Number number1 = getNumber(first);
        Number number2 = getNumber(second);
        Double value1 = number1.doubleValue();
        Double value2 = number2.doubleValue();
        Number result =  operator.apply(value1, value2);
        if (result instanceof Double
                && number1 instanceof Integer
                && number2 instanceof Integer) {
            return result.intValue();
        }
        return result;
    }
}
