package hr.fer.zemris.java.hw12.jvdraw;

import java.awt.*;

/**
 * Listener for the {@link ColorProvider}.
 * @author ilijan
 */
public interface ColorChangeListener {
    /**
     * Executed when source {@link ColorProvider} changes color.
     * @param source    source of the color change.
     * @param oldColor  old color.
     * @param newColor  new color.
     */
    void newColorSelected(ColorProvider source, Color oldColor, Color newColor);
}
