package hr.fer.zemris.java.fractals.demo;

import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.complex.Complex;
import sun.java2d.pipe.SpanIterator;

import java.util.function.Function;

/**
 * @author ilijan
 */
public class Polynomials {
    public static void main(String[] args) {
        //ComplexPolynomial p1 = new ComplexPolynomial(new Complex(0, -1));
        //ComplexPolynomial p2 = new ComplexPolynomial(new Complex(1, -2), new Complex(1, 1), new Complex(0, 1));
        /*System.out.println(p3);
        System.out.println(p4);
        System.out.println(p4.apply(new Complex(0, -1)));


        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p1.multiply(p2));
        System.out.println(p1.derive());
        System.out.println(p1.order());

        */


        ComplexRootedPolynomial p3 = new ComplexRootedPolynomial(new Complex(1, -2), new Complex(4, 1), new Complex(0, 1));
        System.out.println("Rooted polynomial:");
        System.out.println(p3);

        ComplexPolynomial p4 = p3.toComplexPolynom();
        System.out.println("Complex polynomial:");
        System.out.println(p4);
        System.out.println("Derivative:");
        System.out.println(p4.derive());

        Complex z = new Complex(4, 1.1);
        System.out.print("closest index of " + z + " is ");
        System.out.println(p3.indexOfClosestRootFor(z, 1.0));


        Complex approx = Complex.findRoot(w -> p3.apply(w), w -> p4.derive().apply(w), z, 10, 1.0e-6);
        System.out.println("Aproksimacija je :" + approx);
        System.out.print("closest index of " + approx + " is ");
        System.out.println(p3.indexOfClosestRootFor(approx, 1.0));

        Function<Complex, Complex> primijena = w -> p4.apply(w);
        System.out.println(primijena.apply(z));
        System.out.println(p4.apply(z));
    }
}
