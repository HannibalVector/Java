package hr.fer.zemris.java.hw12.jvdraw;

import javax.swing.*;
import java.awt.*;

/**
 * Bar containing information about currently selected foreground and background colors.
 * @author ilijan
 */
public class JColorInfo extends JLabel implements ColorChangeListener {
    /** Current foreground color. */
    private Color foreground = JVDraw.DEFAULT_FOREGROUND;
    /** Current background color. */
    private Color background = JVDraw.DEFAULT_BACKGROUND;
    /** Foreground color provider. */
    private ColorProvider foregroundProvider;
    /** Background color provider. */
    private ColorProvider backgroundProvider;

    /**
     * Constructor initializes new {@link JColorInfo} component using foreground and background
     * color providers.
     *
     * @param foregroundProvider    foreground color provider.
     * @param backgroundProvider    background color provider.
     */
    public JColorInfo(ColorProvider foregroundProvider, ColorProvider backgroundProvider) {
        this.foregroundProvider = foregroundProvider;
        this.backgroundProvider = backgroundProvider;

        foregroundProvider.addColorChangeListener(this);
        backgroundProvider.addColorChangeListener(this);

        setColors();
    }

    @Override
    public void newColorSelected(ColorProvider source, Color oldColor, Color newColor) {
        if (source == foregroundProvider) {
            foreground = newColor;
        } else if (source == backgroundProvider) {
            background = newColor;
        }
        setColors();
    }

    /**
     * Updates label with currently selected foreground and background colors.
     */
    private void setColors() {
        setText(String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).",
                foreground.getRed(),
                foreground.getGreen(),
                foreground.getBlue(),
                background.getRed(),
                background.getGreen(),
                background.getBlue()
        ));
    }
}
