package hr.fer.zemris.java.tecaj.hw3.demo;

import hr.fer.zemris.java.tecaj.hw3.ComplexNumber;

/**
 * Demo program for class {@link ComplexNumber}.
 * Created by ilijan on 3/31/15.
 */
public class ComplexNumberDemo {
    public static void main(String[] args) {
        String s = "  2i - 17.5e-3";
        ComplexNumber complex = ComplexNumber.parse(s);
        System.out.println("z = " + complex.toString());
        System.out.println("Re z = " + complex.getReal());
        System.out.println("Im z = " + complex.getImaginary());

        ComplexNumber newComplex = ComplexNumber.parse(complex.toString());
        System.out.println("reparsed: " + newComplex);

        System.out.println("\nSome operation...");
        ComplexNumber c1 = new ComplexNumber(2, 3);
        ComplexNumber c2 = ComplexNumber.parse("2.5-2i ");
        ComplexNumber c3 = c1.add(ComplexNumber.fromMagnitudeAndAngle(2, 1.57))
                .div(c2).power(3).root(2)[1];
        System.out.println(c3);

        ComplexNumber z1 = ComplexNumber.parse("2.2e2i + 10");
        System.out.println(z1);
    }
}
