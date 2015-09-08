package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * List model implementing model for list of all {@link GeometricObject} objects contained in the drawing.
 * @author ilijan
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricObject> implements DrawingModelListener{
    /** Drawing containing all objects. */
    DrawingModel drawing;

    /**
     * Constructor initializes new {@link DrawingObjectListModel} using drawing containing all objects.
     * @param drawing   drawing containing all objects.
     */
    public DrawingObjectListModel(Drawing drawing) {
        this.drawing = drawing;
        drawing.addDrawingModelListener(this);
    }

    @Override
    public int getSize() {
        return drawing.getSize();
    }

    @Override
    public GeometricObject getElementAt(int index) {
        return drawing.getObject(index);
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        for (ListDataListener l : getListDataListeners()) {
            l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1));
        }
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        for (ListDataListener l : getListDataListeners()) {
            l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1));
        }
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        for (ListDataListener l : getListDataListeners()) {
            l.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1));
        }
    }
}
