package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program HofstadterQ calculates n-th number in Hofstedter's Q-sequence, for given positive integer n.
 * Input is expected through single command line argument. If number of arguments is different than one, or if entered integer is nonpositive, program exits with error.
 * If input argument cannot be parsed as {@link long}, program will crash.
 * 
 * @author Ilijan Kotarac
 *
 */
public class HofstadterQ {

	/**
	 * The main method for the program HofstadterQ.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
		    System.out.println("Invalid number of arguments.");
		    System.exit(1);
		}
		
		long number = Long.parseLong(args[0]);
		
		if (number <= 0) {
		    System.out.println("Argument must be positive.");
		    System.exit(1);
		}
		
		long qNumber = Hofstadter(number);
		System.out.format("You requested calculation of %d. number of Hofstadter's Q-sequence. The requested number is %d.%n", number, qNumber);
	}
	
	/**
	 * Method calculates n-th number from Hofstadter's Q-sequence, where n is given positive integer.
	 * @param n		Positive integer, index of the number from Hofstadter's Q-sequence which the method returns.
	 * @return		n-th number from Hofstadter's Q-sequence.
	 */
	// Sequence is calculated using recursive formula from this source: http://mathworld.wolfram.com/HofstadtersQ-Sequence.html
	public static long Hofstadter(long n) {
        if(n == 1 || n == 2) {
        	return 1;
        }
        return Hofstadter(n - Hofstadter(n-1)) + Hofstadter(n - Hofstadter(n-2));
    }
}
