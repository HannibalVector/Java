package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implements line.
 * @author ilijan
 */
public class Line extends GeometricObject {
    /** Number of created lines. Used for generating generic object names. */
    private static int numberOfLines;
    /** Starting point of the line. */
    private Point start;
    /** Ending point of the line. */
    private Point end;
    /** Line color. */
    private Color lineColor;
    /** Line width. */
    private float lineWidth = 1.0f;

    /**
     * Constructor initializes new {@link Line} using starting and ending point
     * and line color.
     * @param start     {@link Point} representing starting point of the line.
     * @param end       {@link Point} representing ending point of the line.
     * @param lineColor line color.
     */
    public Line(Point start, Point end, Color lineColor) {
        this.start = start;
        this.end = end;
        this.lineColor = lineColor;
        this.type = ObjectType.LINE;
        this.setName(type.toString() + " " + + ++numberOfLines);
    }

    /**
     * Getter for the starting point of the line.
     * @return  starting point of the line.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Setter for the starting point of the line.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param start    new starting point of the line.
     */
    public void setStart(Point start) {
        this.start = start;
        fire(this);
    }

    /**
     * Getter for the ending point of the line.
     * @return  ending point of the line.
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Setter for the ending point of the line.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param end    new ending point of the line.
     */
    public void setEnd(Point end) {
        this.end = end;
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
     * Setter for the line color.
     * Triggers notification to all subscribed {@link DrawingObjectListener} listeners.
     * @param lineColor    new line color.
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
        fire(this);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(lineColor);
        g.setStroke(new BasicStroke(lineWidth));
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    @Override
    public void setSecondPoint(Point point) {
        setEnd(point);
    }

    @Override
    public String generateString() {
        return type.getKeyword() + " "
                + start.x + " "
                + start.y + " "
                + end.x + " "
                + end.y + " "
                + lineColor.getRed() + " "
                + lineColor.getGreen() + " "
                + lineColor.getBlue();
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                Math.min(start.x, end.x),
                Math.min(start.y, end.y),
                Math.max(start.x, end.x),
                Math.max(start.y, end.y)
        );
    }

    @Override
    protected void populateProperties() {
        properties = new ArrayList<>();
        properties.add(new Property<Point>() {
            @Override
            public String getName() {
                return "Starting point";
            }

            @Override
            public void set(Point value) {
                setStart(value);
            }

            @Override
            public Point get() {
                return getStart();
            }
        });

        properties.add(new Property<Point>() {
            @Override
            public String getName() {
                return "Ending point";
            }

            @Override
            public void set(Point value) {
                setEnd(value);
            }

            @Override
            public Point get() {
                return getEnd();
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
                return Line.this.getName();
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
