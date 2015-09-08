package hr.fer.zemris.java.student0177035204.hw06;

import static hr.fer.zemris.java.student0177035204.hw06.CijeliBrojevi.*;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Testovi za razred CijeliBrojevi.
 */
public class CijeliBrojeviTest 
{
    /**
     * Test provjerava da li je -3 neparan broj.
     */
    @Test
    public void testNeparan()
    {
        assertTrue(jeNeparan(-3));
    }

    /**
     * Test provjerava da li je 8 paran broj.
     */
    @Test
    public void testParan()
    {
        assertTrue(jeParan(8));
    }

    /**
     * Test provjerava da li je 7 prost broj.
     */
    @Test
    public void testProst()
    {
        assertTrue(jeProst(7));
    }

    /**
     * Test provjerava da li 16 nije neparan broj.
     */
    @Test
    public void testNijeNeparan()
    {
        assertFalse(jeNeparan(16));
    }

    /**
     * Test provjerava da li 51 nije paran broj.
     */
    @Test
    public void testNijeParan()
    {
        assertFalse(jeParan(51));
    }

    /**
     * Test provjerava da li 15 nije prost broj.
     */
    @Test
    public void testNijeProst()
    {
        assertFalse(jeProst(15));
    }
}
