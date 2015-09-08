package hr.fer.zemris.linearna;

/**
 * Views row or column matrix as a vector.
 *
 * @author ilijan
 */
public class VectorMatrixView extends AbstractVector {
    /** Matrix to be viewed as vector. */
    private IMatrix matrix;
    /** Dimension of vector that represents matrix view. */
    private int dimension;
    /** Flag indicates if viewed matrix is row matrix. */
    private boolean rowMatrix;

    /**
     * Constructor initializes view using provided matrix.
     * @param matrix    matrix to be viewed.
     */
    public VectorMatrixView(IMatrix matrix) {
        if (matrix.getRowsCount() == 1) {
            rowMatrix = true;
            dimension = matrix.getColsCount();
        } else if (matrix.getColsCount() == 1) {
            rowMatrix = false;
            dimension = matrix.getRowsCount();
        } else {
            throw new IllegalArgumentException("Matrix needs to be row matrix or column matrix.");
        }

        this.matrix = matrix;
    }

    @Override
    public double get(int i) {
        if(rowMatrix) {
            return matrix.get(0, i);
        } else {
            return matrix.get(i, 0);
        }
    }

    @Override
    public IVector set(int i, double v) throws UnmodifiableObjectException {
        if(rowMatrix) {
            matrix.set(0, i, v);
        } else {
            matrix.set(i, 0, v);
        }
        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector newInstance(int i) {
        return LinAlgDefaults.defaultVector(i);
    }
}