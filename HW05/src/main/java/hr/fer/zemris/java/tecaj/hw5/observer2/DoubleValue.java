package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * @author ilijan
 */
public class DoubleValue implements IntegerStorageObserver {
    private int counter;
    @Override
    public void valueChanged(IntegerStorageChange istorageChange) {
        counter++;
        System.out.println("Double value: " + istorageChange.getNewValue()*2);
        if (counter == 2) {
            istorageChange.getIstorage().removeObserver(this);
        }
    }
}
