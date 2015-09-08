package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implements filled circle.
 * @author ilijan
 */
public class FilledCircle extends Circle {
    /** Number of created filled circles. Used for generating generic object names. */
    private static int numberOfFilledCircles;
    /** Fill color. */
    private Color fillColor;

    /**
     * Constructor initializes new {@link FilledCircle} using center point, radius, line color
     * and fill color.
     * @param center    {@link Point} representing center of the circle.
     * @param radius    radius of the circle.
     * @param lineColor line color.
     * @param fillColor fill color.
     */
    public FilledCircle(Point center, int radius, Color lineColor, Color fillColor) {
        super(center, radius, lineColor);
        this.fillColor = fillColor;

        this.type = ObjectType.FILLED_CIRCLE;
        this.setName(type.toString() + " " + ++numberOfFilledCircles);
    }

    /**
     * Getter for the fill color of the circle.
     * @return  fill color of the circle.
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Setter for the fill color of the circle.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param fillColor    new fill color of the circle.
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        fire(this);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(fillColor);
        g.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);

        g.setColor(lineColor);
        g.setStroke(new BasicStroke(lineWidth));
        g.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public String generateString() {
        return type.getKeyword() + " "
                + center.x + " "
                + center.y + " "
                + radius + " "
                + lineColor.getRed() + " "
                + lineColor.getGreen() + " "
                + lineColor.getBlue() + " "
                + fillColor.getRed() + " "
                + fillColor.getGreen() + " "
                + fillColor.getBlue();
    }

    @Override
    protected void populateProperties() {
        properties = new ArrayList<>();
        properties.add(new Property<Integer>() {
            @Override
            public String getName() {
                return "Radius";
            }

            @Override
            public void set(Integer value) {
                setRadius(value);
            }

            @Override
            public Integer get() {
                return getRadius();
            }
        });

        properties.add(new Property<Point>() {
            @Override
            public String getName() {
                return "Center";
            }

            @Override
            public void set(Point value) {
                setCenter(value);
            }

            @Override
            public Point get() {
                return getCenter();
            }
        });

        properties.add(new Property<String>() {
            @Override
            public String getName() {
                return "Name";
            }

            @Override
            public void set(String value) {
                setName(value);
            }

            @Override
            public String get() {
                return FilledCircle.this.getName();
            }
        });

        properties.add(new Property<Color>() {
            @Override
            public String getName() {
                return "Line color";
            }

            @Override
            public void set(Color value) {
                setLineColor(value);
            }

            @Override
            public Color get() {
                return getLineColor();
            }
        });

        properties.add(new Property<Color>() {
            @Override
            public String getName() {
                return "Fill color";
            }

            @Override
            public void set(Color value) {
                setFillColor(value);
            }

            @Override
            public Color get() {
                return getFillColor();
            }
        });
    }
}
