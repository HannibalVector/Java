package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;

import java.awt.*;

/**
 * Represents model for vector drawings.
 * @author ilijan
 */
public interface DrawingModel {
    /**
     * Getter for the size of the drawing, i.e. number of {@link GeometricObject} objects
     * in the drawing.
     * @return  size of the drawing.
     */
    int getSize();

    /**
     * Getter for the object at specified index.
     * @param index index of the wanted object.
     * @return      object at the specified index.
     */
    GeometricObject getObject(int index);

    /**
     * Getter for the index of the specified object.
     * @param object    object for which index is asked.
     * @return          index of the given object.
     */
    int indexOf(GeometricObject object);

    /**
     * Raises object's z-index by one.
     * @param index index of the object to be raised.
     */
    void raiseObject(int index);

    /**
     * Lowers object's z-index by one.
     * @param index index of the object to be lowered.
     */
    void lowerObject(int index);

    /**
     * Draws drawing using supplied {@link Graphics2D} object.
     * @param g object used for rendering graphics.
     */
    void draw(Graphics2D g);

    /**
     * Adds {@link GeometricObject} to the drawing.
     * @param object    object to be added.
     */
    void add(GeometricObject object);

    /**
     * Removes {@link GeometricObject} to the drawing.
     * @param object    object to be removed.
     */
    void remove(GeometricObject object);

    /**
     * Checks if drawing was modified.
     * @return  {@code true} if drawing was modified, {@code false} otherwise.
     */
    boolean isModified();

    /**
     * Adds {@link DrawingModelListener} to the collection of listeners.
     * @param l listener to be added.
     */
    void addDrawingModelListener(DrawingModelListener l);
    /**
     * Removes {@link DrawingModelListener} from the collection of listeners.
     * @param l listener to be removed.
     */
    void removeDrawingModelListener(DrawingModelListener l);

    /**
     * Adds {@link DrawingModificationListener} to the collection of modification listeners.
     * @param l listener to be added.
     */
    void addDrawingModificationListener(DrawingModificationListener l);

    /**
     * Removes {@link DrawingModificationListener} from the collection of modification listeners.
     * @param l listener to be removed.
     */
    void removeDrawingModificationListener(DrawingModificationListener l);
}
