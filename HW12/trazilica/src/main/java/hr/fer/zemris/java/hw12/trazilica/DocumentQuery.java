package hr.fer.zemris.java.hw12.trazilica;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents user query. For new query frequency vector and TF-IDF vector are
 * automatically calculated during initialization. Words that appear in query which
 * are not present in the database vocabulary are ignored.
 * @author ilijan
 */
public class DocumentQuery extends Document {

    /**
     * Constructor initializes query from input string and provided {@link Database}.
     * Constructor splits the words in input and calculates frequency and TF-IDF vectors
     * for the query, ignoring words that are not present in the database vocabulary.
     * @param query     string containing user query.
     * @param database  database used for providing vocabulary and documents for
     *                  calculating TF-IDF vector.
     */
    public DocumentQuery(String query, Database database) {
        Vocabulary vocabulary = database.getVocabulary();
        this.words = new ArrayList<>();
        List<String> queryWords = splitWords(query);
        this.words.addAll(
                queryWords.stream().filter(word -> vocabulary.containsWord(word)).collect(Collectors.toList())
        );
        this.calculateFrequencyVector(vocabulary);
        this.calculateTfIdfVector(database.getDocuments());
    }

    /**
     * Prints accepted words in the query (words that appear in the database vocabulary).
     * @return  string containing accepted words.
     */
    public String printWords() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        words.forEach(word -> sb.append(word + ", "));
        int length = sb.length();
        if (length > 1) {
            sb.replace(length - 2, length, "");
        }
        sb.append("]");
        return sb.toString();
    }
}
