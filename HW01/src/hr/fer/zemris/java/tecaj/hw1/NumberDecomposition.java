package hr.fer.zemris.java.tecaj.hw1;

/**
 * Program NumberDecomposition calculates prime factorization of given integer greater than 1.
 * Input is expected through single command line argument. If number of arguments is different than one, or if entered integer is less than or equal 1, program exits with error.
 * If input argument cannot be parsed as {@link int}, program will crash.
 * 
 * @author Ilijan Kotarac
 *
 */
public class NumberDecomposition {

	/**
	 * The main method for the program NumberDecomposition.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Invalid number of arguments.");
            System.exit(1);
        }

        int number = Integer.parseInt(args[0]);
        
        if(number < 2) {
        	System.out.println("You have entered integer less than or equal to 1. Program expects integer greater than 1.");
            System.exit(1);
        }
        
        int[] primeFactors = primeFactorization(number);
        
        System.out.format("You requested decomposition of number %d onto prime factors. Here they are:%n", number);
        for (int i = 0; i < primeFactors.length; i++) {
        	System.out.format("%d. %d%n", i+1, primeFactors[i]);
        }
    }
	
	/**
	 * Method calculates prime factorization of given integer.
	 * @param number	Integer for which method calculates its prime factorization.
	 * @return			Array of prime factors of input number.
	 */
	public static int[] primeFactorization(int number) {
		int[] primes = calculatePrimes(number);
		int[] primeFactorsTemp = new int[number];
		int ind = 0;
        int counter = 0;
        while (number != 1) {
            if (number % primes[ind] == 0) {
                primeFactorsTemp[counter] = primes[ind];
                counter++;
                number /= primes[ind];
            }
            else {
                ind++;
            }
        }

        int[] primeFactors = new int[counter];
        for(int i = 0; i < counter; i++) {
            primeFactors[i] = primeFactorsTemp[i];
        }

        return primeFactors;
	}

	/**
	 * Method calculates all primes less than or equal to given upper bound.
	 * @param upperBound	Upper bound for calculated prime numbers.
	 * @return				Array of all the prime numbers less than or equal to given upper bound.
	 */
    public static int[] calculatePrimes(int upperBound) {

        int[] primesTemp = new int[upperBound];

        primesTemp[0] = 2;
        int newPrime;

        for(newPrime = 1; newPrime < upperBound; newPrime++) {

            primesTemp[newPrime] = primesTemp[newPrime-1] + 1;

            int previousPrime = 0;
            while(previousPrime < newPrime) {
                if (primesTemp[newPrime] % primesTemp[previousPrime] == 0) {
                    primesTemp[newPrime]++;
                    previousPrime = 0;
                }
                else {
                    previousPrime++;
                }
            }
            if (primesTemp[newPrime] > upperBound) {
            	break;
            }
        }
        
        int numberOfPrimes = newPrime;

        int[] primes = new int[numberOfPrimes];
        for(int i = 0; i < numberOfPrimes; i++) {
            primes[i] = primesTemp[i];
        }

        return primes;
    }

}
