package hr.fer.zemris.java.tecaj.hw5.observer1;

import com.sun.org.apache.xpath.internal.SourceTree;

/**
 *  Observer tracks number of changes in subject since it started observing it
 *  and prints it on standard output whenever the value is changed.
 * @author ilijan
 */
public class ChangeCounter implements IntegerStorageObserver {
    private int counter;
    @Override
    public void valueChanged(IntegerStorage istorage) {
        counter++;
        System.out.println("Number of value changes since tracking: " + counter);

    }
}
