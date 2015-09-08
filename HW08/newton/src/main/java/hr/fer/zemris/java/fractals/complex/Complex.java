package hr.fer.zemris.java.fractals.complex;

import java.util.function.Function;

/**
 * Class implements support for working with complex numbers. Class {@code Complex} represents
 * unmodifiable complex number.
 * Created by ilijan on 3/30/15.
 */
public final class Complex {

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ONE_NEG = new Complex(-1, 0);
    public static final Complex IM = new Complex(0, 1);
    public static final Complex IM_NEG = new Complex(0, -1);

    /** Real part of complex number. */
    private double realPart;

    /** Imaginary part of complex number. */
    private double imaginaryPart;

    /**
     * Default constructor initializes new complex number to zero.
     */
    public Complex() {
        super();
    }

    /**
     * Constructor that initializes complex number using its real and imaginary part.
     * @param realPart          real part of complex number.
     * @param imaginaryPart     imaginary part of complex number.
     */
    public Complex(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    /**
     * Constructor that initializes complex number by copying other complex number parts.
     * @param z     complex number to be copied in new complex number.
     */
    public Complex(Complex z) {
        this.realPart = z.realPart;
        this.imaginaryPart = z.imaginaryPart;
    }

    /**
     * Static factory method which creates new {@link Complex} object only from its real part. Imaginary part is assumed to be zero.
     * @param real  real part of complex number.
     * @return      new {@link Complex} with supplied real part.
     */
    public static Complex fromReal(double real) {
        return new Complex(real, 0);
    }

    /**
     * Static factory method which creates new {@link Complex} object only from its imaginary part. Real part is assumed to be zero.
     * @param imaginary     imaginary part of complex number.
     * @return              new {@link Complex} with supplied imaginary part.
     */
    public static Complex fromImaginary(double imaginary) {
        return new Complex(0, imaginary);
    }

    /**
     * Static factory method which creates new {@link Complex} object from its magnitude and angle (argument).
     * @param magnitude magnitude of complex number to be created.
     * @param angle     angle (argument) of complex number to be created.
     * @return          new {@link Complex} with supplied magnitude and angle.
     */
    public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
        double real = magnitude * Math.cos(angle);
        double imaginary = magnitude * Math.sin(angle);

        return new Complex(real, imaginary);
    }

    /**
     * Static factory method which creates new {@link Complex} by parsing it from given string.
     * Examples of input: "i", "-5 + 4i", " 5e-3i - 7.12", "-i", "5".
     * @param string    string to be parsed as {@link Complex}.
     * @return          new {@link Complex} parsed from string.
     * @throws  IllegalArgumentException        if empty string is passed to method.
     * @throws ComplexParserException    if string contains multiple real or imaginary parts.
     * @throws  NumberFormatException if real or imaginary part cannot be parsed as {@code double}.
     */
    public static Complex parse (String string) {
        ComplexParser parser = new ComplexParser(string);
        return parser.getComplexNumber();
    }

    /**
     * Getter for real part of complex number.
     * @return  real part of complex number.
     */
    public double getReal() {
        return realPart;
    }

    /**
     * Getter for imaginary part of complex number.
     * @return  imaginary part of complex number.
     */
    public double getImaginary() {
        return imaginaryPart;
    }

    /**
     * Calculates magnitude of complex number.
     * @return  magnitude of complex number.
     */
    public double getMagnitude() {
        return Math.sqrt(realPart * realPart + imaginaryPart * imaginaryPart);
    }

    /**
     * Calculates angle (argument) of complex number. Returns standard value from interval (-PI, PI].
     * @return  angle (argument) of complex number.
     */
    public double getAngle() {
        return Math.atan2(imaginaryPart, realPart);
    }

    /**
     * Adds complex number to an instance of complex number.
     * @param numberToAdd   number to add to an instance.
     * @return              new {@link Complex} which is a result of addition.
     */
    public Complex add(Complex numberToAdd) {
        double real = realPart + numberToAdd.getReal();
        double imaginary = imaginaryPart + numberToAdd.getImaginary();

        return new Complex(real, imaginary);
    }

    /**
     * Subtracts complex number from an instance of complex number.
     * @param numberToSubtract  number to subtract from an instance.
     * @return                  new {@link Complex} which is a result of subtraction.
     */
    public Complex sub(Complex numberToSubtract) {
        double real = realPart - numberToSubtract.getReal();
        double imaginary = imaginaryPart - numberToSubtract.getImaginary();

        return new Complex(real, imaginary);
    }

