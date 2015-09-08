package hr.fer.zemris.java.tecaj.hw3;

/**
 * Class implements support for working with complex numbers. Class {@code ComplexNumber} represents
 * unmodifiable complex number.
 * Created by ilijan on 3/30/15.
 */
public final class ComplexNumber {

    /** Real part of complex number. */
    private double realPart;

    /** Imaginary part of complex number. */
    private double imaginaryPart;

    /**
     * Constructor that initializes complex number using its real and imaginary part.
     * @param realPart          real part of complex number.
     * @param imaginaryPart     imaginary part of complex number.
     */
    public ComplexNumber(double realPart, double imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    /**
     * Static factory method which creates new {@link ComplexNumber} object only from its real part. Imaginary part is assumed to be zero.
     * @param real  real part of complex number.
     * @return      new {@link ComplexNumber} with supplied real part.
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Static factory method which creates new {@link ComplexNumber} object only from its imaginary part. Real part is assumed to be zero.
     * @param imaginary     imaginary part of complex number.
     * @return              new {@link ComplexNumber} with supplied imaginary part.
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Static factory method which creates new {@link ComplexNumber} object from its magnitude and angle (argument).
     * @param magnitude magnitude of complex number to be created.
     * @param angle     angle (argument) of complex number to be created.
     * @return          new {@link ComplexNumber} with supplied magnitude and angle.
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        double real = magnitude * Math.cos(angle);
        double imaginary = magnitude * Math.sin(angle);

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Static factory method which creates new {@link ComplexNumber} by parsing it from given string.
     * Examples of input: "i", "-5 + 4i", " 5e-3i - 7.12", "-i", "5".
     * @param string    string to be parsed as {@link ComplexNumber}.
     * @return          new {@link ComplexNumber} parsed from string.
     * @throws  IllegalArgumentException        if empty string is passed to method.
     * @throws  ComplexNumberParserException    if string contains multiple real or imaginary parts.
     * @throws  NumberFormatException if real or imaginary part cannot be parsed as {@code double}.
     */
    public static ComplexNumber parse (String string) {
        ComplexNumberParser parser = new ComplexNumberParser(string);
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
        return Math.sqrt(realPart*realPart + imaginaryPart*imaginaryPart);
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
     * @return              new {@link ComplexNumber} which is a result of addition.
     */
    public ComplexNumber add(ComplexNumber numberToAdd) {
        double real = realPart + numberToAdd.getReal();
        double imaginary = imaginaryPart + numberToAdd.getImaginary();

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Subtracts complex number from an instance of complex number.
     * @param numberToSubtract  number to subtract from an instance.
     * @return                  new {@link ComplexNumber} which is a result of subtraction.
     */
    public ComplexNumber sub(ComplexNumber numberToSubtract) {
        double real = realPart - numberToSubtract.getReal();
        double imaginary = imaginaryPart - numberToSubtract.getImaginary();

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Multiplies an instance of complex number by supplied complex number.
     * @param multiplier    number to multiply an instance with.
     * @return              new {@link ComplexNumber} which is a result of multiplication.
     */
    public ComplexNumber mul(ComplexNumber multiplier) {
        double magnitude = getMagnitude() * multiplier.getMagnitude();
        double angle = getAngle() + multiplier.getAngle();

        return ComplexNumber.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Divides an instance of complex number by supplied complex number.
     * @param divisor  number to divide an instance with.
     * @return         new {@link ComplexNumber} which is a result of division.
     */
    public ComplexNumber div(ComplexNumber divisor) {
        if (divisor.getMagnitude() == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        double magnitude = getMagnitude() / divisor.getMagnitude();
        double angle = getAngle() - divisor.getAngle();

        return ComplexNumber.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Raises an instance to a given non-negative integer exponent.
     * @param n non-negative integer exponent to raise an instance to.
     * @return  new {@link ComplexNumber} which is a result of exponentiation.
     */
    public ComplexNumber power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Degree of power must be non-negative.");
        }
        double magnitude = Math.pow(getMagnitude(), n);
        double angle = n*getAngle();

        return ComplexNumber.fromMagnitudeAndAngle(magnitude, angle);
    }

    /**
     * Calculates all roots of an instance, of given positive integer degree.
     * @param n positive integer degree of roots to be calculated.
     * @return  array of type {@link ComplexNumber} which contains all roots of an instance,
     *          of given positive integer degree {@code n}.
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Degree of power must be positive.");
        }
        double magnitude = Math.pow(getMagnitude(), 1.0/n);
        double angle = getAngle() / n;
        double offset = 2*Math.PI / n;

        ComplexNumber[] roots = new ComplexNumber[n];

        for (int i = 0; i < n; i++) {
            roots[i] = ComplexNumber.fromMagnitudeAndAngle(magnitude, angle + offset*i);
        }

        return roots;
    }

    @Override
    public String toString() {
        StringBuilder complexNumberBuilder = new StringBuilder();
        if (realPart != 0) {
            complexNumberBuilder.append(realPart);
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
                imaginaryPartString = String.valueOf(Math.abs(imaginaryPart));
            }
            complexNumberBuilder
                    .append(sign)
                    .append(imaginaryPartString)
                    .append("i");
        }
        return complexNumberBuilder.toString();
    }
}
