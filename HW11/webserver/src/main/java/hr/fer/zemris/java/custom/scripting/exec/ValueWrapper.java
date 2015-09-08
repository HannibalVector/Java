package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * Provides wrapper for values stored in {@link ObjectMultistack} stack.
 * @author ilijan
 */
public class ValueWrapper {
    /** Value stored in wrapper. */
    private Object value;

    /**
     * Constructor sets value stored in wrapper
     * to given value.
     * @param value value to be stored in wrapper.
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

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
    public Number getNumber(Object value) {
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
     * Performs given binary operation between given object and object on which
     * method is invoked.
     * @param other     given object.
     * @param operator  operator to be performed.
     * @return          result of operation in appropriate type ({@code Integer}
     *                  where possible).
     */
    public Number operate(Object other, BiFunction<Double, Double, Number> operator) {
        Number number1 = getNumber(value);
        Number number2 = getNumber(other);
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

    /**
     * Gets value stored in wrapper.
     * @return  value stored in wrapper.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value stored in wrapper.
     * @param value value to be stored in wrapper.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Increases value stored in wrapper
     * by given value.
     * @param incValue increment for stored value.
     */
    public void increment(Object incValue) {
        value = operate(incValue, (x, y) -> x + y);
    }

    /**
     * Decreases value stored in wrapper
     * by given value.
     * @param decValue decrement for stored value.
     */
    public void decrement(Object decValue) {
        value = operate(decValue, (x, y) -> x - y);
    }

    /**
     * Multiplies stored value by given value.
     * @param mulValue multiplier for stored value.
     */
    public void multiply(Object mulValue) {
        value = operate(mulValue, (x, y) -> x * y);
    }

    /**
     * Divides stored value by given value.
     * @param divValue divisor for stored value.
     */
    public void divide(Object divValue) {
        value = operate(divValue, (x, y) -> x / y);
    }

    /**
     * Compares given value with stored value.
     * @param withValue value to compare stored
     *                  value to.
     */
    public int numCompare(Object withValue) {
        return (int) operate(withValue, (x, y) -> x.compareTo(y));
    }

    @Override
    public String toString() {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }
}
