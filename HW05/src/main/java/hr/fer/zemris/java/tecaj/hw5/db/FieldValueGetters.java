package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for value GETTERS for {@link StudentRecord} in {@link StudentDatabase}.
 * @author ilijan
 */
public class FieldValueGetters {
    /** Map for accessing GETTERS by their string representation. */
    public static final Map<String, IFieldValueGetter> GETTERS = createMapOfOperators();

    /** Static initializer for the map of GETTERS. */
    private static Map<String, IFieldValueGetter> createMapOfOperators() {
        Map<String, IFieldValueGetter> result = new HashMap<>();
        result.put("jmbag", new JmbagValueGetter());
        result.put("lastName", new LastNameValueGetter());
        result.put("firstName", new FirstNameValueGetter());

        return Collections.unmodifiableMap(result);
    }

    /** General interface for GETTERS. */
    public interface IFieldValueGetter {
        public String get(StudentRecord record);
    }

    /** Getter for the JMBAG. */
    public static class JmbagValueGetter implements IFieldValueGetter {
        @Override
        public String get(StudentRecord record) {
            return record.getJmbag();
        }
    }

    /** Getter for the last name. */
    public static class LastNameValueGetter implements IFieldValueGetter {
        @Override
        public String get(StudentRecord record) {
            return record.getLastName();
        }
    }

    /** Getter for the first name. */
    public static class FirstNameValueGetter implements IFieldValueGetter {
        @Override
        public String get(StudentRecord record) {
            return record.getFirstName();
        }
    }
}
