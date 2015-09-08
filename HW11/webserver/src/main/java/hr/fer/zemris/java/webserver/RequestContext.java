package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Encapsulates single client request to server and offers functionality for automatic http response generation.
 * @author ilijan
 */
public class RequestContext {

    /** Output stream for sending data to client. */
    private OutputStream outputStream;
    /** Charset used when sending characters as bytes. */
    private Charset charset;

    /** Character encoding. */
    public String encoding = "UTF-8";
    /** Status code. */
    public int statusCode = 200;
    /** Status text. */
    public String statusText = "OK";
    /** MIME type. */
    public String mimeType = "text/html";

    /** Parameters obtained from URL. */
    private Map<String,String> parameters;
    /** Temporary parameters used in scripting. */
    private Map<String,String> temporaryParameters;
    /** Persistent parameters which are cached for predefined amount of time. */
    private Map<String,String> persistentParameters;
    /** Output cookies. */
    private List<RCCookie> outputCookies;
    /** Indicates whether header was sent to prevent repetition. */
    private boolean headerGenerated = false;

    /**
     * Constructor initializes new request using provided output stream, URL parameters, persistent parameters and
     * list of cookies.
     * @param outputStream  output stream.
     * @param parameters    URL parameters.
     * @param persistentParameters  persistent (cached) parameters.
     * @param outputCookies         output cookies.
     */
    public RequestContext(
            OutputStream outputStream,
            Map<String,String> parameters,
            Map<String,String> persistentParameters,
            List<RCCookie> outputCookies) {
        if (outputStream == null) {
            throw new NullPointerException("Output stream must not be null!");
        }

        this.outputStream = outputStream;
        this.parameters = parameters;
        this.persistentParameters = persistentParameters;
        this.outputCookies = outputCookies;
    }

    /**
     * Getter for provided URL parameter key.
     * @param name  key for parameter.
     * @return      value of parameter.
     */
    public String getParameter(String name) {
        if (parameters == null) {
            return null;
        }
        return parameters.get(name);
    }

    /**
     * Getter for set of all keys for getting URL parameters.
     * @return  set of parameter keys.
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet());
    }

    /**
     * Getter for provided persistent parameter key.
     * @param name  key for parameter.
     * @return      value of parameter.
     */
    public String getPersistentParameter(String name) {
        if (persistentParameters == null) {
            return null;
        }
        return persistentParameters.get(name);
    }

    /**
     * Getter for set of all keys for getting persistent parameters.
     * @return  set of parameter keys.
     */
    public Set<String> getPersistentParameterNames() {
        return Collections.unmodifiableSet(persistentParameters.keySet());
    }

    /**
     * Setter for persistent parameter.
     * @param name  name (key) of parameter.
     * @param value value to be set.
     */
    public void setPersistentParameter(String name, String value) {
        persistentParameters.put(name, value);
    }

    /**
     * Removes persistent parameter for given name.
     * @param name  name of parameter to be removed.
     */
    public void removePersistentParameter(String name) {
        persistentParameters.remove(name);
    }

    /**
     * Getter for provided temporary parameter key.
     * @param name  key for parameter.
     * @return      value of parameter.
     */
    public String getTemporaryParameter(String name) {
        if (temporaryParameters == null) {
            return null;
        }
        return temporaryParameters.get(name);
    }

    /**
     * Getter for set of all keys for getting temporary parameters.
     * @return  set of parameter keys.
     */
    public Set<String> getTemporaryParameterNames() {
        if (temporaryParameters == null) {
            return null;
        }
        return Collections.unmodifiableSet(temporaryParameters.keySet());
    }

    /**
     * Setter for temporary parameter.
     * @param name  name (key) of parameter.
     * @param value value to be set.
     */
    public void setTemporaryParameter(String name, String value) {
        if (temporaryParameters == null) {
            temporaryParameters = new HashMap<>();
        }
        temporaryParameters.put(name, value);
    }

    /**
     * Removes temporary parameter for given name.
     * @param name  name of parameter to be removed.
     */
    public void removeTemporaryParameter(String name) {
        temporaryParameters.remove(name);
    }

