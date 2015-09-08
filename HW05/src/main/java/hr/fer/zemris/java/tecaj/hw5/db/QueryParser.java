package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser that parses complex queries containing multiple conditions from given string.
 * @author ilijan
 */
public class QueryParser {

    /**
     * Returns list of conditional expressions which represent complex query to be performed (conditional expressions are
     * assumed to logically be connected by conjunction OPERATORS) from given string.
     * @param queryString   string to be parsed.
     * @return              list of type {@link StudentDatabase.ConditionalExpression}.
     */
    public static List<StudentDatabase.ConditionalExpression> getQueries(String queryString){
        List<StudentDatabase.ConditionalExpression> queryList = new ArrayList<>();
        String[] tokens = queryString.split("and");
        for (String token : tokens) {
            ConditionalExpressionGetter conditionalExpressionGetter = new ConditionalExpressionGetter(token);
            queryList.add(conditionalExpressionGetter.get());
        }

        return queryList;
    }

    /**
     * Gets next {@link StudentDatabase.ConditionalExpression} from given {@code String} token.
     */
    private static class ConditionalExpressionGetter {
        /** Space character. */
        public static final String SPACE = " ";
        /** First characters of OPERATORS. */
        public static final String OPERATORS_FIRST_CHAR = "><=!";
        /** Special characters. */
        public static final String SPECIAL_CHAR = OPERATORS_FIRST_CHAR + SPACE;
        /** String delimiter. */
        public static final String STRING_DELIMITER = "\"";
        /** Wildcard symbol. */
        public static final String WILDCARD_SYMBOL = "*";
        /** Equals symbol. */
        public static final String EQUALS_SYMBOL = "=";
        /** Field name for the primary key. Used for fast retrieval from database if this field is specified. */
        public static final String PRIMARY_KEY_NAME = "jmbag";

        /** String to be parsed as {@link StudentDatabase.ConditionalExpression}. */
        private String string;
        /** Current position in string. */
        private int position;

        /**
         * Constructor that takes the string to be parsed as only parameter.
         * @param string    string to be parsed.
         */
        public ConditionalExpressionGetter(String string) {
            this.string = string;
        }

        /**
         * Checks if end of string is reached.
         * @return  {@code true} if end of string is reached, {@code false} otherwise.
         */
        private boolean isEnd() {
            return string.length() == position;
        }

        /**
         * Returns current character in string, declared as string for convenience.
         * @return  current character.
         */
        private String currentCharacter() {
            return string.substring(position, position + 1);
        }

        /**
         * Skips all spaces in string, as defined by private constant {@code spaces}.
         */
        private void skipSpaces() {
            while(!isEnd()) {
                if(currentCharacter().equals(SPACE)) {
                    position++;
                } else {
                    break;
                }
            }
        }

        /**
         * Parses {@link StudentDatabase.ConditionalExpression} from {@code ConditionalExpressionGetter}.
         * @return  {@link StudentDatabase.ConditionalExpression} parsed by {@code ConditionalExpressionGetter}.
         */
        private StudentDatabase.ConditionalExpression get() {
            // get field name
            skipSpaces();
            int startingPosition = position;
            while(!SPECIAL_CHAR.contains(currentCharacter())) {
                position++;
            }
            String fieldName = string.substring(startingPosition, position);

            // get operator
            skipSpaces();
            startingPosition = position;
            while(OPERATORS_FIRST_CHAR.contains(currentCharacter())) {
                position++;
            }
            String operator = string.substring(startingPosition, position);

            // get string literal
            while(!currentCharacter().equals(STRING_DELIMITER)) {
                position++;
            }
            startingPosition = position + 1;
            position++;
            while(!currentCharacter().equals(STRING_DELIMITER)) {
                position++;
            }
            String stringLiteral = string.substring(startingPosition, position);

            if (!FieldValueGetters.GETTERS.containsKey(fieldName)) {
                throw new IllegalArgumentException("Unsupported field name! (" + fieldName + ").");
            }
            FieldValueGetters.IFieldValueGetter fieldGetter = FieldValueGetters.GETTERS.get(fieldName);

            if (!ComparisonOperators.OPERATORS.containsKey(operator)) {
                throw new IllegalArgumentException("Unsupported operator! (" + operator + ").");
            }
            ComparisonOperators.IComparisonOperator comparisonOperator = ComparisonOperators.OPERATORS.get(operator);

            /*  If conditional expression represents query by primary key, make instance of special class
                StudentDatabase.ConditionalExpressionUsingIndex, so that fast retrieval by index can be used.
             */
            if(fieldName.equals(PRIMARY_KEY_NAME) && operator.equals(EQUALS_SYMBOL) && !stringLiteral.contains(WILDCARD_SYMBOL)) {
                return new StudentDatabase.ConditionalExpressionUsingIndex(fieldGetter, stringLiteral, comparisonOperator);
            }

            return new StudentDatabase.ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator);
        }
    }
}