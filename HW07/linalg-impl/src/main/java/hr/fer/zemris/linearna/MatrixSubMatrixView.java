package hr.fer.zemris.linearna;

/**
 * Implements view on matrix where given row and column are removed from matrix.
 *
 * @author ilijan
 */
public class MatrixSubMatrixView extends AbstractMatrix {
    /** Viewed matrix. */
    private IMatrix matrix;
    /** Indices of rows remaining in view. */
    private int[] rowIndices;
    /** Indices of columns remaining in view. */
    private int[] colIndices;

    /**
     * Constructor initializes new submatrix view using provided matrix,
     * and row and column to be taken out.
     * @param matrix    matrix to be viewed.
     * @param row   row to be taken out of matrix.
     * @param col   column to be taken out of matrix.
     */
    public MatrixSubMatrixView(IMatrix matrix, int row, int col) {
        this.matrix = matrix;

        checkIndices(matrix, row, col);
        int rows = matrix.getRowsCount();
        int cols = matrix.getColsCount();

        rowIndices = new int[rows - 1];
        colIndices = new int[cols - 1];

        int j = 0;
        for (int i = 0; i < rows; i++) {
            if (i == row) {
                continue;
            }
            rowIndices[j] = i;
            j++;
        }

        j = 0;
        for (int i = 0; i < cols; i++) {
            if (i == col) {
                continue;
            }
            colIndices[j] = i;
            j++;
        }
    }

    /**
     * Checks if given indices are out of bounds of viewed matrix dimensions.
     * @param matrix    viewed matrix.
     * @param row   row index to be checked.
     * @param col   column index to be checked.
     */
    private void checkIndices(IMatrix matrix, int row, int col) {
        if (row < 0 || row >= matrix.getRowsCount() || col < 0 || col >= matrix.getColsCount()) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Constructor initializes new submatrix view using matrix and arrays of indices of
     * preserved rows and columns.
     *
     * @param matrix    viewed matrix.
     * @param rowIndices    indices of preserved rows.
     * @param colIndices    indices of preserved columns.
     */
    private MatrixSubMatrixView(IMatrix matrix, int[] rowIndices, int[] colIndices) {
        this.matrix = matrix;
        this.rowIndices = rowIndices;
        this.colIndices = colIndices;
    }

    @Override
    public int getRowsCount() {
        return rowIndices.length;
    }

    @Override
    public int getColsCount() {
        return colIndices.length;
    }

    @Override
    public double get(int row, int col) {
        return matrix.get(rowIndices[row], colIndices[col]);
    }

    @Override
    public IMatrix set(int row, int col, double v) {
        matrix.set(rowIndices[row], colIndices[col], v);
        return this;
    }

    @Override
    public IMatrix newInstance(int row, int col) {
        return matrix.newInstance(row, col);
    }

    @Override
    public IMatrix subMatrix(int row, int col, boolean liveView) {

        checkIndices(this, row, col);

        int[] newRowIndices = new int[rowIndices.length - 1];
        int[] newColIndices = new int[colIndices.length - 1];

        int j = 0;
        for (int i = 0; i < rowIndices.length; i++) {
            if (i == row) {
                continue;
            }
            newRowIndices[j] = rowIndices[i];
            j++;
        }

        j = 0;
        for (int i = 0; i < colIndices.length; i++) {
            if (i == col) {
                continue;
            }
            newColIndices[j] = colIndices[i];
            j++;
        }

        IMatrix subMatrix = new MatrixSubMatrixView(matrix, newRowIndices, newColIndices);

        if (liveView) {
            return subMatrix;
        } else {
            return subMatrix.copy();
        }
    }
}
