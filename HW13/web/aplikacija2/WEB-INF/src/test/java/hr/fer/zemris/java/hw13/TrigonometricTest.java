package hr.fer.zemris.java.hw13;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for class {@link Trigonometric}.
 * @author ilijan
 */
public class TrigonometricTest {
    public Trigonometric.Entry entry;

    @Before
    public void init(){
        entry = new Trigonometric.Entry(123);
    }

    @Test
    public void testSine(){
        assertEquals("0.8387", entry.getSine());
    }

    @Test
    public void testCosine(){
        assertEquals("-0.5446", entry.getCosine());
    }

}
