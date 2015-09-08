package hr.fer.zemris.linearna;

/**
 * Implements view on vector as row or column matrix.
 *
 * @author ilijan
 */
public class MatrixVectorView extends AbstractMatrix {
    /** Viewed vector. */
    private IVector vector;
    /** Flag that indicates if vector should be viewed as row matrix. */
    private boolean asRowMatrix;

    /**
     * Constructor initializes view using vector and flag indicating whether
     * vector should be viewed as row or column matrix.
     * @param vector    viewed vector.
     * @param asRowMatrix   if {@code true} vector will be viewed as row matrix,
     *                      otherwise vector will be viewed as column matrix.
     */
    public MatrixVectorView(IVector vector, boolean asRowMatrix) {
        this.vector = vector;
        this.asRowMatrix = asRowMatrix;
    }

    @Override
    public int getRowsCount() {
        if(asRowMatrix) {
            return 1;
        } else {
            return vector.getDimension();
        }
    }

    @Override
    public int getColsCount() {
        if(asRowMatrix) {
            return vector.getDimension();
        } else {
            return 1;
        }
    }

    @Override
    public double get(int row, int col) {
        if(asRowMatrix) {
            return vector.get(col);
        } else {
            return vector.get(row);
        }
    }

    @Override
    public IMatrix set(int row, int col, double v) {
        if(asRowMatrix) {
            vector.set(col, v);
        } else {
            vector.set(row, v);
        }
        return this;
    }

    @Override
    public IMatrix newInstance(int rows, int cols) {
        return LinAlgDefaults.defaultMatrix(rows, cols);
    }
}
