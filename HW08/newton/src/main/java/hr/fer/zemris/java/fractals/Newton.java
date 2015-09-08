package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexParserException;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;
import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.NewtonViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Application calculates fractal images using Newton-Raphson iterations.
 * @author ilijan
 */
public class Newton {
    private static IFractalProducer newtonProducer;

    /**
     * The main method for application {@code Newton}.
     * @param args  Command-line arguments. Not used.
     */
    public static void main(String[] args) {
        System.out.println(
                "Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
                        "Please enter at least two roots, one root per line. Enter 'done' when done.");

        List<Complex> rootsList = getRoots();
        Complex[] roots = new Complex[rootsList.size()];
        roots = rootsList.toArray(roots);

        ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(roots);

        System.out.println("You have entered polynomial: ");
        System.out.println(rootedPolynomial);
        System.out.println("Image of fractal will appear shortly. Thank you.");

        newtonProducer = new NewtonParallelProducer(rootedPolynomial);

        NewtonViewer.main(new String[]{});

    }

    /**
     * Prompts user for input of roots of complex polynomial from standard input.
     * @return  list of complex roots.
     */
    private static List<Complex> getRoots() {
        Scanner sc = new Scanner(System.in);
        List<Complex> roots = new ArrayList<>();

        while (true) {
            System.out.print("Root " + (roots.size() + 1) + ">");
            String input = sc.nextLine();
            if (input.equals("done")) {
                break;
            } else {
                try {
                    roots.add(Complex.parse(input));
                } catch (Exception ex) {
                    System.out.println("Invalid entry. Please repeat entry.");
                    continue;
                }
            }
        }

        if (roots.size() < 2) {
            System.out.println("Please provide at least two roots.");
            getRoots();
        }

        return roots;
    }

    /**
     * Opens window and draws Newton-Raphson iteration-based fractal using
     * parallel calculations.
     */
    public static void show() {
        FractalViewer.show(newtonProducer);
    }

}
