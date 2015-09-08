package hr.fer.zemris.linearna;


/**
 * Implements vector whose elements are real numbers.
 *
 * @author ilijan
 */
public class Vector extends AbstractVector {
    /** Vector elements. */
    private double[] elements;
    /** Vector dimension. */
    private int dimension;
    /** Indicates immutability of vector. */
    private boolean readOnly;

    /**
     * Constructor initializes new vector to given components.
     * @param elements  components of new vector.
     */
    public Vector(double ... elements) {
        this(true, false, elements);
    }

    /**
     * Constructor initializes new vector to given dimension, using
     * empty elements array. New vector is mutable.
     * @param dimension dimension of new vector.
     */
    public Vector(int dimension) {
        this.dimension = dimension;
        elements = new double[dimension];
        readOnly = false;
    }

    /**
     * Constructor initializes new vector, and sets given immutability flag,
     * using provided array which can be referenced or copied.
     * @param readOnly  determines if vector will be read-only object.
     * @param useProvidedArray  determines if provided array will be referenced or copied.
     * @param elements  array of elements.
     */
    public Vector(boolean readOnly, boolean useProvidedArray, double[] elements) {
        this.readOnly = readOnly;
        this.dimension = elements.length;
        if (useProvidedArray) {
            this.elements = elements;
        } else {
            this.elements = new double[dimension];
            System.arraycopy(elements, 0, this.elements, 0, elements.length);
        }
    }

    @Override
    public IVector newInstance(int i) {
        return new Vector(i);
    }

    /**
     * Static factory method for creating new modifiable vector.
     * @param elements  vector elements.
     * @return  new modifiable vector.
     */
    public static IVector newModifiable(double ... elements) {
        return new Vector(false, false, elements);
    }

    /**
     * Static factory method for creating new immutable vector.
     * @param elements vector elements.
     * @return new immutable vector.
     */
    public static IVector newUnmodifiable(double ... elements) {
        return new Vector(true, false, elements);
    }

    @Override
    public double get(int i) {
        return elements[i];
    }

    @Override
    public IVector set(int i, double v) throws UnmodifiableObjectException {
        if (readOnly) {
            throw new UnmodifiableObjectException();
        }
        elements[i] = v;
        return this;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public IVector copy() {
        return new Vector(false, false, elements);
    }

    /**
     * Parses vector from given string.
     * @param string    string to parse new vector from.
     * @return  parsed vector.
     */
    public static IVector parseSimple(String string) {
        String[] elementStrings = string.trim().split(" +");
        double[] elements = new double[elementStrings.length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = Double.parseDouble(elementStrings[i]);
        }
        return new Vector(true, true, elements);
    }
}
