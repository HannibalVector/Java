package hr.fer.zemris.java.hw12.jvdraw.geometricobjects;

import java.awt.*;

/**
 * Defines all possible object types for {@link GeometricObject} as well as constants associated with them.
 * Provides factories which create instances of corresponding classes (extending {@link GeometricObject})
 * for single provided starting point of the object.
 *
 * @author ilijan
 */
public enum ObjectType {
    /** Represents circle. */
    CIRCLE(
            "Circle", "CIRCLE", "○", (point, foreground, background) -> new Circle(point, 0, foreground)),

    /** Represents filled circl.e */
    FILLED_CIRCLE(
            "Filled circle", "FCIRCLE", "●" ,(point, foreground, background) -> new FilledCircle(point, 0, foreground, background)),

    /** Represents line. */
    LINE(
            "Line", "LINE", "/", (point, foreground, background) -> new Line(point, point, foreground));

    /** Label for the object type - used for generating generic names. */
    private String label;
    /** One-point factory creates instances of classes from single starting point. */
    private OnePointFactory getter;
    /** Keyword used for parsing vector graphics files and storing drawings in files. */
    private String keyword;
    /** Symbol used to mark object type in list of objects. */
    private String symbol;

    /**
     * Constructor initializes new {@link ObjectType} using label, keyword, symbol and instance of
     * one-point factory interface {@link hr.fer.zemris.java.hw12.jvdraw.geometricobjects.ObjectType.OnePointFactory}.
     * @param label     object type label.
     * @param keyword   object type keyword.
     * @param symbol    object type symbol.
     * @param getter    one-point factory.
     */
    ObjectType(String label, String keyword, String symbol, OnePointFactory getter) {
        this.label = label;
        this.keyword = keyword;
        this.symbol = symbol;
        this.getter = getter;
    }

    @Override
    public String toString() {
        return label;
    }

    /**
     * Factory method providing instance of corresponding type.
     * @param point         point used for initializing new {@link GeometricObject}.
     * @param foreground    foreground color.
     * @param background    background color.
     * @return              new geometric object.
     */
    public GeometricObject getInstance(Point point, Color foreground, Color background) {
        return getter.getInstance(point, foreground, background);
    }

    /**
     * Getter for the keyword.
     * @return  keyword associated with object type.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Getter for the symbol.
     * @return  Symbol associated with object type.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Interface specifying one-point factory methods used for getting new instances
     * of corresponding type from single starting point.
     */
    private interface OnePointFactory {
        GeometricObject getInstance(Point point, Color foreground, Color background);
    }
}
