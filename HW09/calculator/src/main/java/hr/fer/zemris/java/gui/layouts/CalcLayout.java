package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Layout manager used for laying out components of a simple calculator. Components are distributed in equidistant grid
 * (grid has constant column width and constant row heights as determined by the biggest component in the grid).
 * Grid consists of 5 rows and 7 columns. Each component spans exactly one grid cell, except for the component in
 * position (1, 1) which spans cells from (1, 1) to (1, 5), and which is intended to be occupied by calculator screen.
 * @author ilijan
 */
public class CalcLayout implements LayoutManager2 {
    /** Number of rows in a grid. */
    private static final int ROWS = 5;
    /** Number of columns in a grid. */
    private static final int COLS = 7;

    /** Layout components. */
    private HashMap<RCPosition, Component> components;
    /** Spacing between components. */
    private int spacing;
    /** Current width of the component spanning single cell. */
    private int componentWidth;
    /** Current height of the component spanning single cell. */
    private int componentHeight;

    /**
     * Constructor initializes layout by setting spacing between components to desired number of pixels.
     * @param spacing   spacing between component.
     */
    public CalcLayout(int spacing) {
        if (spacing < 0) {
            throw new IllegalArgumentException("Spacing cannot be negative.");
        }
        this.spacing = spacing;
        components = new HashMap<>();
    }

    /**
     * Default constructor initializes layout with spacing between components set to zero.
     */
    public CalcLayout() {
        this(0);
    }

    /**
     * Adds component to the layout using object representing constraints. Constraints object is expected to be of type
     * {@code String} or {@link RCPosition}.
     * @param comp  component to be added.
     * @param constraints   object representing constraints.
     */
    public void addLayoutComponent(Component comp, Object constraints) {
        RCPosition position = getPosition(constraints);
        checkConstraints(position);

        if (!components.containsKey(position)) {
            components.put(position, comp);
        } else {
            throw new IllegalArgumentException("Layout already has component at position " + position + ".");
        }

    }

    /**
     * Lays components out using specified insets.
     * @param insets    insets obtained from parent container used for laying out components.
     */
    private void layoutComponents(Insets insets) {
        for (Map.Entry<RCPosition, Component> entry : components.entrySet()) {
            RCPosition position = entry.getKey();
            Component component = entry.getValue();

            Point location = getLocation(position);
            component.setLocation(location.x + insets.left, location.y + insets.top);

            component.setSize(getDimension(position));

            component.setFont(component.getFont().deriveFont(0.25f * componentWidth - 5.0f));
        }
    }

    /**
     * Generic method for finding extremal width and height of a component in a layout. Dimension provider determines
     * getter used for getting dimension (e.g. {@code component.getMinimumSize()}), and comparator determines weather
     * extremal sizes will be minimal or maximal. Width of the component in position (1, 1) is divided in 5 parts after
     * component spacing is taken into account.
     *
     * @return object of type {@link Dimension} containing extremal width and height of the components in a layout, not
     * necessarily obtained from the same component.
     */
    private Dimension getExtremeDimension(Function<Component, Dimension> dimensionProvider, Comparator<Integer> comparator) {
        int maxWidth = 0;
        int maxHeight = 0;

        for (Map.Entry<RCPosition, Component> entry : components.entrySet()) {

            RCPosition position = entry.getKey();
            Dimension componentDimension = dimensionProvider.apply(entry.getValue());

            if (componentDimension == null) {
                continue;
            }

            if (position.toString().equals("(1, 1)")) {
                componentDimension.width -= 4*spacing;
                componentDimension.width /= 5.0;
            }

            if (comparator.compare(componentDimension.width, maxWidth) > 0) {
                maxWidth = componentDimension.width;
            }
            if (comparator.compare(componentDimension.height, maxHeight) > 0) {
                maxHeight = componentDimension.height;
            }
        }
        return new Dimension(maxWidth, maxHeight);
    }

    /**
     * Gets location used for drawing component, relative to layout (insets are ignored here).
     * @param position  constraints object specifying position of the component.
     * @return          point specifying target location of the component relative to the layout bounds.
     */
    private Point getLocation(RCPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        int x = (col - 1)*(componentWidth + spacing);
        int y = (row - 1)*(componentHeight + spacing);

        return new Point(x, y);
    }

    /**
     * Gets constraints object of type {@link RCPosition} from supplied object of type {@code Object} or throws
     * {@link IllegalArgumentException} if object cannot casted to {@link RCPosition} or parsed as string to {@link RCPosition}.
     * @param constraints   constraints object.
     * @return              object of type {@link RCPosition} representing constraints.
     */
    private RCPosition getPosition(Object constraints) {
        if (constraints instanceof String) {
             return RCPosition.parse((String) constraints);
        } else if (constraints instanceof  RCPosition) {
            return (RCPosition) constraints;
        } else {
            throw new IllegalArgumentException("Unsupported type of constraint.");
        }
    }

