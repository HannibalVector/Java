package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Program evaluates mathematical expression in postfix representation.
 * Expression is passed as a single command line argument of type {@code String}.
 * Numbers and operators should be separated by one or more spaces.
 * Supported operators are "+", "-", "/", "*" and "%".
 * 
 * @author ilijan
 *
 */
public class StackDemo {

	/**
	 * The main method for the program StackDemo.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments are passed to program.");
			System.exit(1);
		}
		
		String[] arguments = args[0].split("\\s+");
		
		try {
			int result = calculate(arguments);
			System.out.format("Expression evaluates to %d.%n", result);
		} catch (IllegalArgumentException illegalArgument) {
			System.out.println(illegalArgument.getMessage());
			System.exit(1);
		} catch (EmptyStackException emptyStack) {
			System.out.println(emptyStack.getMessage());
			System.out.println("Invalid number of arguments were passed.");
			System.exit(1);
		} catch (ArithmeticException arithmeticException) {
			System.out.println(arithmeticException.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Evaluates expression passed as an array of strings.
	 * @param arguments Array of type {@code String} which contains numbers and operators in postfix representation.
	 * @return		    Integer to which expression evaluates.
	 */
	private static int calculate(String[] arguments) {
		ObjectStack numbers = new ObjectStack();
		
		for (String argument : arguments) {
			if(isInteger(argument)) {
				numbers.push(Integer.parseInt(argument));
			} else {
				numbers.push(performOperation(numbers, argument));
			}
		}
		if (numbers.size() != 1) {
			throw new IllegalArgumentException("Invalid number of arguments were passed.");
		}
		
		return (int) numbers.peek();
	}
	
	/**
	 * Checks if passed {@code String string} can be parsed as {@code int}.
	 * @param string	{@code String} to be checked.
	 * @return			{@code true} if {@code string} can be parsed as
	 * 					{@code int}, otherwise {@code false}.
	 */
	private static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Performs operation using operator extracted from {@code String argument}
	 * on two last numbers from {@code ObjectStack numbers}.
	 * @param numbers	{@link ObjectStack} stack from which two numbers are popped.
	 * @param operator	{@code String} which represents operator to be applied.
	 * @return			Result of operation using {@code operator} on last two numbers in {@code ObjectStack numbers}.
	 */
	private static int performOperation(ObjectStack numbers, String operator) {
		int operand2 = (int) numbers.pop();
		int operand1 = (int) numbers.pop();
		
		switch (operator) {
			case "+":
				return operand1 + operand2;
			case "-":
				return operand1 - operand2;
			case "/":
				checkZeroDivision(operand2);
				return operand1 / operand2;
			case "*":
				return operand1 * operand2;
			case "%":
				checkZeroDivision(operand2);
				return operand1 % operand2;
			default:
				throw new IllegalArgumentException("Invalid operator.");
		}
	}
	
	/**
	 * If division by zero is attempted throws {@code ArithmeticException}.
	 * @param operand	Operand which, if zero, triggers {@code ArithmeticException}.
	 */
	private static void checkZeroDivision(int operand) {
		if (operand == 0) {
			throw new ArithmeticException("Division by zero.");
		}
	}
}
