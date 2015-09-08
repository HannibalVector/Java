package hr.fer.zemris.java.tecaj.hw3;

import java.util.Iterator;

/**
 * Class represents a collection of integers which allows user to loop from specified integer
 * to specified integer with given step.
 * Created by ilijan on 4/1/15.
 */
public class IntegerSequence implements Iterable<Integer> {

    /** Starting integer in the collection. */
    private int start;

    /** Ending integer in the collection. */
    private int end;

    /** Step. */
    private int step;

    /**
     * Constructor initializes new collection of integers using starting and ending integer, and step.
     * @param start     starting integer of the collection.
     * @param end       ending integer of the collection.
     * @param step      step of the collection.
     */
    public IntegerSequence(int start, int end, int step) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Starting and ending integer must be non-negative.");
        }
        if (step < 1) {
            throw new IllegalArgumentException("Step must be positive integer.");
        }
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntegerIterator();
    }

    /**
     * Private class is used to create iterators for the iterator() method in
     * Iterable<Integer> interface which outer class implements.
     */
    private class IntegerIterator implements Iterator<Integer> {

        /** Current number in iterator. */
        private int currentNumber;

        /**
         * Default constructor initializes iterator to starting number of the collection.
         */
        private IntegerIterator() {
            currentNumber = start;
        }

        @Override
        public boolean hasNext() {
            return currentNumber <= end;
        }

        @Override
        public Integer next() {
            if(!hasNext()) {
                throw new IntegerSequenceException("No more integers left to iterate over.");
            }
            int nextNumber = currentNumber;
            currentNumber += step;
            return nextNumber;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove integer.");
        }
    }

}
