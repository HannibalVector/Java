package hr.fer.zemris.java.tecaj.hw3;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ilijan on 4/1/15.
 */
public class ComplexNumberTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void checkRealPart() {
        ComplexNumber z = new ComplexNumber(1.2, 3.4);
        Assert.assertEquals(1.2, z.getReal(), EPSILON);
    }

    @Test
    public void checkImaginaryPart() {
        ComplexNumber z = new ComplexNumber(1.2, 3.4);
        Assert.assertEquals(3.4, z.getImaginary(), EPSILON);
    }

    @Test
    public void checkMagnitude() {
        ComplexNumber z = new ComplexNumber(1.2, 3.4);
        Assert.assertEquals(Math.sqrt(13), z.getMagnitude(), EPSILON);
    }

    @Test
    public void checkAngle() {
        ComplexNumber z = new ComplexNumber(1.2, 3.4);
        Assert.assertEquals(Math.atan2(3.4, 1.2), z.getAngle(), EPSILON);
    }

    @Test
    public void checkFactoryFromImaginary() {
        ComplexNumber z = ComplexNumber.fromImaginary(5.7);
        checkComplexNumber(0, 5.7, z);
    }

    @Test
    public void checkFactoryFromReal() {
        ComplexNumber z = ComplexNumber.fromReal(2.1);
        checkComplexNumber(2.1, 0, z);
    }

    private void checkComplexNumber(double realPart, double imaginaryPart, ComplexNumber z) {
        Assert.assertEquals(realPart, z.getReal(), EPSILON);
        Assert.assertEquals(imaginaryPart, z.getImaginary(), EPSILON);
    }

    @Test
    public void checkFactoryFromMagnitudeAndAngle() {
        ComplexNumber z = ComplexNumber.fromMagnitudeAndAngle(5.28, 16.3);
        Assert.assertEquals(5.28, z.getMagnitude(), EPSILON);
        double standardAngle = 16.3 - 2*Math.PI*Math.floor(16.3/(2*Math.PI)) - 2*Math.PI;
        Assert.assertEquals(standardAngle, z.getAngle(), EPSILON);
    }

    @Test
    public void checkParser() {
        compareStringAndNumber("+2.5-2i", 2.5, -2);
        compareStringAndNumber("i", 0, 1);
        compareStringAndNumber("  i", 0, 1);
        compareStringAndNumber(" -2.7i -16", -16, -2.7);
        compareStringAndNumber("-i", 0, -1);
        compareStringAndNumber("-1.2e-3i+10e15", 10e15, -1.2e-3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void checkParsingEmptyString() {
        ComplexNumber.parse("");
        ComplexNumber.parse("   ");
    }

    @Test (expected = ComplexNumberParserException.class)
    public void tryPassTwoRealPartsToParser() {
        ComplexNumber.parse("5.2-7");
    }

    @Test (expected = ComplexNumberParserException.class)
    public void tryPassTwoImaginaryPartsToParser() {
        ComplexNumber.parse("i+5.2i");
    }


    private void compareStringAndNumber(String string, double realPart, double imaginaryPart) {
        ComplexNumber z = ComplexNumber.parse(string);
        Assert.assertEquals(realPart, z.getReal(), EPSILON);
        Assert.assertEquals(imaginaryPart, z.getImaginary(), EPSILON);
    }

    @Test
    public void checkToString() {
        checkToString(2.7, 7.12);
        checkToString(12, 0);
        checkToString(0, 5);
        checkToString(0, -5);
        checkToString(5, 1);
    }

    private void checkToString(double realPart, double imaginaryPart) {
        ComplexNumber z = new ComplexNumber(realPart,imaginaryPart);
        ComplexNumber w = ComplexNumber.parse(z.toString());
        Assert.assertEquals(w.getReal(), z.getReal(), EPSILON);
        Assert.assertEquals(w.getImaginary(), z.getImaginary(), EPSILON);
    }

    private static final ComplexNumber z1 = new ComplexNumber(1.2, 3.4);
    private static final ComplexNumber z2 = new ComplexNumber(5.6, 7.8);
    private static final ComplexNumber z3 = new ComplexNumber(1, 2);
    private static final ComplexNumber z4 = new ComplexNumber(3, -4);
    private static final ComplexNumber z5 = new ComplexNumber(0, 0);

    @Test
    public void checkAdd(){
        checkComplexNumber(6.8, 11.2, z1.add(z2));
    }

    @Test
    public void checkSub(){
        checkComplexNumber(-4.4, -4.4, z1.sub(z2));
    }

    @Test
    public void checkMul(){
        checkComplexNumber(-19.8, 28.4, z1.mul(z2));
    }

    @Test
    public void checkDiv(){
        checkComplexNumber(-0.2, 0.4, z3.div(z4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkDivisionByZero() {
        z1.div(z5);
    }

    @Test
    public void checkPower() {
        checkComplexNumber(-11, -2, z3.power(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNonPositiveDegreeOfPower() {
        z1.power(-5);
    }

    @Test
    public void checkRoots() {
        ComplexNumber[] roots = new ComplexNumber[] {
                new ComplexNumber(2, 0),
                new ComplexNumber(-1, Math.sqrt(3)),
                new ComplexNumber(-1, -Math.sqrt(3))
        };
        ComplexNumber z = new ComplexNumber(8, 0);
        ComplexNumber[] calculatedRoots = z.root(3);

        for(int i = 0; i < calculatedRoots.length; i++) {
            checkComplexNumber(roots[i], calculatedRoots[i]);
        }
    }

    private void checkComplexNumber(ComplexNumber expected, ComplexNumber calculated) {
        Assert.assertEquals(expected.getReal(), calculated.getReal(), EPSILON);
        Assert.assertEquals(expected.getImaginary(), calculated.getImaginary(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNegativeDegreeOfRoot() {
        z1.root(-5);
    }
}
