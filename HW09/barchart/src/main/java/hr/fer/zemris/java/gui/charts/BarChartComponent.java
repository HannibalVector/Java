package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;

/**
 * Component displaying bar chart.
 * @author ilijan
 */
public class BarChartComponent extends JComponent {
    /** Space between last tick on axis and ending arrow. */
    private static final int SPACE_TO_ARROW = 10;
    /** Space between screen edge and axis label or between axis and tick labels. */
    private static final int SPACE_TO_LABEL = 10;
    /** Space between screen edge and end of axis. */
    private static final int SPACE_TO_AXIS_END = 30;
    /** Size of ticks. */
    private static final int TICK_SIZE = 6;

    /** Data used for drawing chart. */
    private BarChart chart;

    /** Number of ticks on the x-axis. */
    private int numberOfXTicks;
    /** Number of ticks on the y-axis. */
    private int numberOfYTicks;
    /** Spacing between ticks on the x-axis. */
    private int xTickSpacing;
    /** Spacing between ticks on the y-axis. */
    private int yTickSpacing;
    /** Origin of the coordinate system. */
    private Point origin;

    /**
     * Constructor initializes bar chart component using {@link BarChart} object which encapsulates all data
     * needed for drawing bar chart.
     * @param chart {@link BarChart} object.
     */
    public BarChartComponent(BarChart chart) {
        this.chart = chart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();

        origin = getOrigin(g2d);
        drawXAxis(g2d);
        drawYAxis(g2d);

        drawXLabel(g2d);
        drawYLabel(g2d);

        drawGrid(g2d);
        drawBars(g2d);
    }

    /**
     * Draws chart grid.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        int yEnd = SPACE_TO_AXIS_END;
        for (int i = 1; i <= numberOfXTicks; i++) {
            int xGridPosition = origin.x + i*xTickSpacing;
            g2d.drawLine(xGridPosition, origin.y, xGridPosition, yEnd);
        }

        int xEnd = getWidth() - SPACE_TO_AXIS_END;
        for (int i = 1; i <= numberOfYTicks; i++) {
            int yGridPosition = origin.y - i*yTickSpacing;
            g2d.drawLine(origin.x, yGridPosition, xEnd, yGridPosition);
        }
    }

    /**
     * Draws bars in bar chart.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawBars(Graphics2D g2d) {
        int barWidth = xTickSpacing;
        int y = origin.y;
        g2d.setColor(Color.ORANGE);

        for (int i = 0, numberOfPoints = chart.getData().size(); i < numberOfPoints; i++) {
            int x = origin.x + i*xTickSpacing + 1;
            int barHeight = (int) ((double)(chart.getData().get(i).getY() - chart.getyMin()))/chart.getTickSpacing() * yTickSpacing;
            g2d.fill3DRect(x, y - barHeight, barWidth, barHeight, true);
        }
    }

    /**
     * Calculates origin of the coordinate system.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     * @return      origin of the coordinate system.
     */
    private Point getOrigin(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = fm.getHeight()
                + fm.stringWidth(String.valueOf(chart.getyMax()))
                + 3*SPACE_TO_LABEL + TICK_SIZE;
        int y = getHeight() - 2*fm.getHeight() -  3*SPACE_TO_LABEL - TICK_SIZE;
        return new Point(x, y);
    }

    /**
     * Draws x-axis label.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawXLabel(Graphics2D g2d) {
        int height = getHeight();
        int width = getWidth();

        FontMetrics fm = g2d.getFontMetrics();

        int xLabelXPosition = origin.x + (width - origin.x - SPACE_TO_AXIS_END - fm.stringWidth(chart.getxLabel()))/2;
        int xLabelYPosition = height - SPACE_TO_LABEL;

        g2d.drawString(chart.getxLabel(), xLabelXPosition, xLabelYPosition);
    }

    /**
     * Draws y-axis label.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawYLabel(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics();

        /* When rotated by 90 degrees new x becomes old y and new y becomes negative old x coordinate. */
        int yLabelYPosition = SPACE_TO_AXIS_END + (origin.y - SPACE_TO_AXIS_END + fm.stringWidth(chart.getyLabel()))/2;
        int yLabelXPosition = fm.getHeight() + SPACE_TO_LABEL;

