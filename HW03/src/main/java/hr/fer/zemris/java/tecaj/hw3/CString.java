package hr.fer.zemris.java.tecaj.hw3;

/**
 * Class represents unmodifiable strings on which substring methods (and similar) are executed in O(1) complexity.
 * Created by ilijan on 3/31/15.
 */

public final class CString {

    /** {@code char} array holding data. */
    private char[] data;

    /** Beginning of string in internal {@code char} array. */
    private int offset;

    /** Length of string.
     * Can be smaller than internal {@code char} array length. */
    private int length;

    /**
     * Constructor that initializes new {@code CString} from given {@code char} array.
     * @param data      {@code char} array to initialize {@code CString} from.
     * @param offset    beginning index in {@code char} array from which the string should be initialized.
     * @param length    length of string to be initialized.
     */
    public CString(char[] data, int offset, int length) {
        initializeWithCopiedArray(data, offset, length);
    }

    /**
     * Private method for initialization of {@code CString} by copying supplied array.
     * @param data      {@code char} array to initialize {@code CString} from.
     * @param offset    beginning index in {@code char} array from which the string should be initialized.
     * @param length    length of string to be initialized.
     */
    private void initializeWithCopiedArray(char[] data, int offset, int length) {
        if (offset + length > data.length) {
            throw new CStringIndexOutOfBoundsException("Trying to read outside of char Array.");
        }

        char[] newData = new char[length];
        for (int i = 0; i < length; i++) {
            newData[i] = data[i + offset];
        }

        initializeWithSharedArray(newData, 0, length);
    }

