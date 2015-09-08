package hr.fer.zemris.java.tecaj.hw3;

/**
 * Class implements parser for objects of class {@link ComplexNumber}.
 * Created by ilijan on 3/30/15.
 */
public final class ComplexNumberParser {

    /** String to be parsed */
    private String string;

    /** Current position in string. */
    private int currentPosition;

    /** Special characters that represent signs. */
    private static final String SIGNS = "+-";

    /** Special character that represents imaginary unit.
     * Declared as {@code String} for convenience. */
    private static final String IMAGINARY_UNIT = "i";

    /** String represent all special characters in parsing complex numbers. */
    private static final String SPECIAL_CHARACTERS = "+-i";

    /**
     * Constructor initializes new {@code ComplexNumberParser} using string to be parsed as input.
     * @param string    string to be parsed.
     */
    public ComplexNumberParser(String string) {
        this.string = string;
    }

    /**
     * Method that parses complex number from stored string.
     * @return  complex number of type {@link ComplexNumber} parsed from string
     *          input by constructor {code ComplexNumberParser(String string)}.
     */
    public ComplexNumber getComplexNumber() {
        skipSpaces();
        if (isEndOfString()) {
            throw new IllegalArgumentException("Trying to parse empty string.");
        }
        if (string.equals(IMAGINARY_UNIT)) {
            return new ComplexNumber(0.0, 1.0);
        }
        return parse();
    }

    /**
     * Returns current character in string, declared as string for convenience.
     * @return  current character.
     * @throws  ComplexNumberParserException    if reading is attempted after the end of string is reached.
     */
    private String currentCharacter() {
        if (isEndOfString()) {
            throw new ComplexNumberParserException("Trying to read after the end of string.");
        }
        return string.substring(currentPosition, currentPosition + 1);
    }

    /**
     * Returns previous character in string, declared as string for convenience.
     * @return  previous character in string.
     * @throws  ComplexNumberParserException    if reading is attempted before the beginning of string
     *                                          (if current position in string is <= 0).
     */
    private String previousCharacter() {
        if (!hasPrevious()) {
            throw new ComplexNumberParserException("Trying to read before the beginning of string.");
        }
        return string.substring(currentPosition - 1, currentPosition);
    }

    /**
     * Checks if end of string is reached.
     * @return  {@code true} if end of string is reached, {@code false} otherwise.
     */
    private boolean isEndOfString() {
        return currentPosition == string.length();
    }

    /**
     * Checks if string has character at previous position.
     * @return  {@code true} if end of string has previous character, {@code false} otherwise.
     */
    private boolean hasPrevious() {
        return currentPosition > 0;
    }

    /**
     * Skips one position in string.
     */
    private void skipOne() {
        currentPosition++;
    }

    /**
     * Skips all spaces in string, as defined by private constant {@code spaces}.
     */
    private void skipSpaces() {
        while(!isEndOfString()) {
            if(currentCharacter().equals(" ")) {
                skipOne();
            } else {
                break;
            }
        }
    }

    /**
     * Reads one character from string, and moves one position forward.
     * @return  current character in string.
     */
    private String read() {
        String currentCharacter = currentCharacter();
        currentPosition++;

        return currentCharacter;
    }

    /**
     * Parses string to {@link ComplexNumber}.
     * @return complex number parsed from string.
     */
    private ComplexNumber parse() {
        double realPart = 0;
        double imaginaryPart = 0;

        boolean hasReal = false;
        boolean hasImaginary = false;

        while (!isEndOfString()) {
            double nextNumber = getNextNumber();
            if (!isEndOfString() && currentCharacter().equals(IMAGINARY_UNIT)) {
                if (hasImaginary) {
                    throw new ComplexNumberParserException("Two imaginary parts supplied.");
                } else {
                    imaginaryPart = nextNumber;
                    hasImaginary = true;
                    skipOne();
                }
            } else {
                if (hasReal) {
                    throw new ComplexNumberParserException("Two real parts supplied.");
                } else {
                    realPart = nextNumber;
                    hasReal = true;
                }
            }
            skipSpaces();
        }

        return new ComplexNumber(realPart, imaginaryPart);
    }

    /**
     * Method reads string until reaches special character or end of string,
     * and gets next real number from string.
     * @return  next real number read from string.
     */
    private double getNextNumber() {
        StringBuilder nextNumberBuilder = new StringBuilder();
        skipSpaces();
        if(SIGNS.contains(currentCharacter())) {
            nextNumberBuilder.append(read());
        }
        skipSpaces();
        while (!isEndOfString()) {
            if (SPECIAL_CHARACTERS.contains(currentCharacter()) &&
                    !(SIGNS.contains(currentCharacter()) && hasPrevious() && previousCharacter().toUpperCase().equals("E"))) {
                break;
            }
            nextNumberBuilder.append(read());
        }
        if (!isEndOfString() && currentCharacter().equals(IMAGINARY_UNIT) && SIGNS.contains(nextNumberBuilder.toString())) {
            nextNumberBuilder.append("1");
        }
        return Double.parseDouble(nextNumberBuilder.toString());
    }
}
