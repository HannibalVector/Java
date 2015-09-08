package hr.fer.zemris.linearna;

/**
 * Implements view on matrix where original matrix is transposed.
 *
 * @author ilijan
 */
public class MatrixTransposeView extends AbstractMatrix {
    /** Viewed matrix. */
    private IMatrix matrix;

    /**
     * Constructor initializes new transpose view using given matrix.
     * @param matrix    matrix to create transposed view from.
     */
    public MatrixTransposeView(IMatrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public int getRowsCount() {
        return matrix.getColsCount();
    }

    @Override
    public int getColsCount() {
        return matrix.getRowsCount();
    }

    @Override
    public double get(int row, int col) {
        return matrix.get(col, row);
    }

    @Override
    public IMatrix set(int row, int col, double v) {
        matrix.set(col, row, v);
        return this;
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        return matrix.newInstance(rows, cols);
    }
}
