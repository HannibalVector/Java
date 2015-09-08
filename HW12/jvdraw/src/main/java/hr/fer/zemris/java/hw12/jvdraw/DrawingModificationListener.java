package hr.fer.zemris.java.hw12.jvdraw;

/**
 * Listener which is notified when state of modification of the drawing, to which it is subscribed,
 * is modified.
 *
 * @author ilijan
 */
public interface DrawingModificationListener {
        /**
         * Executed when modification state of the drawing is changed.
         * @param source        drawing whose modification state is monitored.
         */
        void drawingModified(Drawing source);
}
