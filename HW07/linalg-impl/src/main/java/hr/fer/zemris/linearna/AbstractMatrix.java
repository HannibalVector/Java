package hr.fer.zemris.linearna;

/**
 * Represents abstract matrix. Implements methods for working with matrices, including
 * addition, subtraction, matrix multiplication, multiplication by scalar, transpose,
 * inverse, determinant. Supports generating live view as vector, transpose or submatrix.
 *
 * @author ilijan
 */
public abstract class AbstractMatrix implements IMatrix {
    /** Precision used for comparing double values in {@link #equals(Object)} method. */
    public static final double PRECISION = 1.0e-8;

    @Override
    public IMatrix nTranspose(boolean liveView) {
        IMatrix matrixTranspose = new MatrixTransposeView(this);
        if (liveView) {
            return matrixTranspose;
        } else {
            return matrixTranspose.copy();
        }
    }

    @Override
    public IMatrix add(IMatrix iMatrix) {
        checkDimensionCompatibility(iMatrix);
        for (int row = this.getRowsCount() - 1; row >= 0; row--) {
            for (int col = this.getColsCount() - 1; col >= 0; col--) {
                this.set(row, col, this.get(row, col) + iMatrix.get(row, col));
            }
        }
        return this;
    }

    @Override
    public IMatrix nAdd(IMatrix iMatrix) {
        return this.copy().add(iMatrix);
    }

    @Override
    public IMatrix sub(IMatrix iMatrix) {
        checkDimensionCompatibility(iMatrix);
        for (int row = this.getRowsCount() - 1; row >= 0; row--) {
            for (int col = this.getColsCount() - 1; col >= 0; col--) {
                this.set(row, col, this.get(row, col) - iMatrix.get(row, col));
            }
        }
        return this;
    }

    /**
     * Checks dimension compatibility for operations which demand that matrices
     * have same number of rows and same number of columns.
     * @param other matrix to compare rows and columns to.
     */
    private void checkDimensionCompatibility(IMatrix other) {
        if (this.getColsCount() != other.getColsCount()
                || this.getRowsCount() != other.getRowsCount()) {
            throw new IncompatibleOperandException("Matrix dimensions do not agree.");
        }
    }

    @Override
    public IMatrix nSub(IMatrix iMatrix) {
        return this.copy().sub(iMatrix);
    }

    @Override
    public IMatrix nMultiply(IMatrix iMatrix) {
        if (this.getColsCount() != iMatrix.getRowsCount()) {
            throw new IncompatibleOperandException(
                    "Number of columns of the first operand must be equal to " +
                    "the number of rows of the second operand.");
        }
        IMatrix result = newInstance(this.getRowsCount(), iMatrix.getColsCount());
        for (int row = this.getRowsCount() - 1; row >= 0; row--) {
            for (int col = iMatrix.getColsCount() - 1; col >= 0; col--) {
                for (int k = this.getColsCount() - 1; k >= 0; k--) {
                    result.set(row, col, result.get(row, col) + this.get(row, k)*iMatrix.get(k, col));
                }
            }
        }
        return result;
    }

    @Override
    public double determinant() throws IncompatibleOperandException {
        if (getRowsCount() != getColsCount()) {
            throw new IncompatibleOperandException("Matrix must be square.");
        }

        if (getRowsCount() == 2) {
            return get(0, 0)*get(1, 1) - get(0, 1)*get(1, 0);
        }

        double determinant = 0;
        double sign = 1.0;
        for (int i = 0; i < getRowsCount(); i++) {
            determinant += sign * get(0, i) * this.subMatrix(0, i, true).determinant();
            sign *= -1.0;
        }

        return determinant;
    }

    @Override
    public IMatrix subMatrix(int row, int col, boolean liveView) {
        IMatrix subMatrix = new MatrixSubMatrixView(this, row, col);
        if (liveView) {
            return subMatrix;
        } else {
            return subMatrix.copy();
        }
    }

    @Override
    public IMatrix nInvert() {
        double determinant = determinant();
        if (determinant == 0) {
            throw new IllegalArgumentException("Matrix is non-invertible.");
        }
        IMatrix inverse = newInstance(getRowsCount(), getColsCount());
        double sign = 1.0;
        for (int row = 0; row < getRowsCount(); row++) {
            for (int col = 0; col < getColsCount(); col++) {
                inverse.set(row, col, sign * subMatrix(col, row, true).determinant());
                sign *= -1.0;
            }
        }
        inverse.scalarMultiply(1.0 / determinant);
        return inverse;
    }

    @Override
    public double[][] toArray() {
        double[][] arrayRepresentation = new double[getRowsCount()][getColsCount()];
        for (int row = 0; row < getRowsCount(); row++) {
            for (int col = 0; col < getColsCount(); col++) {
                arrayRepresentation[row][col] = get(row, col);
            }
        }
        return arrayRepresentation;
    }

    @Override
    public IVector toVector(boolean liveView) {
        IVector vectorView = new VectorMatrixView(this);
        if (liveView) {
            return vectorView;
        } else {
            return vectorView.copy();
        }
    }

    @Override
    public IMatrix nScalarMultiply(double v) {
        return this.copy().scalarMultiply(v);
    }

    @Override
    public IMatrix scalarMultiply(double v) {
        for (int row = this.getRowsCount() - 1; row >= 0; row--) {
            for (int col = this.getColsCount() - 1; col >= 0; col--) {
                this.set(row, col, this.get(row, col)*v);
            }
        }
        return this;
    }

    @Override
    public IMatrix makeIdentity() {
        IMatrix identityMatrix = newInstance(getRowsCount(), getColsCount());
        for (int i = Math.min(getColsCount(), getRowsCount()) - 1; i >= 0; i--) {
            identityMatrix.set(i, i, 1.0);
        }
        return identityMatrix;
    }

    @Override
    public IMatrix copy() {
        IMatrix newMatrix = newInstance(getRowsCount(), getColsCount());
        for (int row = this.getRowsCount() - 1; row >= 0; row--) {
            for (int col = this.getColsCount() - 1; col >= 0; col--) {
                newMatrix.set(row, col, this.get(row, col));
            }
        }
        return newMatrix;
    }

    @Override
    public String toString() {
        return toString(3);
    }

    /**
     * Generates string representation of matrix using provided precision for
     * printing double values.
     * @param n precision for printing double values.
     * @return  string representation of matrix.
     */
    public String toString(int n) {
        String format = "%." + n + "f";
        StringBuilder sb = new StringBuilder();
        for(int row = 0; row < getRowsCount(); row++) {
            sb.append("[");
            for(int col = 0; col < getColsCount(); col++) {
                sb.append(String.format(format, get(row, col))).append(", ");
            }
            sb.replace(sb.length() - 2, sb.length(), "]\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AbstractMatrix)) {
            return false;
        }
        IMatrix other = (IMatrix)obj;

        try {
            checkDimensionCompatibility(other);
        } catch (IncompatibleOperandException ex) {
            return false;
        }

        int rows = getRowsCount();
        int cols = getColsCount();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (Math.abs(this.get(row, col) - other.get(row, col)) > PRECISION) {
                    return false;
                }
            }
        }
        return true;
    }
}
