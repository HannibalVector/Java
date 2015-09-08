package hr.fer.zemris.java.webserver;

/**
 * Abstract worker which processes client request and generates response.
 * @author ilijan
 */
public interface IWebWorker {
    /**
     * Processes request encapsulated in {@link RequestContext} and generates response which is writen to
     * {@link RequestContext} output stream.
     * @param context   {@link RequestContext} used for communication with client.
     */
    void processRequest(RequestContext context);
}
