package hr.fer.zemris.java.gui.charts;

/**
 * Represents read-only data point storing x and y values.
 * @author ilijan
 */
public class XYValue {
    /** x value of the datapoint. */
    private int x;
    /** y value of the datapoint. */
    private int y;

    /**
     * Constructor initializes new datapoint using its x and y values.
     * @param x     x value of the datapoint.
     * @param y     y value of the datapoint.
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for the data point's x value.
     * @return x value of the data point.
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for the data point's y value.
     * @return y value of the datapoint.
     */
    public int getY() {
        return y;
    }
}
