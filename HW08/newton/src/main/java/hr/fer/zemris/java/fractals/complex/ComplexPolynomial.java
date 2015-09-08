package hr.fer.zemris.java.fractals.complex;

import java.util.Arrays;

/**
 * Represents polynomial with complex coefficients, defined by its coefficients.
 * @author ilijan
 */
public class ComplexPolynomial {

    /** Degree of polynomial + 1. */
    private int degreePlusOne;

    /** Stores coefficients of polynomial . */
    private Complex[] coefficients;

    /**
     * Constructor initializes new {@code ComplexPolynomial} using array of its coefficients of variable
     * size.
     * @param coefficients variable array of coefficients.
     */
    public ComplexPolynomial(Complex... coefficients) {
        if (coefficients.length > 1 && coefficients[0].equals(Complex.ZERO)) {
            throw new IllegalArgumentException("Leading coefficient cannot be zero.");
        }
        this.coefficients = Arrays.copyOf(coefficients, coefficients.length);
        degreePlusOne = (short)coefficients.length;
    }

    /**
     * Getter for polynomial degree.
     * @return degreePlusOne of polynomial.
     */
    public short order() {
        return (short)(degreePlusOne - 1);
    }

    /**
     * Returns product of polynomial with given polynomial.
     * @param p     multiplier for object on which the method is called.
     * @return      product of polynomials.
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        int newDegreePlusOne = this.degreePlusOne + p.degreePlusOne - 1;

        Complex[] newCoefficients = new Complex[newDegreePlusOne];
        for (int i = 0; i < newDegreePlusOne; i++) {
            newCoefficients[i] = new Complex();
        }

        // Multiplies each term of the first polynomial with each term of the second polynomial.
        for (int i = 0; i < this.degreePlusOne; i++) {
            for (int j = 0; j < p.degreePlusOne; j++) {
                newCoefficients[i + j] = newCoefficients[i + j].add(coefficients[i].mul(p.coefficients[j]));
            }
        }

        return new ComplexPolynomial(newCoefficients);
    }

    /**
     * Returns derivative of polynomial.
     * @return  derivative of polynomial.
     */
    public ComplexPolynomial derive() {

        if (degreePlusOne == 1) {
            return new ComplexPolynomial(Complex.ZERO);
        }
        int newDegree = degreePlusOne - 1;
        Complex[] newCoefficients = new Complex[newDegree];
        for (int coeffIndex = 0; coeffIndex < newDegree; coeffIndex++) {
            int power = degreePlusOne - coeffIndex - 1;

            newCoefficients[coeffIndex] = coefficients[coeffIndex].mul(Complex.fromReal(power));
        }
        return new ComplexPolynomial(newCoefficients);
    }

    /**
     * Evaluates polynomial for given argument.
     * @param z     argument for which polynomial is evaluated.
     * @return      value of polynomial for the given argument.
     */
    public Complex apply(Complex z) {
        Complex value = new Complex();
        for (int coeffIndex = 0; coeffIndex < degreePlusOne; coeffIndex++) {

            int power = degreePlusOne - coeffIndex - 1;
            value = value.add(z.power(power).mul(coefficients[coeffIndex]));
        }
        return value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("p(z) = ");
        for (int coeffIndex = 0, maxIndex = degreePlusOne - 1; coeffIndex < maxIndex; coeffIndex++) {
            if (coefficients[coeffIndex].equals(Complex.ZERO)) {
                continue;
            }

            int power = degreePlusOne - coeffIndex - 1;

            sb.append("(" + coefficients[coeffIndex] + ")z");
            if (power > 1) {
                sb.append("^" + power);
            }

            if (coeffIndex < maxIndex) {
                sb.append(" + ");
            }
        }

        Complex constantTerm = coefficients[degreePlusOne - 1];
        if (!constantTerm.equals(Complex.ZERO) || degreePlusOne == 1) {
            sb.append("(" + constantTerm + ")");
        }

        return sb.toString();
    }
}
