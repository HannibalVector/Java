package hr.fer.zemris.java.tecaj.hw3;

/**
 * Created by ilijan on 3/31/15.
 */
public class CStringIndexOutOfBoundsException extends IndexOutOfBoundsException {

    public CStringIndexOutOfBoundsException() {
    }

    public CStringIndexOutOfBoundsException(String s) {
        super(s);
    }

    public CStringIndexOutOfBoundsException(int index) {
        super(String.valueOf(index));
    }
}
