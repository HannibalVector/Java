package hr.fer.zemris.java.gui.layouts;

/**
 * Represents constraints on component placement in {@link CalcLayout} layout manager.
 * @author ilijan
 */
public class RCPosition {
    /** Row of the component. */
    int row;
    /** Column of the component. */
    int column;

    /**
     * Constructor initializes constraints using specified row and column.
     * @param row       row of the component.
     * @param column    column of the component.
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter for the row of the component.
     * @return  row of the component.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column of the component.
     * @return  column of the component.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Parses given string as {@link RCPosition} object.
     * @param constraints   string to be parsed.
     * @return              parsed {@link RCPosition} object.
     */
    public static RCPosition parse(String constraints) {
        String[] rowAndColumn = constraints.trim().split(",");
        int row = Integer.parseInt(rowAndColumn[0].trim());
        int col = Integer.parseInt(rowAndColumn[1].trim());

        return new RCPosition(row, col);
    }

    @Override
    public String toString() {
        return "("+ row + ", " + column +")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RCPosition)) {
            return false;
        }

        RCPosition other = (RCPosition) obj;
        return other.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
