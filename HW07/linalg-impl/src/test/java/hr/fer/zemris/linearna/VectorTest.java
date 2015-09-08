package hr.fer.zemris.linearna;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for classes implementing vectors.
 *
 * @author ilijan
 */
public class VectorTest {
    private static double EPSILON = 1.0e-14;

    @Test
    public void testDimension() {
        IVector v = new Vector(1, 2, 3);
        assertEquals(3, v.getDimension());
    }

    @Test
    public void testGet() {
        IVector v = new Vector(1, 2, 3);
        assertEquals(2.0, v.get(1), EPSILON);
    }

    @Test
    public void testSet() {
        IVector v = Vector.newModifiable(1, 2, 3);
        v.set(0, 100.0);
        assertEquals(100.0, v.get(0), EPSILON);
    }

    @Test
    public void testCopy() {
        IVector v = new Vector(1, 2, 3);
        assertEquals(v, v.copy());
    }

    @Test
    public void testCopyPart() {
        IVector v = new Vector(1, 2, 3);
        assertEquals(new Vector(1, 2), v.copyPart(2));
    }

    @Test
    public void testAdd() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(Vector.newUnmodifiable(5, 7, 9), v1.add(v2));
    }

    @Test (expected = UnmodifiableObjectException.class)
    public void testAddUnmodifiable() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        v1.add(v2);
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testAddIncompatible() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6, 7);
        v1.add(v2);
    }

    @Test
    public void testNAdd() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(Vector.newUnmodifiable(5, 7, 9), v1.nAdd(v2));
    }

    @Test
    public void testSub() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(Vector.newUnmodifiable(-3, -3, -3), v1.sub(v2));
    }

    @Test
    public void testNSub() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(Vector.newUnmodifiable(-3, -3, -3), v1.nSub(v2));
    }

    @Test
    public void testScalarMultiply() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        assertEquals(Vector.newUnmodifiable(-2, -4, -6), v1.scalarMultiply(-2));
    }

    @Test
    public void testNScalarMultiply() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        assertEquals(Vector.newUnmodifiable(-2, -4, -6), v1.nScalarMultiply(-2));
    }

    @Test
    public void testNorm() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        assertEquals(Math.sqrt(14), v1.norm(), EPSILON);
    }

    @Test
    public void testNormalize() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        assertEquals(1, v1.normalize().norm(), EPSILON);
    }

    @Test
    public void testNNormalize() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        assertEquals(1, v1.nNormalize().norm(), EPSILON);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNormalizeNulvector() {
        IVector v1 = Vector.newModifiable(0);
        v1.normalize();
    }

    @Test
    public void testScalarProduct() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(32, v1.scalarProduct(v2), EPSILON);
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testScalarProductIncompatible() {
        IVector v1 = Vector.newModifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6, 7);
        v1.scalarProduct(v2);
    }

    @Test
    public void testNVectorProduct() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(Vector.newUnmodifiable(-3, 6, -3), v1.nVectorProduct(v2));
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testNVectorProductIncompatibleDimension() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3, 4);
        IVector v2 = Vector.newUnmodifiable(5, 6, 7, 8);
        v1.nVectorProduct(v2);
    }

    @Test
    public void testCosine() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        assertEquals(0.97463184619707621, v1.cosine(v2), EPSILON);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCosineNulVector1() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = Vector.newUnmodifiable(0, 0, 0);
        v1.cosine(v2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCosineNulVector2() {
        IVector v1 = Vector.newModifiable(0, 0, 0);
        IVector v2 = Vector.newUnmodifiable(4, 5, 6);
        v1.cosine(v2);
    }

    @Test
    public void testFromHomogeneous() {
        IVector v = Vector.newUnmodifiable(2, 4, 6, 2).nFromHomogeneus();
        assertEquals(Vector.newUnmodifiable(1, 2, 3), v);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFromHomogeneousIncompatibleDimension() {
        Vector.newUnmodifiable(2).nFromHomogeneus();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFromHomogeneousZeroHomogeneousCoordinate() {
        Vector.newUnmodifiable(1, 2, 3, 0).nFromHomogeneus();
    }

    @Test
    public void testToString() {
        IVector v = Vector.newUnmodifiable(1, 2, 3);
        assertEquals("(1.000, 2.000, 3.000)", v.toString());
    }

    @Test
    public void testVectorToMatrix() {
        IVector v = Vector.newModifiable(1, 2, 3);
        assertEquals(v.toColumnMatrix(true), Matrix.parseSimple("1 | 2 | 3"));
        assertEquals(v.toRowMatrix(true), Matrix.parseSimple("1 2 3"));
    }

    @Test
    public void testVectorToMatrixLive() {
        IVector v = Vector.newModifiable(1, 2, 3);

        IMatrix mc = v.toColumnMatrix(true);
        mc.set(1, 0, 100.0);

        IMatrix mr = v.toRowMatrix(true);
        mr.set(0, 2, 200.0);

        assertEquals(v, Vector.newModifiable(1, 100.0, 200.0));
    }

    @Test
    public void testVectorToMatrixDead() {
        IVector v = Vector.newModifiable(1, 2, 3);

        IMatrix mc = v.toColumnMatrix(false);
        mc.set(1, 0, 100.0);

        IMatrix mr = v.toRowMatrix(false);
        mr.set(0, 2, 200.0);

        assertEquals(v, Vector.newUnmodifiable(1, 2, 3));
    }

    @Test
    public void testParseSimple() {
        IVector v = Vector.parseSimple("1 2 3");
        assertEquals(Vector.newUnmodifiable(1, 2, 3), v);
    }

    @Test
    public void testToArray() {
        IVector vector = Vector.newUnmodifiable(1, 2, 3);
        double[] array = new double[]{1, 2, 3};
        assertArrayEquals(array, vector.toArray(), EPSILON);
    }

    @Test
    public void testEquals() {
        IVector v1 = Vector.newUnmodifiable(1, 2, 3);
        IVector v2 = v1.copy();
        IVector v3 = Vector.newUnmodifiable(4, 5, 6);
        IVector v4 = Vector.newUnmodifiable(4, 5);

        assertEquals(v1, v2);
        assertFalse(v1.equals(new Integer(5)));
        assertFalse(v1.equals(v3));
        assertFalse(v1.equals(v4));
    }
}
