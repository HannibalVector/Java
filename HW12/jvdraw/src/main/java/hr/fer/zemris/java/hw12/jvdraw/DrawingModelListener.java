package hr.fer.zemris.java.hw12.jvdraw;


/**
 * Listens for changes in drawing to which it is subscribed.
 * @author ilijan
 */
public interface DrawingModelListener {
    /**
     * Executed when objects are added to the drawing.
     * @param source    drawing to which objects are added.
     * @param index0    starting index for the collection of added objects.
     * @param index1    ending index (included) for the collection of added objects.
     */
    void objectsAdded(DrawingModel source, int index0, int index1);

    /**
     * Executed when objects are removed from the drawing.
     * @param source    drawing from which objects are removed.
     * @param index0    starting index for the collection of removed objects.
     * @param index1    ending index (included) for the collection of removed objects.
     */
    void objectsRemoved(DrawingModel source, int index0, int index1);

    /**
     * Executed when objects in the drawing are changed.
     * @param source    drawing in which the objects are changed.
     * @param index0    starting index for the collection of changed objects.
     * @param index1    ending index (included) for the collection of changed objects.
     */
    void objectsChanged(DrawingModel source, int index0, int index1);
}
