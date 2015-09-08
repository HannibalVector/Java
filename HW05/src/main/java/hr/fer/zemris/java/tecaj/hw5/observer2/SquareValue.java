package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * @author ilijan
 */
public class SquareValue implements IntegerStorageObserver {
    @Override
    public void valueChanged(IntegerStorageChange istorageChange) {
        int newValue = istorageChange.getNewValue();
        System.out.println("Provided new value: " + newValue +
                ", square is " + newValue*newValue);
    }
}
