package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * @author ilijan
 */
public class IntegerStorageChange {
    private IntegerStorage istorage;
    private int oldValue;
    private int newValue;

    public IntegerStorageChange(IntegerStorage istorage, int oldValue, int newValue) {
        this.istorage = istorage;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public IntegerStorage getIstorage() {
        return istorage;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }
}
