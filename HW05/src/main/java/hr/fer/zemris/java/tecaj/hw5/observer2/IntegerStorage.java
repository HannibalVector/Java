package hr.fer.zemris.java.tecaj.hw5.observer2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilijan
 */
public class IntegerStorage {
    private int value;
    private List<IntegerStorageObserver> observers;
    private List<IntegerStorageObserver> toBeRemoved;
    public IntegerStorage(int initialValue) {
        this.value = initialValue;
        observers = new ArrayList<>();
        toBeRemoved = new ArrayList<>();
    }
    public void addObserver(IntegerStorageObserver observer) {
        if(!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    public void removeObserver(IntegerStorageObserver observer) {
        toBeRemoved.add(observer);
    }

    public void clearObservers() {
        toBeRemoved.addAll(observers);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {

        int oldValue = value;

        // Only if new value is different than the current value:
        if(this.value!=value) {
            // Update current value
            this.value = value;
        }

        observers.removeAll(toBeRemoved);
        // Notify all registered observers
        if(observers!=null) {
            for(IntegerStorageObserver observer : observers) {

                observer.valueChanged(new IntegerStorageChange(this, oldValue, value));
            }
        }
    }
}
