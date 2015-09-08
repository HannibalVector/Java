package hr.fer.zemris.java.tecaj.hw5.observer1;

/**
 * Observer prints doubled value of subject, whenever the value is changed.
 * It automatically de-registers itself after the second change.
 * @author ilijan
 */
public class DoubleValue implements IntegerStorageObserver {
    private int counter;
    @Override
    public void valueChanged(IntegerStorage istorage) {
        counter++;
        System.out.println("Double value: " + istorage.getValue()*2);
        if (counter == 2) {
            istorage.removeObserver(this);
        }
    }
}
