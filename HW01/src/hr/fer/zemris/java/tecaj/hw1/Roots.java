package hr.fer.zemris.java.tecaj.hw1;

import java.text.DecimalFormat;

/**
 * Program Roots calculates all roots of given complex number, of given degree.
 * Input is expected through three command line arguments. 
 * First and second argument are real and imaginary part of complex number for which the program calculates its roots.
 * Third argument is degree of roots (integer greater than 1). 
 * If number of arguments is different than three, or if entered degree is integer less than or equal 1, program exits with error.
 * If first two arguments cannot be parsed as {@link double}, or if third argument cannot be parsed as {@link int} program will crash.
 * 
 * @author Ilijan Kotarac
 * @version 1.0
 */
public class Roots {
	
	/**
	 * The main method for the program Roots.
	 * @param args	Command line arguments.
	 * 
	 */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Invalid number of arguments.");
            System.exit(1);
        }

        double realPart = Double.parseDouble(args[0]);
        double imaginaryPart = Double.parseDouble(args[1]);
        int degree = Integer.parseInt(args[2]);
        
        if(degree < 2) {
        	System.out.println("You have entered integer less than or equal to 1. Program expects integer greater than 1.");
            System.exit(1);
        }
        
        Complex[] roots = complexRoots(realPart, imaginaryPart, degree);

        System.out.format("You requested calculation of  %d. roots. Solutions are:%n", degree);
        printComplexArray(roots);
    }
    
    /**
     * Method prints array of complex numbers (of type {@link Complex}) on standard output, using standard mathematical formatting.
     * @param array		Array of complex numbers to be printed.
     * @see				DecimalFormat
     */
    public static void printComplexArray(Complex[] array) {
    	DecimalFormat realPartFormatter = new DecimalFormat("0.##");
        DecimalFormat imaginaryPartFormatter = new DecimalFormat("+ 0.##i;- 0.##i");
        
        for (int i = 0; i < array.length; i++) {
            System.out.format("%d) %s %s%n", i + 1, realPartFormatter.format(array[i].real), imaginaryPartFormatter.format(array[i].imaginary));
        }
    }

    /**
     * Class implements "structure" for storing complex number with fields for real and imaginary part.
     * @author Ilijan Kotarac
     *
     */
    static class Complex {
        double real;
        double imaginary;
    }

    /**
     * Method calculates all roots of given complex number of given integer degree.
     * @param realPart			Real part of complex number whose roots are returned by method.
     * @param imaginaryPart		Imaginary part of complex number whose roots are returned by method.
     * @param degree			Degree of roots which are to be calculated.
     * @return					Array of all complex roots of given degree of given complex number.
     */
    public static Complex[] complexRoots(double realPart, double imaginaryPart, int degree) {

        double r = getRadius(realPart, imaginaryPart);
        double phi = getArgument(realPart, imaginaryPart);

        Complex[] roots = new Complex[degree];
        double amplitude = amplitudeOfRoot(degree, r);

        for (int i = 0; i < degree; i++) {
            double argument = argumentOfRoot(degree, phi, i);
            roots[i] = new Complex();
            roots[i].real = amplitude*Math.cos(argument);
            roots[i].imaginary = amplitude*Math.sin(argument);
        }

        return roots;
    }

    private static double argumentOfRoot(int degree, double phi, int i) {
        return (phi + 2*Math.PI*i)/degree;
    }

    private static double amplitudeOfRoot(int degree, double r) {
        return Math.pow(r, 1.0/degree);
    }

    private static double getArgument(double realPart, double imaginaryPart) {
        return Math.atan2(imaginaryPart, realPart);
    }

    private static double getRadius(double realPart, double imaginaryPart) {
        return Math.sqrt(realPart * realPart + imaginaryPart * imaginaryPart);
    }
}