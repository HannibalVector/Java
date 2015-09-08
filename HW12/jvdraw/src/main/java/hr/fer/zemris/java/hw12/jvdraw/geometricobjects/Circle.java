package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implements circle.
 * @author ilijan
 */
public class Circle extends GeometricObject {
    /** Number of created circles. Used for generating generic object names. */
    private static int numberOfCircles;
    /** Center of the circle. */
    protected Point center;
    /** Radius of the circle. */
    protected int radius;
    /** Line color. */
    protected Color lineColor;
    /** Line width. */
    protected float lineWidth = 1.0f;

    /**
     * Constructor initializes new {@link Circle} using center point, radius and line color.
     * @param center    {@link Point} representing center of the circle.
     * @param radius    radius of the circle.
     * @param lineColor line color.
     */
    public Circle(Point center, int radius, Color lineColor) {
        this.center = center;
        this.radius = radius;
        this.lineColor = lineColor;
        this.type = ObjectType.CIRCLE;
        this.setName(type.toString() + " " + ++numberOfCircles);
    }

    /**
     * Getter for the center of the circle.
     * @return  {@link Point} representing center of the circle.
     */
    public Point getCenter() {
        return center;
    }

    /**
     * Setter for the center of the circle.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param center    new center of the circle.
     */
    public void setCenter(Point center) {
        this.center = center;
        fire(this);
    }

    /**
     * Getter for the radius of the circle.
     * @return  radius of the circle.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Setter for the radius of the circle.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param radius    new radius of the circle.
     */
    public void setRadius(int radius) {
        this.radius = radius;
        fire(this);
    }

    /**
     * Getter for the line color of the circle.
     * @return  line color of the circle.
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Setter for the line color of the circle.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param lineColor    new line color of the circle.
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        fire(this);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(lineColor);
        g.setStroke(new BasicStroke(lineWidth));
        g.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public void setSecondPoint(Point point) {
        int radius = (int)Math.sqrt(
                Math.pow(point.x - center.x, 2)
                + Math.pow(1.0*point.y - center.y, 2));
        setRadius(radius);
    }

    @Override
    public String generateString() {
        return type.getKeyword() + " "
                + center.x + " "
                + center.y + " "
                + radius + " "
                + lineColor.getRed() + " "
                + lineColor.getGreen() + " "
                + lineColor.getBlue();
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
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
                return Circle.this.getName();
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
    }
}
