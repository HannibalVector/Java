package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.DrawingObjectListener;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

/**
 * Vector graphics drawing.
 * @author ilijan
 */
public class Drawing implements DrawingModel, DrawingObjectListener {
    /** List of all objects. */
    private List<GeometricObject> objects = new ArrayList<>();
    /** Listeners observing change in objects. */
    private List<DrawingModelListener> listeners = new ArrayList<>();
    /** Listeners observing drawing modification state. */
    private List<DrawingModificationListener> modificationListeners = new ArrayList<>();
    /** Filename for the file in which the drawing is saved. */
    private Optional<Path> filename = Optional.empty();
    /** Modification state of the drawing. */
    private boolean isModified;

    /**
     * Adds new {@link GeometricObject} from its string representation.
     * @param string    string representation of the new {@link GeometricObject}.
     */
    public void addFromString(String string) {
        List<String> lines = Arrays.asList(string.split("\n"));

        int startingIndex = objects.size();

        for (String line : lines) {
            add(GeometricObject.fromString(line));
        }

        int endingIndex = objects.size() - 1;
        listeners.forEach(l -> l.objectsAdded(this, startingIndex, endingIndex));

        setModified(true);
    }

    /**
     * Generates string representation of the whole drawing. Used for storing drawing
     * in vector graphic file format.
     * @return  string representation of the drawing.
     */
    public String generateString() {
        StringBuilder sb = new StringBuilder();
        objects.forEach(object -> sb.append(object.generateString() + "\n"));
        return sb.toString();
    }

    /**
     * Creates rasterized {@link BufferedImage} from the vector drawing.
     * @return  rasterized image.
     */
    public BufferedImage rasterize() {
        Optional<Rectangle> boundingBox = getBoundingBox();
        if (!boundingBox.isPresent()) {
            throw new DrawingException("Empty image.");
        }
        Rectangle bounds = getBoundingBox().get();

        BufferedImage image = new BufferedImage(
                bounds.width,
                bounds.height,
                BufferedImage.TYPE_3BYTE_BGR
        );

        Graphics2D g = image.createGraphics();
        g.translate(-bounds.x, -bounds.y);
        draw(g);
        g.dispose();

        return image;
    }

    /**
     * Returns bounding box for the drawing, i.e. the smallest {@link Rectangle} containing
     * all {@link GeometricObject} objects in the drawing.
     * @return  {@link Optional<Rectangle>} which is empty if no objects are present in the image, and
     *          contains bounding rectangle otherwise.
     */
    private Optional<Rectangle> getBoundingBox() {
        if (objects.isEmpty()) return Optional.empty();

        Rectangle2D boundingBox = objects.get(0).getBoundingBox();

        for (GeometricObject object : objects) {
            boundingBox = boundingBox.createUnion(object.getBoundingBox());
        }

        return Optional.of((Rectangle)boundingBox);
    }

    /**
     * Getter for the filename in which the drawing is saved.
     * @return  {@link Optional<Path>} which is empty if no filename is set, or contains
     *          path to the file otherwise.
     */
    public Optional<Path> getFilename() {
        return filename;
    }

    /**
     * Setter for the filename.
     * @param filename  new {@link Optional<Path>}.
     */
    public void setFilename(Path filename) {
        this.filename = Optional.of(filename);
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    /**
     * Setter for the modification state of the drawing.
     * @param isModified    modification state of the drawing.
     */
    public void setModified(boolean isModified) {
        this.isModified = isModified;
        modificationListeners.forEach(l -> l.drawingModified(this));
    }

    /**
     * Clears the drawing, i.e. removes all {@link GeometricObject} objects from it.
     */
    public void clear(){
        if (!objects.isEmpty()) {

            int lastIndex = objects.size() - 1;
            objects.clear();
            listeners.forEach(l -> l.objectsRemoved(this, 0, lastIndex));
        }

        filename = Optional.empty();
        setModified(false);
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public GeometricObject getObject(int index) {
        return objects.get(index);
    }

    @Override
    public int indexOf(GeometricObject object) {
        return objects.indexOf(object);
    }

    @Override
    public void raiseObject(int index) {
        if (index == objects.size() - 1) {
            return;
        }
        Collections.swap(objects, index, index + 1);
        objectChanged(objects.get(index));
        objectChanged(objects.get(index  + 1));
    }

    @Override
    public void lowerObject(int index) {
        if (index == 0) {
            return;
        }
        Collections.swap(objects, index, index - 1);
        objectChanged(objects.get(index));
        objectChanged(objects.get(index - 1));
    }

    @Override
    public void draw(Graphics2D g) {
        objects.forEach(object -> object.draw(g));
    }

    @Override
    public void add(GeometricObject object) {
        objects.add(object);
        object.addDrawingObjectListener(this);
        int index = objects.indexOf(object);
        listeners.forEach(l -> l.objectsAdded(this, index, index));

        setModified(true);
    }

    @Override
    public void remove(GeometricObject object) {
        int index = objects.indexOf(object);
        object.removeDrawingObjectListener(this);
        objects.remove(object);
        listeners.forEach(l -> l.objectsRemoved(this, index, index));

        setModified(true);
    }

    @Override
    public void addDrawingModelListener(DrawingModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeDrawingModelListener(DrawingModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void addDrawingModificationListener(DrawingModificationListener l) {
        modificationListeners.add(l);
    }

    @Override
    public void removeDrawingModificationListener(DrawingModificationListener l) {
        modificationListeners.remove(l);
    }

    @Override
    public void objectChanged(GeometricObject source) {
        int index = objects.indexOf(source);
        listeners.forEach(l -> l.objectsChanged(this, index, index));

        setModified(true);
    }
}
