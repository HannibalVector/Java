package hr.fer.zemris.linearna;

/**
 * Implements matrix whose elements are real numbers.
 *
 * @author ilijan
 */
public class Matrix extends AbstractMatrix {
    /** Matrix elements. */
    private double[][] elements;
    /** Number of rows. */
    private int rows;
    /** Number of columns. */
    private int cols;

    /**
     * Constructor initializes new matrix to given number of rows and columns.
     * @param rows number of rows.
     * @param cols number of columns.
     */
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        elements = new double[rows][cols];
    }

    /**
     * Constructor initializes new matrix to given number of rows and columns,
     * using provided array of elements.
     * Supplied array of elements can be used or copied, depending on provided
     * flag.
     *
     * @param rows  number of rows.
     * @param cols  number of columns.
     * @param elements  provided array of elements.
     * @param useProvidedArray  if {@code true} provided array is used, otherwise
     *                          provided array is copied.
     */
    public Matrix(int rows, int cols, double[][] elements, boolean useProvidedArray) {
        this.rows = rows;
        this.cols = cols;
        if (useProvidedArray) {
            this.elements = elements;
        } else {
            this.elements = new double[rows][cols];
            for (int row = 0; row < rows; row++) {
                System.arraycopy(elements[row], 0, this.elements[row], 0, cols);
            }
        }
    }

    @Override
    public int getRowsCount() {
        return rows;
    }

    @Override
    public int getColsCount() {
        return cols;
    }

    @Override
    public double get(int row, int col) {
        return elements[row][col];
    }

    @Override
    public IMatrix set(int row, int col, double v) {
        elements[row][col] = v;
        return this;
    }

    @Override
    public IMatrix copy() {
        return new Matrix(rows, cols, elements, false);
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        return new Matrix(rows, cols);
    }

    /**
     * Parses matrix from given string.
     * @param string    string to parse matrix from.
     * @return  parsed matrix.
     */
    public static IMatrix parseSimple(String string) {
        String[] rowStrings = string.trim().split("\\|");
        int rows = rowStrings.length;

        String[][] colStrings = new String[rows][];
        for (int row = 0; row < rows; row++) {
            colStrings[row] = rowStrings[row].trim().split(" +");
        }

        int cols = colStrings[0].length;
        double[][] elements = new double[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                elements[row][col] = Double.parseDouble(colStrings[row][col]);
            }
        }
        return new Matrix(rows, cols, elements, true);
    }
}
