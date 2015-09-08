package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program PrimeNumbers calculates first n prime numbers for given positive integer n.
 * Input is expected through single command line argument. If number of arguments is different than one, or if entered integer is nonpositive, program exits with error.
 * If input argument cannot be parsed as {@link int}, program will crash.
 * 
 * @author Ilijan Kotarac
 *
 */
public class PrimeNumbers {

	/**
	 * The main method for the program PrimeNumbers 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Invalid number of arguments.");
            System.exit(1);
        }
        
        int numberOfPrimes = Integer.parseInt(args[0]);
        if(numberOfPrimes < 1) {
        	System.out.println("You have entered nonpositive integer. Program expects positive integer.");
            System.exit(1);
        }
        
        int[] primes = calculatePrimes(numberOfPrimes);
        System.out.format("You requested calculation of %d prime numbers. Here they are:%n", numberOfPrimes);
        for(int i = 0; i < numberOfPrimes; i++) {
            System.out.format("%d. %d%n", i+1, primes[i]);
        }
    }

	/**
	 * Method calculates desired number of the first prime numbers.
	 * @param numberOfPrimes	Number of the first prime numbers to be calculated.
	 * @return					Array of the first numberOfPrimes prime numbers.
	 */
    public static int[] calculatePrimes(int numberOfPrimes) {

        int[] primes = new int[numberOfPrimes];
        primes[0] = 2;

        for(int newPrime = 1; newPrime < numberOfPrimes; newPrime++) {

            primes[newPrime] = primes[newPrime-1] + 1;

            int previousPrime = 0;
            while(previousPrime < newPrime) {
                if (primes[newPrime] % primes[previousPrime] == 0) {
                    primes[newPrime]++;
                    previousPrime = 0;
                }
                else {
                    previousPrime++;
                }
            }
        }

        return primes;
    }

}
