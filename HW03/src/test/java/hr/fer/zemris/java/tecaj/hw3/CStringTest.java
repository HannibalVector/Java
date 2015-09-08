package hr.fer.zemris.java.tecaj.hw3;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ilijan on 4/1/15.
 */
public class CStringTest {

    private static final char[] array = new char[] {'t', 'e', 'k', 's', 't'};
    private CString cstring = new CString("otorinolaringologija");

    @Test
    public void testConstructorFromArray() {

        cstring = new CString(array, 0, array.length);
        Assert.assertTrue(cstring.toString().equals("tekst"));

        cstring = new CString(array, 0, 3);
        Assert.assertTrue(cstring.toString().equals("tek"));

        cstring = new CString(array, 1, 4);
        Assert.assertTrue(cstring.toString().equals("ekst"));
    }

    @Test (expected = CStringIndexOutOfBoundsException.class)
    public void testWrongOffsetAndLength() {
        cstring = new CString(array, 3, 4);
    }

    @Test
    public void testConstructorFromString() {
        String string = "ovo je neki tekst";
        cstring = new CString(string);
        Assert.assertTrue(cstring.toString().equals(string));
    }

    @Test
    public void testConstructorFromCString() {
        cstring = new CString("neki tekst");
        CString cstring1 = new CString(cstring);
        Assert.assertTrue(cstring.toString().equals(cstring1.toString()));

        cstring1 = new CString(cstring.substring(0, 4));
        System.out.println(cstring1);
        Assert.assertTrue(cstring1.toString().equals("neki"));
    }

    @Test
    public void testLength() {
        cstring = new CString("neki tekst");
        Assert.assertEquals(10, cstring.length());

        cstring = cstring.substring(5, 10);
        Assert.assertEquals(5, cstring.length());
    }

    @Test
    public void testCharAt() {
        cstring = new CString("blabla");
        Assert.assertEquals('a', cstring.charAt(2));
        Assert.assertEquals('b', cstring.charAt(3));
    }

    @Test (expected =  CStringIndexOutOfBoundsException.class)
    public void testCharAtTooBigIndex() {
        cstring = new CString("blabla");
        Assert.assertEquals('a', cstring.charAt(7));
    }

    @Test (expected =  CStringIndexOutOfBoundsException.class)
    public void testCharAtNegativeIndex() {
        cstring = new CString("blabla");
        Assert.assertEquals('a', cstring.charAt(-1));
    }

    @Test
    public void testToCharArray() {
        cstring = new CString(array, 0, array.length);
        char[] newArray = cstring.toCharArray();
        for(int i = 0; i < newArray.length; i++) {
            Assert.assertEquals(array[i], newArray[i]);
        }
    }

    @Test
    public void testIndexOf() {
        Assert.assertEquals(3, cstring.indexOf('r'));
        Assert.assertEquals(7, cstring.indexOf('l'));
        Assert.assertEquals(-1, cstring.indexOf('z'));
    }

    @Test
    public void testStartsWith() {
        CString startString = new CString("otorino");
        CString endString = new CString("gologija");
        Assert.assertTrue(cstring.startsWith(startString));
        Assert.assertFalse(cstring.startsWith(endString));
    }

    @Test
    public void testEndsWith() {
        CString startString = new CString("otorino");
        CString endString = new CString("gologija");
        Assert.assertTrue(cstring.endsWith(endString));
        Assert.assertFalse(cstring.endsWith(startString));
    }

    @Test
    public void testContains() {
        CString containedString = new CString("otorino");
        CString noncontainedString = new CString("macka");
        Assert.assertTrue(cstring.contains(containedString));
        Assert.assertFalse(cstring.contains(noncontainedString));
    }

    @Test (expected = CStringIndexOutOfBoundsException.class)
    public void testSubstringNegativeStart() {
        cstring.substring(-1, 5);
    }

    @Test (expected = CStringIndexOutOfBoundsException.class)
    public void testSubstringTooLargeEnd() {
        cstring.substring(0, 50);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSubstringEndSmallerThanStart() {
        cstring.substring(5, 4);
    }

    @Test
    public void testLeft() {
        CString left = cstring.left(5);
        Assert.assertTrue(left.toString().equals("otori"));
    }

    @Test
    public void testRight() {
        CString left = cstring.right(7);
        Assert.assertTrue(left.toString().equals("ologija"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLeftNegativeNumberOfCharacters() {
        cstring.left(-5);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testLeftTooLargeNumberOfCharacters() {
        cstring.left(80);
    }

    @Test
    public void testAdd() {
        CString cstringToAdd = new CString(" je jako duga rijec.");
        Assert.assertTrue(
                cstring.add(cstringToAdd).toString()
                        .equals("otorinolaringologija je jako duga rijec.")
        );
    }

    @Test
    public void testReplaceAllChars() {
        Assert.assertTrue(
                cstring.replaceAll('o', 'x').toString()
                        .equals("xtxrinxlaringxlxgija")
        );
    }

    @Test
    public void testReplaceAllStrings() {
        CString cstringOld = new CString("ri");
        CString cstringNew = new CString(" cvjetic xoxo ");
        Assert.assertTrue(
                cstring.replaceAll(cstringOld, cstringNew).toString()
                        .equals("oto cvjetic xoxo nola cvjetic xoxo ngologija")
        );
    }








}