    /**
     * Gets dimensions of the component based on its position. All the components have the same dimensions, except for
     * the component in position (1, 1).
     * @param position  position of the component.
     * @return          dimension of the component.
     */
    private Dimension getDimension(RCPosition position) {
        if (position.toString().equals("(1, 1)")) {
            return new Dimension(5*componentWidth + 4*spacing, componentHeight);
        } else {
            return new Dimension(componentWidth, componentHeight);
        }
    }

    /**
     * Checks if supplied constraints are valid.
     * @param position object representing constraints.
     */
    private void checkConstraints(RCPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        if (row < 1 || row > ROWS) {
            throw new IllegalArgumentException("Illegal row position (" + row + "). Row must be between 1 and " + ROWS);
        }
        if (col < 1 || col > COLS) {
            throw new IllegalArgumentException(
                    "Illegal column position (" + col + "). Column must be between 1 and " + COLS);
        }
        if (row == 1 && col >= 2 && col <= 5) {
            throw new IllegalArgumentException(
                    "Illegal position " + position +". Positions (1, 2), (1, 3), (1, 4) and (1, 5) cannot be used.");
        }
    }

    /**
     * Gets maximum layout size for displaying in specified container.
     * @param target    target container.
     * @return          maximum layout size.
     */
    public Dimension maximumLayoutSize(Container target) {
        Insets insets = target.getInsets();

        /* Returns dimension with minimal width of all maximal widths and minimal height of all maximal heights
        *  of all components (ignoring component in position (1, 1)). */
        Dimension preferredDimension = getExtremeDimension(Component::getMaximumSize, (x, y) -> -1 * Integer.compare(x, y));
        int preferredComponentWidth = preferredDimension.width;
        int preferredComponentHeight = preferredDimension.height;


        int width = preferredComponentWidth*COLS + spacing*(COLS - 1) + insets.left + insets.right;
        int height = preferredComponentHeight*ROWS + spacing*(ROWS - 1) + insets.top + insets.bottom;

        return new Dimension(width, height);
    }

    /** Does nothing. Required by LayoutManager2. */
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /** Does nothing. Required by LayoutManager2. */
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /** Does nothing. Required by LayoutManager2. */
    public void invalidateLayout(Container target) {
        /* Does nothing. Required by LayoutManager2. */
    }

    /** Does nothing. Required by LayoutManager2. */
    public void addLayoutComponent(String name, Component comp) {
        /* Does nothing. Required by LayoutManager2. */
    }

    /** Does nothing. Required by LayoutManager2. */
    public void removeLayoutComponent(Component comp) {
        /* Does nothing. Required by LayoutManager2. */
    }

    /**
     * Gets preferred layout size for displaying in specified parent container..
     * @param parent    parent container.
     * @return          preferred layout size.
     */
    public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();

        Dimension preferredDimension = getExtremeDimension(Component::getPreferredSize, Integer::compare);
        int preferredComponentWidth = preferredDimension.width;
        int preferredComponentHeight = preferredDimension.height;


        int width = preferredComponentWidth*COLS + spacing*(COLS - 1) + insets.left + insets.right;
        int height = preferredComponentHeight*ROWS + spacing*(ROWS - 1) + insets.top + insets.bottom;

        return new Dimension(width, height);
    }

    /**
     * Gets minimum layout size for displaying in specified parent container..
     * @param parent    parent container.
     * @return          minimum layout size.
     */
    public Dimension minimumLayoutSize(Container parent) {
        Insets insets = parent.getInsets();

        Dimension preferredDimension = getExtremeDimension(Component::getMinimumSize, Integer::compare);
        int preferredComponentWidth = preferredDimension.width;
        int preferredComponentHeight = preferredDimension.height;


        int width = preferredComponentWidth*COLS + spacing*(COLS - 1) + insets.left + insets.right;
        int height = preferredComponentHeight*ROWS + spacing*(ROWS - 1) + insets.top + insets.bottom;

        return new Dimension(width, height);
    }

    /**
     * Lays out component in a given parent container, according to the layout constraints.
     * @param parent    parent container.
     */
    public void layoutContainer(Container parent) {
        Rectangle parentRectangle = parent.getBounds();
        Insets insets = parent.getInsets();

        int scaledWidth = (parentRectangle.width - (COLS - 1)*spacing - insets.left - insets.right) / COLS;
        int scaledHeight = (parentRectangle.height - (ROWS - 1)*spacing - insets.top - insets.right) / ROWS;

        componentWidth = scaledWidth;
        componentHeight = scaledHeight;

        layoutComponents(insets);
    }
}
