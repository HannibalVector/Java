package hr.fer.zemris.java.fractals.complex;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;

import java.util.Arrays;

/**
 * Represents polynomial with complex coefficients, defined by its roots.
 * @author ilijan
 */
public class ComplexRootedPolynomial {

    /** Stores roots of polynomial . */
    private Complex[] roots;

    /**
     * Constructor initializes new {@code ComplexRootedPolynomial} using array of its roots of variable size
     * @param roots     variable array of roots.
     */
    public ComplexRootedPolynomial(Complex ... roots) {
        this.roots = Arrays.copyOf(roots, roots.length);
    }

    /**
     * Evaluates polynomial for given argument.
     * @param z     argument for which polynomial is evaluated.
     * @return      value of polynomial for the given argument.
     */
    public Complex apply(Complex z) {
        Complex value = new Complex(Complex.ONE);
        for (int rootIndex = 0; rootIndex < roots.length; rootIndex++) {
            value = value.mul(z.sub(roots[rootIndex]));
        }
        return value;
    }

    /**
     * Converts {@code ComplexRootedPolynomial} to {@link ComplexPolynomial}.
     * @return      new {@link ComplexPolynomial} in which polynomial is represented with its coefficients
     *              rather than roots.
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial polynomial = new ComplexPolynomial(Complex.ONE);
        for (int rootIndex = 0; rootIndex < roots.length; rootIndex++) {

            polynomial = polynomial.multiply(new ComplexPolynomial(Complex.ONE, roots[rootIndex].mul(Complex.ONE_NEG)));
        }
        return polynomial;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("p(z) = ");
        for (int rootIndex = 0; rootIndex < roots.length; rootIndex++) {
            sb.append("[z - (" + roots[rootIndex] + ")]");
        }

        return sb.toString();
    }

    /**
     * Returns index of the nearest root of polynomial for given complex number, if there exists a root within
     * distance defined by threshold around given complex number.
     * @param z             complex number near which the search is performed.
     * @param threshold     minimal distance for regarding given complex number as known root.
     * @return              index of the closest root, or -1 if closest root was not within given threshold.
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        int closestIndex = 0;
        double minimumDistance = roots[0].sub(z).getMagnitude();

        for (int rootIndex = 1; rootIndex < roots.length; rootIndex++) {
            double distance = roots[rootIndex].sub(z).getMagnitude();
            if (distance < minimumDistance) {
                minimumDistance = distance;
                closestIndex = rootIndex;
            }
        }
        if (minimumDistance < threshold) {
            return closestIndex;
        } else {
            return -1;
        }
    }
}
