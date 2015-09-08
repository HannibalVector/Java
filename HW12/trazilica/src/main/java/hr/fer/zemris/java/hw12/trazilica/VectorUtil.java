package hr.fer.zemris.java.hw12.trazilica;

/**
 * Utility class offering few methods for working with vectors which are needed in calculating
 * similarity between the documents.
 * @author ilijan
 */
public class VectorUtil {

    /**
     * Represents dot product of two vectors which are represented by {@code double} arrays.
     * @param vector1   the first vector in the product.
     * @param vector2   the second vector in the product.
     * @return          result of the dot product of given vectors.
     */
    public static double dotProduct(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vector dimensions must agree.");
        }
        double product = 0;
        for (int i = 0; i < vector1.length; i++) {
            product += vector1[i] * vector2[i];
        }
        return product;
    }

    /**
     * Calculates euclidean norm of the given vector which is represented by {@code double} array.
     * @param vector    given vector.
     * @return          norm of the given vector.
     */
    public static double norm(double[] vector) {
        return Math.sqrt(dotProduct(vector, vector));
    }

    /**
     * Calculates cosine of angle between two given vectors which are represented by {@code double} arrays.
     * @param vector1   the first vector.
     * @param vector2   the second vector.
     * @return          cosine of the angle between given vectors.
     */
    public static double cosine(double[] vector1, double[] vector2) {
        double product = dotProduct(vector1, vector2);
        double norm1 = norm(vector1);
        double norm2 = norm(vector2);

        return product / (norm1 * norm2);
    }
}
