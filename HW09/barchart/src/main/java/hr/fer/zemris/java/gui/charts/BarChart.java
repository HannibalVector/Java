package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Encapsulates data needed for creation of a bar chart.
 * @author ilijan
 */
public class BarChart {

    /** List of data points. */
    private List<XYValue> data;
    /** Label for the x-axis. */
    private String xLabel;
    /** Label for the y-axis. */
    private String yLabel;
    /** Minimal y value displayed on y-axis. */
    private int yMin;
    /** Maximal y value displayed on y-axis. */
    private int yMax;
    /** Spacing between two ticks on y-axis. */
    private int tickSpacing;

    /**
     * Constructor initializes all variables in new {@link BarChart} using input parameters.
     * @param data      list of data points.
     * @param xLabel    label for the x-axis.
     * @param yLabel    label for the y-axis.
     * @param yMin      minimal y value displayed on y-axis.
     * @param yMax      maximal y value displayed on y-axis.
     * @param tickSpacing   spacing between two ticks on y-axis.
     */
    public BarChart(List<XYValue> data, String xLabel, String yLabel, int yMin, int yMax, int tickSpacing) {
        this.data = data;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.yMin = yMin;
        this.yMax = yMax;
        this.tickSpacing = tickSpacing;
    }

    /**
     * Getter for the data list.
     * @return list of data points.
     */
    public List<XYValue> getData() {
        return data;
    }

    /**
     * Getter for the x-axis label.
     * @return  x-axis label.
     */
    public String getxLabel() {
        return xLabel;
    }

    /**
     * Getter for the y-axis label.
     * @return  y-axis label.
     */
    public String getyLabel() {
        return yLabel;
    }

    /**
     * Getter for the minimal displayed y value.
     * @return  minimal displayed y value.
     */
    public int getyMin() {
        return yMin;
    }

    /**
     * Getter for the maximal displayed y value.
     * @return  maximal displayed y value.
     */
    public int getyMax() {
        return yMax;
    }

    /**
     * Getter for the tick spacing.
     * @return  y-axis tick spacing.
     */
    public int getTickSpacing() {
        return tickSpacing;
    }
}
