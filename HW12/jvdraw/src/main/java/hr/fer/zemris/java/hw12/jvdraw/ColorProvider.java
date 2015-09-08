package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.*;

/**
 * Provides color to all subscribed {@link ColorChangeListener} listeners.
 * @author ilijan
 */
public interface ColorProvider {
    /**
     * Getter for the current color.
     * @return  current color.
     */
    Color getCurrentColor();

    /**
     * Getter for the {@link ColorProvider} identifier. Used for differing color providers
     * for listeners subscribed to more than one color provider.
     * @return  color provider identifier.
     */
    String getIdentifier();

    /**
     * Adds {@link ColorChangeListener} to the collection of listeners.
     * @param l listener to be added.
     */
    void addColorChangeListener(ColorChangeListener l);

    /**
     * Removes {@link ColorChangeListener} from the collection of listeners.
     * @param l listener to be removed.
     */
    void removeColorChangeListener(ColorChangeListener l);
}