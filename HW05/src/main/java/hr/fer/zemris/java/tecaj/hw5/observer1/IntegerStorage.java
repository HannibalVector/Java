package hr.fer.zemris.java.tecaj.hw5.observer1;

import java.util.ArrayList;
import java.util.List;

/**
 * Observed subject in Observer design patter example.
 * @author ilijan
 */
public class IntegerStorage {
    /** Value of the subject. */
    private int value;
    /** List of observers. */
    private List<IntegerStorageObserver> observers;
    /** List of observers to be removed. Tracked to prevent {@code ConcurrentModificationException}.*/
    private List<IntegerStorageObserver> toBeRemoved;

    /**
     * Constructor initializes subject to given value.
     * @param initialValue  value to initialize subject to.
     */
    public IntegerStorage(int initialValue) {
        this.value = initialValue;
        observers = new ArrayList<>();
        toBeRemoved = new ArrayList<>();
    }

    /**
     * Adds observer to observer list.
     * @param observer  observer to be added.
     */
    public void addObserver(IntegerStorageObserver observer) {
        if(!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes observer from observer list.
     * @param observer  observer to be removed.
     */
    public void removeObserver(IntegerStorageObserver observer) {
        toBeRemoved.add(observer);
    }

    /**
     * Clears list of observers.
     */
    public void clearObservers() {
        toBeRemoved.addAll(observers);
    }

    /**
     * Gets value stored in subject.
     * @return  value stored in subject.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value stored in subject.
     * @param value value to be stored in subject.
     */
    public void setValue(int value) {
        // Only if new value is different than the current value:
        if(this.value!=value) {
            // Update current value
            this.value = value;
        }

        observers.removeAll(toBeRemoved);
        // Notify all registered observers
        if(observers!=null) {
            for(IntegerStorageObserver observer : observers) {
                observer.valueChanged(this);
            }
        }
    }
}
