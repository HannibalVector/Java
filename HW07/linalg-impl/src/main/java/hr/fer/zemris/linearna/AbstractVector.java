package hr.fer.zemris.linearna;

/**
 * Represents abstract vector. Implements methods for working with vectors, including
 * addition, subtraction, multiplication by scalar, scalar product, normalization,
 * calculating norm, calculating cosine between vectors, etc.
 * Supports generating live views as row or column matrix.
 *
 * @author ilijan
 */
public abstract class AbstractVector implements IVector{
    /** Precision used for comparing double values in {@link #equals(Object)} method. */
    public static final double PRECISION = 1.0e-14;

    @Override
    public IVector copyPart(int i) {
        IVector result = newInstance(i);
        for(int j = Math.min(i, this.getDimension()) - 1; j >= 0; j--) {
            result.set(j, this.get(j));
        }
        return result;
    }
    @Override
    public IVector add(IVector iVector) throws IncompatibleOperandException {
        checkDimensionCompatibility(iVector);
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i) + iVector.get(i));
        }
        return this;
    }

    /**
     * Checks that dimensions of provided vector and current vector are the same
     * when performing operations that demand equal dimensions.
     * @param other vector to compare dimension to.
     */
    private void checkDimensionCompatibility(IVector other) {
        if (this.getDimension() != other.getDimension()) {
            throw new IncompatibleOperandException("Vector dimensions do not agree.");
        }
    }

    @Override
    public IVector nAdd(IVector iVector) throws IncompatibleOperandException {
        return this.copy().add(iVector);
    }

    @Override
    public IVector sub(IVector iVector) throws IncompatibleOperandException {
        return this.add(iVector.nScalarMultiply(-1.0));
    }

    @Override
    public IVector nSub(IVector iVector) throws IncompatibleOperandException {
        return this.copy().sub(iVector);
    }

    @Override
    public IVector scalarMultiply(double v) {
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            this.set(i, this.get(i)*v);
        }
        return this;
    }

    @Override
    public IVector nScalarMultiply(double v) {
        return this.copy().scalarMultiply(v);
    }

    @Override
    public double norm() {
        return Math.sqrt(this.scalarProduct(this));
    }

    @Override
    public IVector normalize() {
        double norm = norm();
        if (norm == 0) {
            throw new IllegalArgumentException("Cannot normalize nul-vector.");
        }
        return this.scalarMultiply(1.0 / norm);
    }

    @Override
    public IVector nNormalize() {
        return this.copy().normalize();
    }

    @Override
    public double cosine(IVector iVector) throws IncompatibleOperandException {
        checkDimensionCompatibility(iVector);
        double normThis = norm();
        double normOther = iVector.norm();
        if (normThis == 0 || normOther == 0) {
            throw new IllegalArgumentException("Cannot calculate cosine with nul-vectors.");
        }

        return this.scalarProduct(iVector)/(normThis * normOther);
    }

    @Override
    public double scalarProduct(IVector iVector) throws IncompatibleOperandException {
        checkDimensionCompatibility(iVector);
        double result = 0;
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            result += this.get(i) * iVector.get(i);
        }
        return result;
    }

    @Override
    public IVector nVectorProduct(IVector iVector) throws IncompatibleOperandException {
        if (this.getDimension() != 3 || iVector.getDimension() != 3) {
            throw new IncompatibleOperandException("Both vectors must be 3-dimensional.");
        }
        IVector result = newInstance(3);
        result.set(0, this.get(1)*iVector.get(2) - this.get(2)*iVector.get(1));
        result.set(1, this.get(2) * iVector.get(0) - this.get(0) * iVector.get(2));
        result.set(2, this.get(0)*iVector.get(1) - this.get(1)*iVector.get(0));
        return result;
    }

    @Override
    public IVector nFromHomogeneus() {
        if (this.getDimension() < 2) {
            throw new IllegalArgumentException("Vector in homogeneous space must have dimension at least 2.");
        }
        if (this.get(this.getDimension() - 1) == 0) {
            throw new IllegalArgumentException("Homogeneous coordinate must not be zero.");
        }
        return this.copyPart(this.getDimension() - 1).scalarMultiply(1.0 / this.get(this.getDimension() - 1));
    }

    @Override
    public IMatrix toRowMatrix(boolean liveView) {
        IMatrix matrixView = new MatrixVectorView(this, true);
        if (liveView) {
            return matrixView;
        } else {
            return matrixView.copy();
        }
    }

    @Override
    public IMatrix toColumnMatrix(boolean liveView) {
        IMatrix matrixView = new MatrixVectorView(this, false);
        if (liveView) {
            return matrixView;
        } else {
            return matrixView.copy();
        }
    }

    @Override
    public double[] toArray() {
        double[] result = new double[this.getDimension()];
        for (int i = this.getDimension() - 1; i >= 0; i--) {
            result[i] = this.get(i);
        }
        return result;
    }

    @Override
    public IVector copy() {
        IVector newVector = newInstance(getDimension());
        for (int i = getDimension() - 1; i >= 0; i--) {
            newVector.set(i, this.get(i));
        }
        return newVector;
    }

    @Override
    public String toString() {
        return toString(3);
    }

    /**
     * Generates string representation of vector using provided precision for
     * printing double values.
     * @param n precision for printing double values.
     * @return  string representation of vector.
     */
    public String toString(int n) {
        String format = "%." + n + "f";
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < getDimension(); i++) {
            sb.append(String.format(format, get(i))).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AbstractVector)) {
            return false;
        }
        IVector other = (IVector)obj;

        try {
            checkDimensionCompatibility(other);
        } catch (IncompatibleOperandException ex) {
            return false;
        }

        int dimension = getDimension();
        for (int i = 0; i < dimension; i++) {
            if (Math.abs(this.get(i) - other.get(i)) > PRECISION) {
                return false;
            }
        }
        return true;
    }
}
