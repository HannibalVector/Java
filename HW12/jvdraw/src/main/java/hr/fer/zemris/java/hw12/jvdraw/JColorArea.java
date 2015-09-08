package hr.fer.zemris.java.hw12.jvdraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Component used for picking color.
 * @author ilijan
 */
public class JColorArea extends JComponent implements ColorProvider {

    /** Selected color. */
    private Color selectedColor;
    /** Size of the color picker. */
    private static final int SIZE = 15;
    /** Dimension of the color picker. */
    private static final Dimension DIMENSION = new Dimension(SIZE, SIZE);
    /** Identifier of the {@link JColorArea} object. */
    private String name;

    /** List of {@link ColorChangeListener} listeners. */
    private List<ColorChangeListener> listeners = new ArrayList<>();

    /**
     * Constructor initializes new {@link JColorArea} using its identifier,
     * default selected color and default {@link ColorChangeListener} listener;
     * @param name          identifier for the color picker.
     * @param selectedColor default selected color.
     * @param listener      default listener.
     */
    public JColorArea(String name, Color selectedColor, ColorChangeListener listener) {
        this.selectedColor = selectedColor;
        this.name = name;
        addMouseListener(selectColor);
        addColorChangeListener(listener);
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    /**
     * Mouse adaptor defines action which is triggered on mouse-click event.
     */
    MouseAdapter selectColor = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Color oldColor = selectedColor;
            selectedColor = JColorChooser.showDialog(
                    JColorArea.this,
                    "Choose " + name + " color:", selectedColor);
            repaint();
            listeners.forEach(l -> l.newColorSelected(JColorArea.this, oldColor, selectedColor));
        }
    };

    @Override
    public void paint(Graphics g) {
        g.setColor(selectedColor);
        g.fillRect(0, 0, SIZE, SIZE);

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, SIZE - 1, SIZE - 1);
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }

    @Override
    public void addColorChangeListener(ColorChangeListener l) {
        listeners.add(l);
    }

    @Override
    public void removeColorChangeListener(ColorChangeListener l) {
        listeners.remove(l);
    }

    @Override
    public Dimension getMaximumSize() {
        return DIMENSION;
    }

    @Override
    public Dimension getPreferredSize() {
        return DIMENSION;
    }

    @Override
    public Dimension getMinimumSize() {
        return DIMENSION;
    }

}
