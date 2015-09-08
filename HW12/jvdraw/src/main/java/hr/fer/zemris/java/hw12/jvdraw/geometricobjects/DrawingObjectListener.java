package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;


/**
 * Defines listener which observes {@link GeometricObject} for changes.
 * @author ilijan
 */
public interface DrawingObjectListener {
    /**
     * Triggered when source {@link GeometricObject} is changed.
     * @param source    source which triggers notifications to all its listeners
     *                  when it is changed.
     */
    void objectChanged(GeometricObject source);
}
