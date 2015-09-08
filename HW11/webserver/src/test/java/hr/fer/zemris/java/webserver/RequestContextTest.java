package hr.fer.zemris.java.webserver;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for class {@link RequestContext}.
 * @author ilijan
 */
public class RequestContextTest {
    RequestContext requestContext;
    Map<String, String> paramsURL;

    @Before
    public void init() {
        paramsURL = new HashMap<>();
        requestContext =
                new RequestContext(System.out, paramsURL, new HashMap<>(), new ArrayList<>());
    }

    @Test
    public void putPersistentParameter() {
        requestContext.setPersistentParameter("ana", "22");
        assertEquals("22", requestContext.getPersistentParameter("ana"));
    }

    @Test
    public void putTemporaryParameter() {
        requestContext.setTemporaryParameter("ivan", "33");
        assertEquals("33", requestContext.getTemporaryParameter("ivan"));
    }

    @Test
    public void getParameter() {
        paramsURL.put("ime", "Marko");
        paramsURL.put("prezime", "Markovic");

        assertEquals("Marko", requestContext.getParameter("ime"));
        assertEquals("Markovic", requestContext.getParameter("prezime"));
    }

    @Test
    public void removePersistentParameter() {
        requestContext.setPersistentParameter("ivana", "18");
        assertEquals("18", requestContext.getPersistentParameter("ivana"));
        requestContext.removePersistentParameter("ivana");
        assertEquals(null, requestContext.getPersistentParameter("ivana"));
    }

    @Test
    public void removeTemporaryParameter() {
        requestContext.setTemporaryParameter("ivana", "18");
        assertEquals("18", requestContext.getTemporaryParameter("ivana"));
        requestContext.removeTemporaryParameter("ivana");
        assertEquals(null, requestContext.getTemporaryParameter("ivana"));
    }
}
