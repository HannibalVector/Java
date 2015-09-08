package hr.fer.zemris.java.tecaj.hw1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Program calculates area and perimeter of a rectangle of given width and height.
 * Width and height can be supplied as command line arguments. Program expects two nonnegative real numbers. If invalid number of arguments is entered, or if any of the entered numbers are negative, program terminates with error.
 * If no arguments are passed from command line, program prompts user to enter width and height from standard input. Program assumes entry of string which can be parsed as {@link double} or it will crash.
 * 
 * @author Ilijan Kotarac
 * @version 1.0
 *
 */
public class Rectangle {

	/**
	 * The main method for the program Rectangle.
	 * @param args	Command line arguments.
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        double width = 0;
        double height = 0;

        if (args.length == 2) {

            width = Double.parseDouble(args[0]);
            height = Double.parseDouble(args[1]);
            if(width < 0 || height < 0) {
            	System.out.println("Arguments must be positive.");
            	System.exit(1);
            }
        }
        else if (args.length > 0) {

            System.out.println("Invalid number of arguments was provided.");
            System.exit(1);
        }
        else {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new BufferedInputStream(System.in))
            );

            width = inputFeature(reader, "width");
            height = inputFeature(reader, "height");
        }

        double area = width*height;
        double perimeter = 2*(width + height);
        System.out.format(
                "You have specified a rectangle with width %.1f and height %.1f. Its area is %.2f and its perimeter is %.1f.",
                width, height, area, perimeter
        );
    }

    /**
     * The method prompts user to enter feature of given name and reads a number from given character-input stream.
     * @param reader		Reader for reading text from a character-input stream.
     * @param featureName	Name of the feature that will be printed on standard output when input is expected.
     * @return				Number extracted from line read from character-input stream.
     * @throws IOException
     */
    public static double inputFeature(BufferedReader reader, String featureName) throws IOException {

        double feature;

        System.out.print("Please provide " + featureName + ": ");

        String trimmedLine = reader.readLine().trim();
        if(trimmedLine.isEmpty()) {
            System.out.println("Nothing was given.");
            feature = inputFeature(reader, featureName);
        }
        else {
            feature = Double.parseDouble(trimmedLine);
            if(feature < 0.0) {
                String featureNameCapitalized =  Character.toUpperCase(featureName.charAt(0)) + featureName.substring(1);
                System.out.println(featureNameCapitalized + " is negative.");
                feature = inputFeature(reader, featureName);
            }
        }

        return feature;
    }
}