    /**
     * Multiplies an instance of complex number by supplied complex number.
     * @param multiplier    number to multiply an instance with.
     * @return              new {@link Complex} which is a result of multiplication.
     */
    public Complex mul(Complex multiplier) {
        double magnitude = getMagnitude() * multiplier.getMagnitude();
        double angle = getAngle() + multiplier.getAngle();

        return Complex.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Divides an instance of complex number by supplied complex number.
     * @param divisor  number to divide an instance with.
     * @return         new {@link Complex} which is a result of division.
     */
    public Complex div(Complex divisor) {
        if (divisor.getMagnitude() == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        double magnitude = getMagnitude() / divisor.getMagnitude();
        double angle = getAngle() - divisor.getAngle();

        return Complex.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Raises an instance to a given non-negative integer exponent.
     * @param n non-negative integer exponent to raise an instance to.
     * @return  new {@link Complex} which is a result of exponentiation.
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree of power must be non-negative.");
        }
        double magnitude = Math.pow(getMagnitude(), n);
        double angle = n*getAngle();

        return Complex.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Calculates all roots of an instance, of given positive integer degree.
     * @param n positive integer degree of roots to be calculated.
     * @return  array of type {@link Complex} which contains all roots of an instance,
     *          of given positive integer degree {@code n}.
     */
    public Complex[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Degree of power must be positive.");
        }
        double magnitude = Math.pow(getMagnitude(), 1.0/n);
        double angle = getAngle() / n;
        double offset = 2*Math.PI / n;

        Complex[] roots = new Complex[n];

        for (int i = 0; i < n; i++) {
            roots[i] = Complex.fromMagnitudeAndAngle(magnitude, angle + offset * i);
        }

        return roots;
    }

    @Override
    public String toString() {
        StringBuilder complexNumberBuilder = new StringBuilder();
        String format = "%.2f";

        if (realPart != 0) {
            complexNumberBuilder.append(String.format(format, realPart));
        }
        if (imaginaryPart != 0) {
            String sign, imaginaryPartString;
            if(realPart != 0) {
                sign = imaginaryPart > 0 ? " + " : " - ";
            } else {
                sign = imaginaryPart > 0 ? "" : "-";
            }
            if (Math.abs(imaginaryPart) == 1) {
                imaginaryPartString = "";
            } else {
                imaginaryPartString = String.format(format, Math.abs(imaginaryPart));
            }
            complexNumberBuilder
                    .append(sign)
                    .append(imaginaryPartString)
                    .append("i");
        }

        if (realPart == 0 && imaginaryPart == 0) {
            return "0";
        }
        return complexNumberBuilder.toString();
    }

    /**
     * Returns module of complex number.
     * @return  module of complex number.
     */
    public double module() {
        return this.getMagnitude();
    }

    /**
     * Multiplies an instance of complex number by supplied complex number.
     * @param multiplier    number to multiply an instance with.
     * @return              new {@link Complex} which is a result of multiplication.
     */
    public Complex multiply(Complex multiplier) {
        return this.mul(multiplier);
    }

    /**
     * Divides an instance of complex number by supplied complex number.
     * @param divisor  number to divide an instance with.
     * @return         new {@link Complex} which is a result of division.
     */
    public Complex divide(Complex divisor) {
        return this.div(divisor);
    }

    /**
     * Returns opposite number of complex number (number multiplied by -1).
     * @return  opposite complex number.
     */
    public Complex negate() {
        return this.mul(ONE_NEG);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  Complex)) {
            return false;
        }
        Complex other = (Complex) obj;
        if (Double.compare(this.realPart, other.realPart) == 0
                && Double.compare(this.imaginaryPart, other.imaginaryPart) == 0) {
            return true;
        }
        return false;
    }


    /**
     * Finds approximation of root of complex function, provided its derivative, using Newton's method.
     * @param function      function for finding roots (zeros).
     * @param derivative    first derivative of the function of interest.
     * @param startingApproximation     starting approximation.
     * @param maxIters                  maximum number of iterations.
     * @param tolerance                 tolerance for two subsequent approximations.
     * @return              approximation of root of given function.
     */
    public static Complex findRoot(Function<Complex, Complex> function, Function<Complex, Complex> derivative,
                                   Complex startingApproximation, int maxIters, double tolerance) {
        Complex rootApprox = new Complex(startingApproximation);

        int numberOfIterations = 0;
        double module;
        do {
            Complex numerator = function.apply(rootApprox);
            Complex denominator = derivative.apply(rootApprox);
            Complex fraction = numerator.divide(denominator);

            Complex oldApprox = new Complex(rootApprox);

            // new approximation
            rootApprox = rootApprox.sub(fraction);
            module = rootApprox.sub(oldApprox).module();

            numberOfIterations++;
        } while(numberOfIterations < maxIters && module > tolerance);

        return rootApprox;
    }
}
