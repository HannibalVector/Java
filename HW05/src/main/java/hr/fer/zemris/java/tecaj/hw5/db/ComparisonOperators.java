package hr.fer.zemris.java.tecaj.hw5.db;

import java.text.Collator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class wraps all comparison OPERATORS used to compare two strings. Operators can be accessed through {@code IComparisonOperator} interface,
 * or can be get using their string representation stored in map.
 * @author ilijan
 */
public class ComparisonOperators {
    /** Map for accessing OPERATORS by their string representation. */
    public static final Map<String, IComparisonOperator> OPERATORS = createMapOfOperators();

    /** Collator used for proper comparison of strings containing Croatian letters. */
    private static final Collator CROATIAN_COLLATOR = Collator.getInstance(new Locale("hr", "HR"));

    /** Static initializer for the map of OPERATORS. */
    private static Map<String, IComparisonOperator> createMapOfOperators() {
        Map<String, IComparisonOperator> result = new HashMap<>();
        result.put("=", new WildCardEqualsCondition());
        result.put(">", new GreaterThanCondition());
        result.put("<", new LessThanThanCondition());
        result.put(">=", new GreaterThanOrEqualsCondition());
        result.put("<=", new LessThanThanOrEqualsCondition());
        result.put("!=", new NotEqualCondition());

        return Collections.unmodifiableMap(result);
    }

    /** General interface for operator used to compare two strings. */
    public interface IComparisonOperator {
        boolean satisfied(String value1, String value2);
    }

    /**
     * Operator compares if two strings are equal and supports use of wildcard character (*).
     * Wildcard character can be used anywhere in the second string, but it should appear at most once.
     */
    public static class WildCardEqualsCondition implements IComparisonOperator {
        /** Wildcard symbol. */
        public static final String WILDCARD_SYMBOL = "*";

        @Override
        public boolean satisfied(String value1, String value2) {
            if (wildcardOccurrences(value2) == 0) {
                return value1.equals(value2);
            } else {
                if (value2.startsWith(WILDCARD_SYMBOL)) {
                    return value1.endsWith(value2.substring(1));
                } else if (value2.endsWith(WILDCARD_SYMBOL)) {
                    return value1.startsWith(value2.substring(0, value2.length() - 1));
                } else {
                    String[] beginningAndEnd = value2.split("\\"+WILDCARD_SYMBOL);
                    return value1.startsWith(beginningAndEnd[0]) && value1.endsWith(beginningAndEnd[1]);
                }
            }
        }

        /**
         * Method counts number of wildcard occurrences in given string. If number of occurrences is greater than one
         * appropriate exception is thrown.
         * @param inputString   string to be checked for number of wildcard occurrences.
         * @return              number of occurrences of wildcard character.
         * @throws IllegalArgumentException if number of occurences is greater than 1.
         */
        public int wildcardOccurrences(String inputString) {
            int wildCardOccurrences = inputString.length() - inputString.replace(WILDCARD_SYMBOL, "").length();
            if (wildCardOccurrences > 1) {
                throw new IllegalArgumentException("Invalid number of wildcard characters (" + wildCardOccurrences + ").");
            }
            return wildCardOccurrences;
        }
    }

    /**
     * Checks if the first string is lexicographically greater than the second.
     */
    public static class GreaterThanCondition implements IComparisonOperator {
        @Override
        public boolean satisfied(String value1, String value2) {
            return CROATIAN_COLLATOR.compare(value1, value2) > 0;
        }
    }

    /**
     * Checks if the first string is lexicographically smaller than the second.
     */
    public static class LessThanThanCondition implements IComparisonOperator {
        @Override
        public boolean satisfied(String value1, String value2) {
            return CROATIAN_COLLATOR.compare(value1, value2) < 0;
        }
    }

    /**
     * Checks if the first string is lexicographically greater than or equal to the second.
     */
    public static class GreaterThanOrEqualsCondition implements IComparisonOperator {
        @Override
        public boolean satisfied(String value1, String value2) {
            return CROATIAN_COLLATOR.compare(value1, value2) >= 0;
        }
    }

    /**
     * Checks if the first string is lexicographically smaller than or equal to the second.
     */
    public static class LessThanThanOrEqualsCondition implements IComparisonOperator {
        @Override
        public boolean satisfied(String value1, String value2) {
            return CROATIAN_COLLATOR.compare(value1, value2) <= 0;
        }
    }

    /**
     * Checks if the first string is not equal to the second.
     */
    public static class NotEqualCondition implements IComparisonOperator {
        @Override
        public boolean satisfied(String value1, String value2) {
            return !value1.equals(value2);
        }
    }
}