        g2d.rotate(Math.toRadians(-90));
        g2d.drawString(chart.getyLabel(), -yLabelYPosition, yLabelXPosition);
        g2d.rotate(Math.toRadians(90));
    }

    /**
     * Draws x-axis.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawXAxis(Graphics2D g2d) {
        Point end = new Point(getWidth() - SPACE_TO_AXIS_END, origin.y);
        numberOfXTicks = chart.getData().size();
        FontMetrics fm = g2d.getFontMetrics();

        xTickSpacing = drawAxis(g2d, origin, end, TickPosition.DOWN, numberOfXTicks);
        int tickLabelYPosition = origin.y + fm.getAscent() + SPACE_TO_LABEL + TICK_SIZE;

        for (int i = 1; i <= numberOfXTicks; i++) {
            String tickLabel = String.valueOf(chart.getData().get(i-1).getX());
            int tickLabelXPosition = origin.x + (int)((i - 0.5)*xTickSpacing - 0.5*fm.stringWidth(tickLabel));
            g2d.drawString(tickLabel, tickLabelXPosition, tickLabelYPosition);
        }
    }

    /**
     * Draws y-axis.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     */
    private void drawYAxis(Graphics2D g2d) {
        Point end = new Point(getHeight() - SPACE_TO_AXIS_END, origin.y);
        numberOfYTicks = (int)Math.ceil(
                ((double)(chart.getyMax() - chart.getyMin()))/chart.getTickSpacing());

        FontMetrics fm = g2d.getFontMetrics();

        g2d.rotate(Math.toRadians(-90), origin.x, origin.y);
        yTickSpacing = drawAxis(g2d, origin, end, TickPosition.UP, numberOfYTicks);

        g2d.rotate(Math.toRadians(90), origin.x, origin.y);

        for (int i = 0; i <= numberOfYTicks; i++) {
            String tickLabel = String.valueOf(chart.getyMin() + i*chart.getTickSpacing());

            int tickLabelXPosition = origin.x - SPACE_TO_LABEL/2 - fm.stringWidth(tickLabel) - TICK_SIZE;
            int tickLabelYPosition = (int)(origin.y - i*yTickSpacing + 0.5*fm.getAscent()) - 1;

            g2d.drawString(tickLabel, tickLabelXPosition, tickLabelYPosition);
        }
    }

    /**
     * Draws general coordinate system axis with specified number and direction of ticks and returns calculated tick
     * spacing. Note that axis extends before starting point for the amount of the tick size, and beyond the ending
     * point for the amount of arrow size plus spacing between last tick and arrow.
     * @param g2d   {@link Graphics2D} object used for rendering 2D graphics.
     * @param start     starting point, i.e. origin of the coordinate system.
     * @param end       ending point of the axis.
     * @param tickPosition  {@link hr.fer.zemris.java.gui.charts.BarChartComponent.TickPosition} specifies if ticks will
     *                      be placed above or under the axis.
     * @param numberOfTicks number of ticks to display.
     * @return              calculated tick spacing.
     */
    private int drawAxis(Graphics2D g2d, Point start, Point end, TickPosition tickPosition, int numberOfTicks) {
        final String ARROWHEAD = "➤";
        final int ARROWHEAD_SIZE = 4;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        g2d.drawLine(start.x - TICK_SIZE, start.y, end.x + ARROWHEAD_SIZE, start.y);

        int yTick = start.y + (tickPosition == TickPosition.UP ? -1 : 1) * TICK_SIZE;
        int tickSpacing = (end.x - start.x - SPACE_TO_ARROW) / numberOfTicks;

        for (int i = 0; i <= numberOfTicks; i++) {
            int xTick = start.x + i*tickSpacing;
            g2d.drawLine(xTick, start.y, xTick, yTick);
        }

        g2d.drawString(ARROWHEAD, end.x, end.y + ARROWHEAD_SIZE);

        return tickSpacing;
    }

    /**
     * Specifies if ticks should be drawn above or below the axis.
     */
    private enum TickPosition {
        /** Ticks should be drawn above the axis. */
        UP,
        /** Ticks should be drawn below the axis. */
        DOWN
    }
}
