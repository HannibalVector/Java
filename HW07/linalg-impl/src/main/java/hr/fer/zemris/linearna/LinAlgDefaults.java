package hr.fer.zemris.linearna;

/**
 * Provides static methods for creating instances of default implementations of vectors
 * and matrices.
 * @author ilijan
 */
public class LinAlgDefaults {
    /**
     * Creates new instance of {@link Matrix} with provided number of rows and columns.
     * @param rows  number of rows.
     * @param cols  number of columns.
     * @return  new {@link Matrix} object.
     */
    public static IMatrix defaultMatrix(int rows, int cols) {
        return new Matrix(rows, cols);
    }

    /**
     * Creates new instance of {@link Vector} with provided dimension.
     * @param dimension dimension of new vector.
     * @return  new {@link Vector} object.
     */
    public static IVector defaultVector(int dimension) {
        return new Vector(dimension);
    }
}
