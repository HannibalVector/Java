package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

/**
 * Represents {@link GeometricObject} property modifiable through graphic user interface.
 * @author ilijan
 */
public interface Property<T> {
    /**
     * Getter for the name of the property.
     * @return  name of the property.
     */
    String getName();

    /**
     * Setter for the property.
     * @param value new value for the property.
     */
    void set(T value);

    /**
     * Getter for the property.
     * @return  current value of the property.
     */
    T get();
}
