package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.*;

/**
 * Represents simple student database.
 * @author ilijan
 */
public class StudentDatabase {

    /** Map containing student records. */
    private HashMap<String, StudentRecord> studentRecords;

    /**
     * Constructor taking string of records from which the database will be constructed.
     * @param stringRecords string of records from which the database will be constructed.
     */
    public StudentDatabase(List<String> stringRecords) {
        getRecordsFromStringList(stringRecords);
    }

    /**
     * Fills map of student records with entries.
     * @param stringRecords string of records from which the database will be constructed.
     */
    private void getRecordsFromStringList(List<String> stringRecords) {
        studentRecords = new HashMap<>();

        for (String stringRecord : stringRecords) {
            StudentRecord newRecord = StudentRecord.recordFromString(stringRecord);
            studentRecords.put(newRecord.getJmbag(), newRecord);
        }
    }

    /**
     * Gets {@code StudentRecord} for provided JMBAG (primary key).
     * @param jmbag JMBAG for which {@code StudentRecord} will be returned.
     * @return      {@code StudentRecord} for given JMBAG.
     */
    public StudentRecord forJMBAG(String jmbag) {
        return studentRecords.get(jmbag);
    }

    /**
     * Interface representing general filter for accepting {@code StudentRecord} in query.
     */
    public interface IFilter {
        boolean accepts(StudentRecord record);
    }

    /**
     * List obtained by filtering student database with provided filter.
     * @param filter    filter used for filtering through student database.
     * @return          list of type {@code StudentRecord} obtained by filtering student database with provided filter.
     */
    public List<StudentRecord> filter(IFilter filter) {
        List<StudentRecord> filteredList = new ArrayList<>();

        if (filter instanceof QueryFilter && ((QueryFilter) filter).getJMBAG().isPresent()) {
            System.out.println("Using index for record retrieval.");
            StudentRecord gotByIndex = forJMBAG(((QueryFilter) filter).getJMBAG().get());
            if(filter.accepts(gotByIndex)) {
                filteredList.add(gotByIndex);
            }
            return filteredList;
        }

        for (Map.Entry<String, StudentRecord> entry : studentRecords.entrySet()) {
            if (filter.accepts(entry.getValue())) {
                filteredList.add(entry.getValue());
            }
        }

        filteredList.sort(((o1, o2) -> o1.getJmbag().compareTo(o2.getJmbag())));

        return filteredList;
    }

    /**
     * Represents conditional expression for querying in database.
     */
    public static class ConditionalExpression {
        private FieldValueGetters.IFieldValueGetter fieldGetter;
        private String stringLiteral;
        private ComparisonOperators.IComparisonOperator comparisonOperator;

        public ConditionalExpression(FieldValueGetters.IFieldValueGetter fieldGetter,
                                     String stringLiteral, ComparisonOperators.IComparisonOperator comparisonOperator) {
            this.fieldGetter = fieldGetter;
            this.stringLiteral = stringLiteral;
            this.comparisonOperator = comparisonOperator;
        }

        public FieldValueGetters.IFieldValueGetter getFieldGetter() {
            return fieldGetter;
        }

        public String getStringLiteral() {
            return stringLiteral;
        }

        public ComparisonOperators.IComparisonOperator getComparisonOperator() {
            return comparisonOperator;
        }
    }

    /**
     * Represents special type of conditional expression if conditional expression represents query containing search
     * by primary key, so that fast retrieval by index can be used.
     */
    public static class ConditionalExpressionUsingIndex extends ConditionalExpression {
        public ConditionalExpressionUsingIndex(FieldValueGetters.IFieldValueGetter fieldGetter,
                                               String stringLiteral, ComparisonOperators.IComparisonOperator comparisonOperator) {
            super(fieldGetter, stringLiteral, comparisonOperator);
        }
    }

    /**
     * Filter for given complex query. Uses internal query list and executes them assuming they are connected by
     * logical "and" OPERATORS. If query representing search by primary key is encountered, fast retrieval by index
     * is performed.
     */
    public static class QueryFilter implements IFilter {
        private List<ConditionalExpression> queryList;
        private boolean hasConditionalExpressionUsingIndex;
        private String jmbag;

        public QueryFilter(String queryString) {
            queryList = QueryParser.getQueries(queryString);

            for (ConditionalExpression expression : queryList) {
                if (expression instanceof ConditionalExpressionUsingIndex) {
                    if (!hasConditionalExpressionUsingIndex) {
                        jmbag = expression.getStringLiteral();
                        hasConditionalExpressionUsingIndex = true;
                    } else {
                        if (jmbag != expression.getStringLiteral()) {
                            throw new IllegalArgumentException("Trying to query entries with different JMBAG.");
                        }
                    }
                }
            }
        }

        @Override
        public boolean accepts (StudentRecord record){
            for (ConditionalExpression expression : queryList) {

                if (!expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record), expression.getStringLiteral())) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Returns JMBAG of the student record, if it is available (if query representing search by primary key was encountered).
         * @return Optional of type {@code String} representing JMBAG.
         */
        public Optional<String> getJMBAG () {
            if (hasConditionalExpressionUsingIndex) {
                return Optional.of(jmbag);
            } else {
                return Optional.empty();
            }
        }
    }
}

