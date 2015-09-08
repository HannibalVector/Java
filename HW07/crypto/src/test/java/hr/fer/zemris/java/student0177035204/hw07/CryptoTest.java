package hr.fer.zemris.java.student0177035204.hw07;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for class {@code Crypto}.
 * @author ilijan
 */
public class CryptoTest {

    @Test
    public void hexToByteTest() {
        assertEquals("abcd", Crypto.byteToHex(Crypto.hexToByte("abcd")));
    }
}