    /**
     * Private method for initialization of {@code CString} by using the supplied array.
     * @param data      {@code char} array to initialize {@code CString} from.
     * @param offset    beginning index in {@code char} array from which the string should be initialized.
     * @param length    length of string to be initialized.
     */
    private void initializeWithSharedArray(char[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    /**
     * Private method for initialization of {@code CString} by using the supplied array.
     * Initialized {@code CString} uses whole array for initialization.
     * @param data  {@code char} array to initialize {@code CString} from.
     */
    private void initializeWithSharedArray(char[] data) {
        initializeWithSharedArray(data, 0, data.length);
    }

    /**
     * Empty private constructor used for creation of new {@code CString} objects,
     * which will be initialized in private static factory methods.
     */
    private CString() {

    }

    /**
     * Private factory method that creates new {@code CString} object and initializes it by using the supplied array.
     * @param data      {@code char} array to initialize {@code CString} from.
     * @param offset    beginning index in {@code char} array from which the string should be initialized.
     * @param length    length of string to be initialized.
     * @return          new {@code CString} object.
     */
    private static CString fromSharedArray(char[] data, int offset, int length) {
        CString newCString = new CString();
        newCString.initializeWithSharedArray(data, offset, length);
        return newCString;
    }

    /**
     * Private factory method that creates new {@code CString} object and initializes it by using the supplied array.
     * New {@code CString} uses whole array for initialization.
     * @param data  {@code char} array to initialize {@code CString} from.
     * @return      new {@code CString} object.
     */
    private static CString fromSharedArray(char[] data) {
        return CString.fromSharedArray(data, 0, data.length);
    }

    /**
     * Constructor which initializes {@code CString} using supplied {@code String}.
     * @param string    string to initialize {@code CString} from.
     */
    public CString(String string) {
        length = string.length();
        offset = 0;
        data = new char[length];

        for (int i = 0; i < length; i++) {
            data[i] = string.charAt(i);
        }
    }

    /**
     * Constructor which initializes {@code CString} using supplied {@code CString}.
     * If original internal character array is larger than needed,
     * new instance allocates its own character array of minimal required size and copies data;
     * otherwise it reuses original character array.
     * @param original  original {@code CString} to initialize {@code CString} from.
     */
    public CString(CString original) {
        if (original.length < original.data.length) {
            initializeWithCopiedArray(original.data, 0, original.length);
        } else {
            initializeWithSharedArray(original.data);
        }
    }

    /**
     * Returns the length of {@code CString}.
     * @return  length of {@code CString}.
     */
    public int length() {
        return length;
    }

    /**
     * Returns character in {@code CString} at given index.
     * @param index index of character to be returned.
     * @return  character at given index.
     * @throws CStringIndexOutOfBoundsException if reading outside of range is attempted.
     */
    public char charAt(int index) {
        if(index < 0 || index >= length) {
            throw new CStringIndexOutOfBoundsException(index);
        }
        return data[index];
    }

    /**
     * Converts {@code CString} to character array.
     * @return  character array representing {@code CString}.
     */
    public char[] toCharArray() {
        char[] newCharArray = new char[length];
        for (int i = 0; i < length; i++) {
            newCharArray[i] = data[i + offset];
        }

        return newCharArray;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(data[i + offset]);
        }

        return stringBuilder.toString();
    }

    /**
     * Returns index of the first occurrence of given character
     * or -1 if {@code CString} does not contain given character.
     * @param character character to be found in {@code CString}.
     * @return index of the first occurrence of character.
     */
    public int indexOf(char character) {
        int index = -1;
        for (int i = offset, finalIndex = offset + length; i < finalIndex; i++) {
            if (data[i] == character) {
                index = i;
                break;
            }
        }

        return index;
    }

    /**
     * Checks if {@code CString} starts with given {@code CString}.
     * @param start {@code CString} to check if the instance starts with it.
     * @return      {@code true} if {@code CString} starts with given
     *              {@code CString start}, {@code false} otherwise.
     */
    public boolean startsWith(CString start) {
        return containsAt(0, start);
    }

    /**
     * Checks if {@code CString} ends with given {@code CString}.
     * @param end   {@code CString} to check if the instance ends with it.
     * @return      {@code true} if {@code CString} ends with given
     *              {@code CString end}, {@code false} otherwise.
     */
    public boolean endsWith(CString end) {
        return containsAt(length - end.length, end);
    }

    /**
     * Checks if {@code CString} contains given {@code CString string} at given {@code offset}.
     * @param offset    offset at which method seeks given {@code CString string}.
     * @param string    {@code CString} to be found at given offset.
     * @return          {@code true} if {@code CString} contains given
     *                  {@code CString string}, at given {@code offset},
     *                  {@code false} otherwise.
     */
    private boolean containsAt(int offset, CString string) {
        if (string.length > length - offset) {
            return false;
        }

        for (int i = 0; i < string.length; i++) {
            if (charAt(i + offset) != string.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if {@code CString} contains given {@code CString string}.
     * @param string    {@code CString} to be found inside the instance.
     * @return          {@code true} if {@code CString} contains given
     *                  {@code CString string}, {@code false} otherwise.
     */
    public boolean contains(CString string) {
        for(int i = 0; i < length; i++) {
            if (containsAt(i, string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns new {@code CString} which represents a part of original string.
     * Position {@code endIndex} does not belong to the substring and following
     * constraints should be respected: {@code startIndex>=0}, {@code endIndex >= startIndex}.
     * @param startIndex    starting index from which substring will be taken.
     * @param endIndex      ending index (not contained in substring)
     *                      until which substring will be taken.
     * @return              new {@code CString} which represents a part of original string.
     * @throws CStringIndexOutOfBoundsException if starting index is negative, or ending index larger than length.
     * @throws IllegalArgumentException if ending index is smaller than starting index.
     */
    public CString substring(int startIndex, int endIndex) {
        if (startIndex < 0) {
            throw new CStringIndexOutOfBoundsException("Starting index is negative.");
        }
        if (endIndex > length) {
            throw new CStringIndexOutOfBoundsException("Ending index is larger than length of the string.");
        }
        if (endIndex < startIndex) {
            throw new IllegalArgumentException("Ending index is smaller than starting index.");
        }

        return CString.fromSharedArray(data, startIndex, endIndex - startIndex);
    }

    /**
     * Returns new {@code CString} which represents leftmost {@code n} characters of the instance.
     * @param n     number of characters to take from the instance in a substring.
     * @return      new {@code CString} which represents left part of the instance.
     * @throws  IllegalArgumentException    if number of characters is negative or larger than length.
     */
    public CString left(int n) {
        checkNumberOfCharacters(n);
        return substring(0, n);
    }

    /**
     * Returns new {@code CString} which represents rightmost {@code n} characters of the instance.
     * @param n     number of characters to take from the instance in a substring.
     * @return      new {@code CString} which represents right part of the instance.
     * @throws  IllegalArgumentException    if number of characters is negative or larger than length.
     */
    public CString right(int n) {
        checkNumberOfCharacters(n);
        return substring(length - n, length);
    }

    /**
     * Checks that number of characters to be taken from {@code CString} in a substring is valid.
     * @param n number of characters to take in substring.
     * @throws  IllegalArgumentException    if number of characters is negative or larger than length.
     */
    private void checkNumberOfCharacters(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of characters is negative.");
        }

        if (n > length) {
            throw new IllegalArgumentException("Number of characters larger than length.");
        }
    }

    /**
     * Creates a new {@code CString} which is concatenation of current and given string.
     * @param string    string to concatenate to the current {@code CString}.
     * @return          new {@code CString} which is concatenation of current and given string.
     */
    public CString add(CString string) {
        char[] newData = new char[length + string.length];

        for (int i = 0; i < length; i++) {
            newData[i] = charAt(i);
        }

        for (int i = 0; i < string.length; i++) {
            newData[i + length] = string.data[i];
        }

        return CString.fromSharedArray(newData);
    }

    /**
     * Creates a new CString in which each occurrence of old character is replaced with new character.
     * @param oldChar   character to be replaced.
     * @param newChar   replacing character.
     * @return          new {@code CString} in which each occurrence of old character is replaced with new character.
     */
    public CString replaceAll(char oldChar, char newChar) {
        char[] newData = new char[length];
        for (int i = 0; i < length; i++) {
            if (data[i] ==  oldChar) {
                newData[i] = newChar;
            } else {
                newData[i] = data[i];
            }
        }

        return CString.fromSharedArray(newData);
    }

    /**
     * Creates a new CString in which each occurrence of old {@code CString} is replaced with new {@code CString}.
     * @param oldStr    {@code CString} to be replaced.
     * @param newStr    replacing {@code CString}.
     * @return          new {@code CString} in which each occurrence of old {@code CString}
     *                  is replaced with new {@code CString}.
     */
    public CString replaceAll(CString oldStr, CString newStr) {
        int numberOfOccurences = 0;

        for (int i = 0; i < length; i++) {
            if(containsAt(i, oldStr)) {
                numberOfOccurences++;
            }
        }

        int newLength = length + (newStr.length - oldStr.length)*numberOfOccurences;
        char[] newData = new char[newLength];

        int positionInOldData = 0;
        int positionInNewData = 0;

        while(positionInNewData < newLength) {
            if (containsAt(positionInOldData, oldStr)) {
                for (int positionInNewString = 0; positionInNewString < newStr.length; positionInNewString++) {
                    newData[positionInNewData] = newStr.data[positionInNewString];
                    positionInNewData++;
                }
                positionInOldData += oldStr.length;
            } else {
                newData[positionInNewData] = data[positionInOldData];
                positionInNewData++;
                positionInOldData++;
            }
        }

        return CString.fromSharedArray(newData);
    }
}
