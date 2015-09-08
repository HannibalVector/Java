package hr.fer.zemris.java.tecaj.hw5.observer1;

/**
 * Observer squares value stored in subject and prints it on standard output whenever the value is changed.
 * @author ilijan
 */
public class SquareValue implements IntegerStorageObserver {
    @Override
    public void valueChanged(IntegerStorage istorage) {
        int newValue = istorage.getValue();
        System.out.println("Provided new value: " + newValue +
                ", square is " + newValue*newValue);
    }
}
