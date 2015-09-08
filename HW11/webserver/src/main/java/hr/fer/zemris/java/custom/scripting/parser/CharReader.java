package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Class for reading characters from given string. Class encapsulates string, current position in string
 * and provides methods for getting consecutive characters from string.
 */
public class CharReader {
    /** String that reader scans. */
    private String string;

    /** Current position in string. */
    private int currentPosition;

    /** String contains characters which can be escaped when seeking through string. */
    private String escapedCharacters;

    /** String contains characters regarded as whitespace. */
    private final String spaces = " \t\r\n";

    /** Escape character. */
    private final char escape = '\\';

    /**
     * Constructor takes string to be scanned through. By default no characters are treated as escaped.
     * @param string    String to be scanned through by {@code CharReader}.
     */
    public CharReader(String string) {
        this.string = string;
        escapedCharacters = "";
    }

    /**
     * Sets characters to be eascaped if reader finds them after the escape ({@code \}) character.
     * @param escapedCharacters String of characters that can be escaped.
     */
    public void setEscapedCharacters(String escapedCharacters) {
        this.escapedCharacters = escapedCharacters;
    }

    /**
     * Checks if string has next character (one position furher of current position).
     * @return  {@code true} if string has next character, {@code false} otherwise.
     */
    public boolean hasNext() {
        return currentPosition < string.length() - 1;
    }

    /**
     * Checks if string has character in next {@code numberOfCharacters} positions.
     * @param numberOfCharacters Number of positions which method checks if they are available.
     * @return {@code true} if string has next characters, {@code false} otherwise.
     */
    public boolean hasNext(int numberOfCharacters) {
        return currentPosition < string.length() - numberOfCharacters;
    }

    /**
     * Checks if current position points to the end of the string.
     * @return  {@code true} if the end of the string is reached, {@code false} otherwise.
     */
    public boolean isEndOfString() {
        return currentPosition == string.length();
    }

    /**
     * Current character in string. If the end of the string is reached throws {@link CharReaderException}.
     * @return  Current character in string.
     */
    public char current() {
        if(isEndOfString()) {
            throw new CharReaderException("Trying to read after the end of string.");
        }
        return string.charAt(currentPosition);
    }

    /**
     * Next character in string. If string does not have next character throws {@link CharReaderException}.
     * @return  Next character in string.
     */
    public char nextChar() {
        if(!hasNext()) {
            throw new CharReaderException("Trying to read after the end of string.");
        }
        return string.charAt(currentPosition + 1);
    }

    /**
     * Reads one character from string and moves one position forward.
     * @return Character read from string.
     */
    public char read() {
        char currentChar = current();
        currentPosition++;
        return currentChar;
    }

    /**
     * Current {@code numberOfCharacters} characters in string.
     * If the end of the string is reached throws {@link CharReaderException}.
     * @return  Current {@code numberOfCharacters} characters in string.
     */
    public String current(int numberOfCharacters) {
        if(!hasNext(numberOfCharacters-1)) {
            throw new CharReaderException("Trying to read after the end of string.");
        }
        return string.substring(currentPosition, currentPosition + numberOfCharacters);
    }

    /**
     * Reads {@code numberOfCharacters} characters from string and moves {@code numberOfCharacters}
     * positions forward.
     * If the end of the string is reached throws {@link CharReaderException}.
     * @return Characters read from string.
     */
    public String read(int numberOfCharacters) {
        if(!hasNext(numberOfCharacters-1)) {
            throw new CharReaderException("Trying to read after the end of string.");
        }
        String currentString = string.substring(currentPosition, currentPosition + numberOfCharacters);
        currentPosition += numberOfCharacters;
        return currentString;
    }

    /**
     * Reads until it reaches {@code delimiter} and if does not find it throws {@link CharReaderException}.
     * Characters set as escaped by method {@code setEscapedCharacters(String escapedCharacters)} are ignored
     * if they appear after escape character ({@code \}), even if they are part of delimiter.
     * @param delimiter Delimiter until which reader reads characters.
     * @return          String of characters read until the delimiter is reached.
     */
    public String readCarefullyUntil(String delimiter) {
        return readUntil(delimiter, true);
    }

    /**
     * Reads until reaches {@code delimiter} or the end of the string.
     * Characters set as escaped by method {@code setEscapedCharacters(String escapedCharacters)} are ignored
     * if they appear after escape character ({@code \}), even if they are part of delimiter.
     * @param delimiter Delimiter until which reader reads characters.
     * @return          String of characters read until the delimiter or end of string is reached.
     */
    public String readUntil(String delimiter) {
        return readUntil(delimiter, false);
    }


    /**
     * General method from which {@code readUntil()} and {@code readCarefullyUntil()} are derived.
     * Characters set as escaped by method {@code setEscapedCharacters(String escapedCharacters)} are ignored
     * if they appear after escape character ({@code \}), even if they are part of delimiter.
     * @param delimiter         Delimiter until which reader reads characters.
     * @param throwException    Wheather the {@code CharReaderException} should be thrown if delimiter is not found.
     * @return                  String of characters read until the delimiter or the end of string is reached.
     */
    private String readUntil(String delimiter, boolean throwException) {

        int delimiterLength = delimiter.length();
        StringBuilder nextStringBuilder = new StringBuilder();

        boolean isCurrentCharEscaped = false;
        boolean isDelimiterReached = false;

        while(!isEndOfString()) {
            isDelimiterReached = hasNext(delimiterLength-1) &&
                    (current(delimiterLength).equals(delimiter)) &&
                    !isCurrentCharEscaped;

            if(isDelimiterReached) {
                break;
            }

            if (hasNext()
                    && current() == escape
                    && !isCurrentCharEscaped
                    && escapedCharacters.contains(String.valueOf(nextChar()))) {

                skip();
                isCurrentCharEscaped = true;
                continue;
            }
            nextStringBuilder.append(read());
            isCurrentCharEscaped = false;
        }

        if (!isDelimiterReached && throwException) {
            throw new CharReaderException("Delimiter not reached before end of string!");
        }

        return nextStringBuilder.toString();
    }

    /**
     * Skips one place.
     */
    public void skip() {
        currentPosition++;
    }

    /**
     * Skips {@code numberOfPlaces} places.
     * @param numberOfPlaces    Number of places to be skipped.
     */
    public void skip(int numberOfPlaces) {
        currentPosition += numberOfPlaces;
    }

    /**
     * Skip all spaces defined by constant string {@code spaces}.
     */
    public void skipSpaces() {
        while(!isEndOfString()) {
            if (spaces.contains(String.valueOf(current()))) {
                currentPosition++;
            } else {
                break;
            }
        }
    }
}
