package hr.fer.zemris.java.tecaj.hw5.observer1;


/**
 * Observer in observer design pattern.
 * @author ilijan
 */
public interface IntegerStorageObserver  {
    /**
     * Gets executed if value of observed subject is changed.
     * @param istorage integer storage to be observed.
     */
    void valueChanged(IntegerStorage istorage);
}
