package hr.fer.zemris.java.hw12.trazilica;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents single document to be stored in the database.
 * @author ilijan
 */
public class Document {
    /** Path to the document. */
    private File file;
    /** List of all non-stopping words in the document. */
    protected List<String> words;
    /** Frequency vector for the document. Each component represents number of occurrences of corresponding
     * word in the {@link Document#vocabulary}. */
    private int[] frequencyVector;
    /** TF-IDF vector for the document. */
    protected double[] tfIdfVector;
    /** Vocabulary of the collection of all documents in the database.
     * Gives meaning to the components of {@link Document#frequencyVector} and {@link Document#tfIdfVector}.
     * Frequency of given word in a vocabulary is stored at index provided by method
     * {@link Vocabulary#indexOf(String)}. */
    private Vocabulary vocabulary;

    /**
     * Constructor initializes new {@link Document} using path to file and the set of stopwords.
     * @param file          path to file.
     * @param stopwords     set of stopwords.
     */
    public Document(File file, Set<String> stopwords) {
        try {
            String fileString = new String(Files.readAllBytes(file.toPath()));

            this.words = new ArrayList<>();
            this.words.addAll(
                    splitWords(fileString).stream().filter(word -> !stopwords.contains(word)).collect(Collectors.toList())
            );

            this.file = file;
        } catch (IOException ex) {
            System.out.println("File " + file + " not readable.");
        }
    }

    /**
     * Splits string representing document to list of words. Any number of successive non-letter characters
     * is used for splitting words. Words are transformed to lower case for the sake of uniqueness.
     * @param document  string representing document.
     * @return          list of words contained in document.
     */
    protected static List<String> splitWords(String document) {
        String[] words = document.trim().split("[^a-zA-ZšđčćžŠĐČĆŽ]+");

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toLowerCase();
        }

        return Arrays.asList(words);
    }

    /**
     * Default constructor does nothing. Required to enable definition of constructors with different signature in
     * children classes.
     */
    protected Document() {}

    /**
     * Getter for the list of all words contained in the document.
     * @return  list of words contained in the document.
     */
    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    /**
     * Calculates frequency vector for the document using provided vocabulary. Document vocabulary is set
     * to provided vocabulary which is also later used for calculating TF-IDF vector.
     * @param vocabulary    vocabulary to be used when calculating frequency vector and IF-IDF vector.
     */
    public void calculateFrequencyVector(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
        frequencyVector = new int[vocabulary.size()];

        for (String word : words) {
            int indexInVocabulary = vocabulary.indexOf(word);
            if (indexInVocabulary != -1) {
                frequencyVector[indexInVocabulary]++;
            }
        }
    }

    /**
     * Calculates TF-IDF vector using vocabulary stored in {@link Document#vocabulary} and provided list
     * of documents.
     * @param documents     list of documents used for calculating TF-IDF vector.
     */
    public void calculateTfIdfVector(List<Document> documents){
        if (frequencyVector == null) {
            throw new UnsupportedOperationException("Frequency vector must be calculated first.");
        }

        tfIdfVector = new double[vocabulary.size()];
        int numberOfDocs = documents.size();

        for (String word : words) {
            int ind = vocabulary.indexOf(word);
            if (ind != -1) {
                int docsContainingWord = 0;
                for (Document document : documents) {
                    docsContainingWord += document.getTermFrequency(word);
                }
                tfIdfVector[ind] = getTermFrequency(word) * Math.log(((double)numberOfDocs)/docsContainingWord);
            }
        }
    }

    /**
     * Gets frequency (number of occurrences) of the given word in the document.
     * @param word  word which frequency is returned.
     * @return      frequency of the given word.
     */
    public int getTermFrequency(String word) {
        if (frequencyVector == null) {
            throw new UnsupportedOperationException("Frequency vector must be calculated first.");
        }

        return frequencyVector[vocabulary.indexOf(word)];
    }

    /**
     * Calculates similarity of given query to the document. Similarity is given in range [-1, 1], where
     * bigger number means greater similarity.
     * @param query     object representing user query.
     * @return          number which measures similarity of query to the document.
     */
    public double calculateSimilarity(DocumentQuery query) {
        if (tfIdfVector == null) {
            throw new UnsupportedOperationException("TF-IDF vector must be calculated first.");
        }

        if (query.words.size() == 0) {
            return 0;
        }
        return VectorUtil.cosine(this.tfIdfVector, query.tfIdfVector);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (double num : tfIdfVector) {
            sb.append(String.format("%.2f, ", num));
        }
        int length = sb.length();
        if (length > 1) {
            sb.replace(length - 2, length, "");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Getter for the path of the document.
     * @return  path of the document.
     */
    public Path getPath() {
        return file.toPath();
    }
}
