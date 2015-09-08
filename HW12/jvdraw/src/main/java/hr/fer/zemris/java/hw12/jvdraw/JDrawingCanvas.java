package hr.fer.zemris.java.hw12.jvdraw;

import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.GeometricObject;
import hr.fer.zemris.java.hw12.jvdraw.geometricobjects.ObjectType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.SimpleTimeZone;

/**
 * Drawing canvas for drawing vector graphics.
 * @author ilijan
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener{
    /** Vector drawing. */
    private DrawingModel drawing;
    /** Type of the next {@link GeometricObject}. */
    private Optional<ObjectType> nextObjectType = Optional.empty();
    /** Next {@link GeometricObject}. */
    private Optional<GeometricObject> newObject = Optional.empty();
    /** Current foreground color. */
    private Color foreground = JVDraw.DEFAULT_FOREGROUND;
    /** Current background color. */
    private Color background = JVDraw.DEFAULT_BACKGROUND;
    /** Minimal dimension of the component. */
    private static final Dimension dimension = new Dimension(600, 600);

    /**
     * Constructor initializes new {@link JDrawingCanvas} using instance of {@link Drawing}.
     * @param drawing   instance of drawing represented in {@link JDrawingCanvas};
     */
    public JDrawingCanvas(Drawing drawing) {
        this.drawing = drawing;
        drawing.addDrawingModelListener(this);

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        setMinimumSize(dimension);
        setPreferredSize(dimension);
    }

    /**
     * Setter for the next object type.
     * @param nextObjectType    type of the next {@link GeometricObject}.
     */
    public void setNextObjectType(ObjectType nextObjectType) {
        this.nextObjectType = Optional.of(nextObjectType);
    }

    @Override
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }

    @Override
    public void paint(Graphics g) {
        drawing.draw((Graphics2D)g);
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        repaint();
    }

    /**
     * Mouse adapter implementing actions performed when mouse is clicked or moved.
     */
    private MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!nextObjectType.isPresent()) {
                JOptionPane.showMessageDialog(
                        JDrawingCanvas.this,
                        "You must select object type first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if (newObject.isPresent()) {
                newObject = Optional.empty();
            } else {
                newObject = Optional.of(
                        nextObjectType
                                .get()
                                .getInstance(e.getPoint(), foreground, background));
                drawing.add(newObject.get());
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (newObject.isPresent()) {
                newObject.get().setSecondPoint(e.getPoint());
            }
        }
    };

    @Override
    public void newColorSelected(ColorProvider source, Color oldColor, Color newColor) {
        if (source.getIdentifier().equals("foreground")) {
            foreground = newColor;
        } else if (source.getIdentifier().equals("background")) {
            background = newColor;
        }
    }
}
