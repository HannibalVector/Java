package hr.fer.zemris.java.hw12.trazilica;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Implements the vocabulary of the collection of documents in the database.
 * @author ilijan
 */
public class Vocabulary implements Iterable<String>{
    /** Maps words to indices. Used for fast access to word index needed in populating
     * frequency and TF-IDF vectors of the document. */
    HashMap<String, Integer> vocabulary;
    /** Size of the vocabulary. Cached for easier addition of new (word, index) pair to
     * map {@link Vocabulary#vocabulary}. */
    int size;

    /**
     * Constructor initializes new Vocabulary to an empty collection.
     */
    public Vocabulary() {
        vocabulary = new HashMap<>();
    }

    /**
     * Adds word to vocabulary. Duplicate words are only stored once.
     * @param word  word to be added to vocabulary.
     */
    public void add(String word) {
        if (vocabulary.containsKey(word)) {
            return;
        }
        vocabulary.put(word, size);
        size++;
    }

    /**
     * Gets index of word in the vocabulary or -1 if word is not present in the vocabulary.
     * @param word  word for which index is returned.
     * @return      index of given word in the vocabulary.
     */
    public int indexOf(String word) {
        Integer index = vocabulary.get(word);
        if (index == null) {
            return -1;
        }
        return index;
    }

    /**
     * Checks if vocabulary contains given word.
     * @param word  word to be checked.
     * @return      {@code true} if vocabulary contains given word, {@code false} otherwise.
     */
    public boolean containsWord(String word) {
        return vocabulary.containsKey(word);
    }

    @Override
    public Iterator<String> iterator() {
        return vocabulary.keySet().iterator();
    }

    /**
     * Returns the size of the vocabulary.
     * @return  size of the vocabulary.
     */
    public int size() {
        return size;
    }

    /**
     * Adds collection of words to the vocabulary.
     * @param words     collection of words to be added.
     */
    public void addAll(Collection<String> words) {
        words.forEach(word -> add(word));
    }
}