package hr.fer.zemris.java.hw12.trazilica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Implements database for collection of textual documents which is then being queried for expressions input by user.
 * @author ilijan
 */
public class Database {
    /** Vocabulary of the collection of documents. */
    private Vocabulary vocabulary;
    /** Stopwords for language in which documents are written. */
    private Set<String> stopwords;
    /** List of all documents in the collection. */
    private List<Document> documents;
    /** Path to file containing definitions of stopwords. */
    private static final String STOPWORDS = "stopwords_hr.txt";
    /** Maximal number of results to be shown when user queries database. */
    public static final int RESULTS_THRESHOLD = 10;

    /**
     * Constructor takes path to directory containing all textual files to be analyzed.
     * @param directory     path to directory containing collection of textual files to be analyzed.
     */
    public Database(File directory) {
        loadStopwords();
        addDocuments(directory);
        createVocabulary();
        calculateFrequencyVectors();
        calculateTfIdfVector();
    }

    /**
     * Calculates TF-IDF vector for all documents in the database.
     */
    private void calculateTfIdfVector() {
        documents.forEach(document -> document.calculateTfIdfVector(documents));
    }

    /**
     * Calculates frequency vector for all documents in the databse.
     */
    private void calculateFrequencyVectors() {
        documents.forEach(document -> document.calculateFrequencyVector(vocabulary));
    }

    /**
     * Loads stopwords in the database. All non-letter characters are ignored and used
     * for splitting words in input file.
     */
    private void loadStopwords() {
        try {
            String stopwordsString = new String(Files.readAllBytes(Paths.get(STOPWORDS)));

            stopwords = new HashSet<>();
            stopwords.addAll(Document.splitWords(stopwordsString));

        } catch (IOException ex) {
            System.out.println("File defining stopwords not readable.");
        }
    }

    /**
     * Adds documents in specified directory to the database.
     * @param directory     path to directory containing the files to be added.
     */
    private void addDocuments(File directory) {
        documents = new ArrayList<>();
        for (File file : directory.listFiles()) {
            documents.add(new Document(file, stopwords));
        }
    }

    /**
     * Creates vocabulary from collection of documents. Frequency vectors for all the documents
     * have to be calculated prior to calling this method.
     */
    private void createVocabulary() {
        vocabulary = new Vocabulary();
        documents.forEach(document -> vocabulary.addAll(document.getWords()));
    }

    /**
     * Gets list of most similar documents (embedded in class {@link QueryResult}) to given query
     * which is represented by class {@link DocumentQuery}.
     * @param query     query embedding expression for which the most similar documents are searched.
     * @return          list of the most similar documents to given query.
     */
    public List<QueryResult> getMostSimilar(DocumentQuery query) {
        documents.sort(
                (doc1, doc2) -> -1*Double.compare(doc1.calculateSimilarity(query), doc2.calculateSimilarity(query))
        );

        List<QueryResult> results = new ArrayList<>();

        for (Document document : documents) {
            double similarity = document.calculateSimilarity(query);
            if (Double.compare(similarity, 0) == 0 || results.size() == RESULTS_THRESHOLD) {
                break;
            }
            results.add(new QueryResult(document, similarity));
        }

        return results;
    }

    /**
     * Getter for the vocabulary of the collection of documents.
     * @return  vocabulary represented by class {@link Vocabulary}.
     */
    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    /**
     * Getter for the collection of documents in the database.
     * @return  list of all documents in the database.
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * Class represents single result of database query. {@code QueryResults} encapsulates path to the document
     * and measure of similarity of given document to the query.
     */
    public static class QueryResult {
        /** Path to resulting document. */
        public Path documentPath;
        /** Similarity to given query. */
        public double similarity;

        /**
         * Constructor initializes all member variables through parameters.
         * @param document      path to resulting document.
         * @param similarity    similarity to given query.
         */
        public QueryResult(Document document, double similarity) {
            this.documentPath = document.getPath();
            this.similarity = similarity;
        }
    }
}
