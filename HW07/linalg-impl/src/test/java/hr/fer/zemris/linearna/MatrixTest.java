package hr.fer.zemris.linearna;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for classes implementing matrices.
 * 
 * @author ilijan
 */
public class MatrixTest {

    private static double EPSILON = 1.0e-14;

    @Test
    public void testRowsCount() {
        IMatrix m = new Matrix(2, 3);
        assertEquals(2, m.getRowsCount());
    }

    @Test
    public void testColsCount() {
        IMatrix m = new Matrix(2, 3);
        assertEquals(3, m.getColsCount());
    }

    @Test
    public void testGet() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        assertEquals(3.0, m.get(0, 2), EPSILON);
        assertEquals(5.0, m.get(1, 1), EPSILON);
    }

    @Test
    public void testSet() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        m.set(0, 1, 100);
        assertEquals(100.0, m.get(0, 1), EPSILON);
    }

    @Test
    public void testCopy() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        assertEquals(m, m.copy());
    }

    @Test
    public void testTranspose() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix mt = Matrix.parseSimple("1 4 | 2 5 | 3 6");
        assertEquals(m.nTranspose(true), mt);
    }

    @Test
    public void testTransposeLive() {
        IMatrix m_original = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m_transpose = m_original.nTranspose(true);
        m_transpose.set(0, 1, 100.0);
        assertEquals(100.0, m_original.get(1, 0), EPSILON);
    }

    @Test
    public void testTransposeDead() {
        IMatrix m_original = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m_transpose = m_original.nTranspose(false);
        IMatrix m_backup = m_original.copy();
        m_transpose.set(0, 1, 100.0);
        assertEquals(m_original, m_backup);
    }

    @Test
    public void testAdd() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 | 10 11 12");
        assertEquals(Matrix.parseSimple("8 10 12 | 14 16 18"), m1.add(m2));
    }

    @Test
    public void testNAdd() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 | 10 11 12");
        assertEquals(Matrix.parseSimple("8 10 12 | 14 16 18"), m1.nAdd(m2));
        assertEquals(Matrix.parseSimple("1 2 3 | 4 5 6"), m1);
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testAddIncompatibleCols() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 10 | 11 12 13 14");
        m1.add(m2);
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testAddIncompatibleRows() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 | 10 11 12 | 13 14 15");
        m1.add(m2);
    }

    @Test
    public void testSub() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 | 10 11 12");
        assertEquals(Matrix.parseSimple("6 6 6 | 6 6 6"), m2.sub(m1));
    }

    @Test
    public void testNSub() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 | 10 11 12");
        assertEquals(Matrix.parseSimple("6 6 6 | 6 6 6"), m2.nSub(m1));
        assertEquals(Matrix.parseSimple("7 8 9 | 10 11 12"), m2);
    }

    @Test
    public void testMultiply() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6");
        IMatrix m2 = Matrix.parseSimple("7 8 9 10 | 11 12 13 14 | 15 16 17 18");
        assertEquals(Matrix.parseSimple("74 80 86 92 | 173 188 203 218"), m1.nMultiply(m2));
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testMultiplyIncompatible() {
        IMatrix m = new Matrix(4, 5);
        m.nMultiply(m);
    }

    @Test
    public void testDeterminant() {
        IMatrix m = Matrix.parseSimple("17 4 90 | 27 5 91 | 3 5 15");
        assertEquals(3812.0, m.determinant(), EPSILON);
    }

    @Test
    public void testInverse() {
        IMatrix m = Matrix.parseSimple("17 4 90 | 27 5 91 | 3 5 15");
        IMatrix m_inv = Matrix.parseSimple("-0.0996852 0.1023085 -0.02256034" +
                "| -0.03462749 -0.00393494 0.23163694" +
                "| 0.03147954 -0.01915005 -0.00603358");
        assertEquals(m.nInvert(), m_inv);
    }

    @Test (expected = IncompatibleOperandException.class)
    public void testInverseNonSquareMatrix() {
        IMatrix m = Matrix.parseSimple("17 4 90 | 27 5 91 ");
        m.nInvert();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInverseNonInvertibleMatrix() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        m.nInvert();
    }

    @Test
    public void testIdentity() {
        IMatrix m = Matrix.parseSimple("17 4 90 | 27 5 91 | 3 5 15");
        assertEquals(m.makeIdentity(), m.nMultiply(m.nInvert()));
    }

    @Test
    public void testSubMatrixSubMatrix() {
        IMatrix m = Matrix.parseSimple("1 2 3 4 | 5 6 7 8 | 9 10 11 12 | 13 14 15 16");
        IMatrix subSub = m.subMatrix(0, 1, true).subMatrix(2, 1, true);
        assertEquals(Matrix.parseSimple("5 8 | 9 12"), subSub);

        subSub.set(0, 1, 100);
        assertEquals(Matrix.parseSimple("1 2 3 4 | 5 6 7 100 | 9 10 11 12 | 13 14 15 16"), m);

        subSub = m.subMatrix(0, 1, true).subMatrix(2, 1, false);
        subSub.set(1, 1, 100);
        assertEquals(Matrix.parseSimple("1 2 3 4 | 5 6 7 100 | 9 10 11 12 | 13 14 15 16"), m);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testSubMatrixIndexOutOfBounds1() {
        IMatrix m = new Matrix(5, 6);
        m.subMatrix(5, 7, true);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testSubMatrixIndexOutOfBounds2() {
        IMatrix m = new Matrix(5, 6);
        m.subMatrix(-5, -2, true);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testSubMatrixIndexOutOfBounds3() {
        IMatrix m = new Matrix(5, 6);
        m.subMatrix(-5, 1, true);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testSubMatrixIndexOutOfBounds4() {
        IMatrix m = new Matrix(5, 6);
        m.subMatrix(12, -2, true);
    }

    @Test
    public void testSubMatrixSubMatrixLive() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        IMatrix sub = m.subMatrix(0, 1, true);
        sub.set(0, 1, 100.0);
        assertEquals(Matrix.parseSimple("1 2 3 | 4 5 100 | 7 8 9"), m);
    }

    @Test
    public void testSubMatrixSubMatrixDead() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        IMatrix sub = m.subMatrix(0, 1, false);
        sub.set(0, 1, 100.0);
        assertEquals(Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9"), m);
    }

    @Test
    public void testScalarMultiply() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        assertEquals(Matrix.parseSimple("2 4 6 | 8 10 12"), m.scalarMultiply(2));
    }

    @Test
    public void testNScalarMultiply() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        assertEquals(Matrix.parseSimple("2 4 6 | 8 10 12"), m.nScalarMultiply(2));
        assertEquals(Matrix.parseSimple("1 2 3 | 4 5 6"), m);
    }

    @Test
    public void testToString() {
        IMatrix m = Matrix.parseSimple("1 2 3 | 4 5 6");
        assertEquals("[1.000, 2.000, 3.000]\n[4.000, 5.000, 6.000]\n", m.toString());
    }

    @Test
    public void testRowMatrixToVectorLive() {
        IMatrix m = Matrix.parseSimple("1 2 3");
        m.toVector(true).set(0, 100);
        assertEquals(Matrix.parseSimple("100 2 3"), m);
    }

    @Test
    public void testColMatrixToVectorLive() {
        IMatrix m = Matrix.parseSimple("1 | 2 | 3");
        m.toVector(true).set(0, 100);
        assertEquals(Matrix.parseSimple("100 | 2 | 3"), m);
    }

    @Test
    public void testRowMatrixToVectorDead() {
        IMatrix m = Matrix.parseSimple("1 2 3");
        m.toVector(false).set(0, 100);
        assertEquals(Matrix.parseSimple("1 2 3"), m);
    }

    @Test
    public void testColMatrixToVectorDead() {
        IMatrix m = Matrix.parseSimple("1 | 2 | 3");
        m.toVector(false).set(0, 100);
        assertEquals(Matrix.parseSimple("1 | 2 | 3"), m);
    }

    @Test
    public void testMatrixToVectorRow() {
        IVector v = Matrix.parseSimple("1 2 3").toVector(true);
        assertEquals(Vector.newUnmodifiable(1, 2, 3), v);
    }

    @Test
    public void testMatrixToVectorCol() {
        IVector v = Matrix.parseSimple("1 | 2 | 3").toVector(true);
        assertEquals(Vector.newUnmodifiable(1, 2, 3), v);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMatrixToVectorTooBigDimensions() {
        Matrix.parseSimple("1 2 3 | 4 5 6").toVector(true);
    }

    @Test
    public void testToArray() {
        IMatrix matrix = Matrix.parseSimple("1 2 3 | 4 5 6");
        double[][] array = new double[][]{{1, 2, 3}, {4, 5, 6}};
        for (int row = 0; row < 2; row++) {
            assertArrayEquals(array[row], matrix.toArray()[row], EPSILON);
        }
    }

    @Test
    public void testEquals() {
        IMatrix m1 = Matrix.parseSimple("1 2 3 | 4 5 6 | 7 8 9");
        IMatrix m2 = m1.copy();
        IMatrix m3 = Matrix.parseSimple("1 2 3 | 7 8 9 | 4 5 6");
        IMatrix m4 = Matrix.parseSimple("1 2 3 | 7 8 9");

        assertEquals(m1, m2);
        assertFalse(m1.equals(new Integer(5)));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(m4));
    }
}
