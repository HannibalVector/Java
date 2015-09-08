package hr.fer.zemris.java.student0177035204.hw15;

import hr.fer.zemris.java.student0177035204.hw15.model.BlogUser;
import hr.fer.zemris.java.student0177035204.hw15.util.Util;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link BlogUser}.
 * @author ilijan
 */
public class BlogUserTest {
    BlogUser user;

    /**
     * Initializes new {@link BlogUser} object for further testing.
     */
    @Before
    public void intializeUser() {
        user = new BlogUser();
        user.setFirstName("Marko");
        user.setLastName("Markovic");
        user.setEmail("marko.markovic@gmail.com");
        user.setId(123456l);
        user.setNick("Markisha");
        user.setPasswordHash(Util.sha1Hex("password"));
    }

    /**
     * Tests first name getter.
     */
    @Test
    public void testFirstName() {
        assertEquals("Marko", user.getFirstName());
    }

    /**
     * Tests last name getter.
     */
    @Test
    public void testLastName() {
        assertEquals("Markovic", user.getLastName());
    }

    /**
     * Tests e-mail getter.
     */
    @Test
    public void testEmail() {
        assertEquals("marko.markovic@gmail.com", user.getEmail());
    }

    /**
     * Tests id getter.
     */
    @Test
    public void testId() {
        assertEquals(123456l, (long) user.getId());
    }

    /**
     * Test nickname getter.
     */
    @Test
    public void testNick() {
        assertEquals("Markisha", user.getNick());
    }

    /**
     * Tests password hash getter.
     */
    @Test
    public void testPasswordHash() {
        assertEquals(Util.sha1Hex("password"), user.getPasswordHash());
    }
}