    /**
     * Adds cookie to list of output cookies.
     * @param cookie    cookie to be added.
     */
    public void addRCCookie(RCCookie cookie) {
        if (headerGenerated) {
            throw new RuntimeException("Header already written!");
        }
        outputCookies.add(cookie);
    }

    /**
     * Writes array of bytes to client.
     * @param data  data to be written.
     * @return      reference to self.
     * @throws IOException  if writing is not successful.
     */
    public RequestContext write(byte[] data) throws IOException {
        if (!headerGenerated) {
            writeHeader();
        }

        outputStream.write(data);

        return this;
    }

    /**
     * Writes string of bytes to client.
     * @param text  data to be written.
     * @return      reference to self.
     * @throws IOException  if writing is not successful.
     */
    public RequestContext write(String text) throws IOException {
        if (!headerGenerated) {
            writeHeader();
        }

        outputStream.write(text.getBytes(charset));

        return this;
    }

    /**
     * Writes header to client.
     * @throws IOException  if writing is not successful.
     */
    private void writeHeader() throws IOException {
        charset = Charset.forName(encoding);
        String header =
                "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
                        + "Content-Type: " + mimeType;

        if (mimeType.startsWith("text/")) {
            header += "; charset=" + encoding;
        }

        header += "\r\n";

        if (outputCookies != null) {
            for (RCCookie cookie : outputCookies) {
                header += "Set-Cookie: " + cookie.getName() + "=\"" + cookie.getValue() + "\";";
                if (cookie.getDomain() != null) {
                    header += " Domain=" + cookie.getDomain() + ";";
                }
                if (cookie.getPath() != null) {
                    header += " Path=" + cookie.getPath() + ";";
                }
                if (cookie.getMaxAge() != null) {
                    header += " Max-Age=" + cookie.getMaxAge();
                }
                header += "\r\n";
            }
        }

        header += "\r\n";

        outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));

        headerGenerated = true;
    }


    /**
     * Sets encoding.
     * @param encoding Encoding to be set.
     */
    public void setEncoding(String encoding) {
        if (headerGenerated) {
            throw new RuntimeException("Header already written!");
        }
        this.encoding = encoding;
    }

    /**
     * Sets status code.
     * @param statusCode    status code to be set.
     */
    public void setStatusCode(int statusCode) {
        if (headerGenerated) {
            throw new RuntimeException("Header already written!");
        }
        this.statusCode = statusCode;
    }

    /**
     * Sets status text.
     * @param statusText    status text to be set.
     */
    public void setStatusText(String statusText) {
        if (headerGenerated) {
            throw new RuntimeException("Header already written!");
        }
        this.statusText = statusText;
    }

    /**
     * Sets MIME type.
     * @param mimeType  MIME type to be set.
     */
    public void setMimeType(String mimeType) {
        if (headerGenerated) {
            throw new RuntimeException("Header already written!");
        }
        this.mimeType = mimeType;
    }

    /**
     * Implements output cookie sent in {@link RequestContext}.
     */
    public static class RCCookie {
        /** Name of the cookie. */
        private String name;
        /** Value of the cookie. */
        private String value;
        /** Domain of server. */
        private String domain;
        /** Path on server. */
        private String path;
        /** Max-age of cookie. */
        private Integer maxAge;

        /**
         * Constructor initializes new cookie providing values for all variables through parameters.
         * @param name  cookie name.
         * @param value cookie value.
         * @param maxAge    maximal cookie age.
         * @param domain    domain of server.
         * @param path      path on server.
         */
        public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
            this.name = name;
            this.value = value;
            this.domain = domain;
            this.path = path;
            this.maxAge = maxAge;
        }

        /**
         * Getter for cookie name.
         * @return  cookie name.
         */
        public String getName() {
            return name;
        }

        /**
         * Getter for cookie value.
         * @return  cookie value.
         */
        public String getValue() {
            return value;
        }

        /**
         * Getter for server domain.
         * @return  server domain.
         */
        public String getDomain() {
            return domain;
        }

        /**
         * Getter for path on server.
         * @return  path on server.
         */
        public String getPath() {
            return path;
        }

        /**
         * Getter for maximal age of the cookie.
         * @return  maximal age of cookie.
         */
        public Integer getMaxAge() {
            return maxAge;
        }
    }
}
