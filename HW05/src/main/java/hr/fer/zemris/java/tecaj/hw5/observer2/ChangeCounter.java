package hr.fer.zemris.java.tecaj.hw5.observer2;

/**
 * @author ilijan
 */
public class ChangeCounter implements IntegerStorageObserver {
    private int counter;
    @Override
    public void valueChanged(IntegerStorageChange istorageChangee) {
        counter++;
        System.out.println("Number of value changes since tracking: " + counter);

    }
}
