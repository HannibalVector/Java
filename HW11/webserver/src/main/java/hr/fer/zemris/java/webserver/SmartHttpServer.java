package hr.fer.zemris.java.webserver;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple HTTP server. Single optional command line argument can be provided - path to configuration file.
 * Server responds to basic HTTP requests providing access to files if they are available, but also
 * offers dynamical execution of SmartScripts (extension .smscr) or use of worker classes which are invoked
 * when specific format of URL is requested.
 *
 * @author ilijan
 */
public class SmartHttpServer {
    /** Server IP address - address at which server listens for connections. */
    private String address;
    /** Server port. */
    private int port;
    /** Number of worker threads that process client requests. */
    private int workerThreads;
    /** Time after which cached information about client is no longer valid and can be erased. */
    private int sessionTimeout;
    /** Map of supported MIME types. */
    private Map<String, String> mimeTypes = new HashMap<>();
    /** Map of instances of supported worker classes. */
    private Map<String, IWebWorker> workersMap = new HashMap<>();
    /** Map used for caching sessions. */
    private Map<String, SessionMapEntry> sessions = new HashMap<>();
    /** Random number generator, used to generate session identifiers. */
    private Random sessionRandom = new Random();
    /** Main server thread monitoring connections to clients. */
    private ServerThread serverThread;
    /** Thread pool of worker threads. */
    private ExecutorService threadPool;
    /** Directory containing local files accessible through server. */
    private Path documentRoot;
    /** Server socket. */
    private ServerSocket serverSocket;
    /** Thread used for cleaning expired cached sessions. */
    private RemoveExpiredSessions cleanerThread;

    /**
     * Constructor initializes server by providing path to configuration file.
     * @param configFileName    path to configuration file.
     */
    public SmartHttpServer(String configFileName) {
        try {
            Properties serverProperties = new Properties();
            serverProperties.load(Files.newBufferedReader(Paths.get(configFileName)));

            address = serverProperties.getProperty("server.address");
            port = Integer.parseInt(serverProperties.getProperty("server.port"));
            workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
            documentRoot = Paths.get(serverProperties.getProperty("server.documentRoot"));
            sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));

            Path mimeConfigPath = Paths.get(serverProperties.getProperty("server.mimeConfig"));
            configureMimeTypes(mimeConfigPath);

            Path workersConfigPath = Paths.get(serverProperties.getProperty("server.workers"));
            configureWorkers(workersConfigPath);

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures map of workers. For every worker, for which path alias and fully qualified class name (FQCN)
     * are provided in configuration file (whose location is specified in main configuration file in parameter
     * 'server.workers') method makes an instance and puts it in the map.
     * @param workersConfigPath path to workers configuration file.
     * @throws IOException      if configuration file can not be read.
     */
    private void configureWorkers(Path workersConfigPath) throws IOException {
        Properties workersConfig = new Properties();
        workersConfig.load(Files.newBufferedReader(workersConfigPath));

        workersConfig.forEach(
                (pathObj, classNameObj) -> {
                    String path = (String)pathObj;
                    String className = (String)classNameObj;

                    if (workersMap.containsKey(path)) {
                        throw new IllegalArgumentException("Multiple workers for same path");
                    }

                    try {
                        workersMap.put(path, workerFromClassName(className));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Configures map of MIME types. For every supported MIME type, which is defined in MIME configuration file
     * (whose location is specified in main configuration file in parameter 'server.mimeConfig'), MIME identifier
     * is put into map.
     * @param mimeConfigPath    MIME types configuration file.
     * @throws IOException      if configuration file cannot be read.
     */
    private void configureMimeTypes(Path mimeConfigPath) throws IOException {
        Properties mimeConfig = new Properties();
        mimeConfig.load(Files.newBufferedReader(mimeConfigPath));

        mimeConfig.stringPropertyNames().forEach(
                key -> mimeTypes.put(key, mimeConfig.getProperty(key)
                ));
    }

    /**
     * Gets instance of worker class for given fully qualified class name.
     * @param className     fully qualified class name.
     * @return              instance of worker class.
     * @throws ClassNotFoundException   if there is no such class.
     * @throws IllegalAccessException   if the class or its nullary constructor is not accessible.
     * @throws InstantiationException   if this Class represents an abstract class, an interface, an array class,
     *                                  a primitive type, or void; or if the class has no nullary constructor;
     *                                  or if the instantiation fails for some other reason.
     */
    private IWebWorker workerFromClassName(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(className);
        Object newObject = referenceToClass.newInstance();
        return (IWebWorker)newObject;
    }

    /**
     * Starts server.
     */
    protected synchronized void start() {
        serverThread = new ServerThread();
        serverThread.start();

        cleanerThread = new RemoveExpiredSessions();
        cleanerThread.start();

        threadPool = Executors.newFixedThreadPool(workerThreads);
    }

    /**
     * Stops server.
     * @throws IOException
     */
    protected synchronized void stop() throws IOException {
        serverThread.shutdown();
        threadPool.shutdown();
    }

    /**
     * Thread used for removing expired cached sessions.
     */
    protected class RemoveExpiredSessions extends Thread {
        /**
         * Constructor sets thread to deamon thread.
         */
        public RemoveExpiredSessions() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(true) {
                synchronized(this) {
                    List<String> toRemove = new ArrayList<>();

                    for (Map.Entry<String, SessionMapEntry> entry : sessions.entrySet()) {
                        if (entry.getValue().validUntil - new Date().getTime() < 0) {
                            toRemove.add(entry.getKey());
                        }
                    }

                    toRemove.forEach(key -> {
                        sessions.remove(key);
                        timestampMessage("Removed session: " + key);
                    });
                }
                try {
                    Thread.sleep(sessionTimeout*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Server thread that manages connections between server and clients.
     * Delegates processing of work to multiple worker threads.
     */
    protected class ServerThread extends Thread {
        @Override
        public void run() {
            while(true) {
                Socket client;
                try {
                    client = serverSocket.accept();
                    timestampMessage("Client connected: " + client.getInetAddress() + ":" + client.getPort());
                    ClientWorker clientWorker = new ClientWorker(client);
                    threadPool.submit(clientWorker);
                } catch (SocketException ex) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            timestampMessage("Server stopped.");
        }

        /**
         * Shuts down the server thread by closing server socket. In such scenario method accept
         * (which is blocking the thread) throws {@link SocketException} which is used to escape
         * infinite loop and kill the thread.
         */
        public void shutdown() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Implements work done by worker threads, which is requested by clients.
     */
    private class ClientWorker implements Runnable {
        /** Client socket. */
        private Socket clientSocket;
        /** Input stream used to retrieve data from client. */
        private PushbackInputStream inputStream;
        /** Output stream used to send data to client. */
        private OutputStream outputStream;
        /** Version of protocol used. */
        private String version;
        /** HTTP method. */
        private String method;
        /** Map of parameters received through URL. */
        private Map<String,String> params = new HashMap<>();
        /** Map of persistent parameters which are cached for predefined period of time. */
        private Map<String,String> persParams = null;
        /** List of output cookies. */
        private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
        /** Path to requested file. */
        private String path;

        /**
         * Constructor initializes new worker using provided client socket.
         * @param clientSocket  socket used for communication with client.
         */
        public ClientWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                inputStream = new PushbackInputStream(clientSocket.getInputStream());
                outputStream = clientSocket.getOutputStream();

                List<String> request = readRequest(inputStream);
                if (request.size() == 0) {
                    sendError(400, "Invalid header.");
                }

                // checks if session is cached in map sessions
                checkSession(request);

                String firstLine = request.get(0);
                extractProperties(firstLine);

                RequestContext requestContext = new RequestContext(outputStream, params, persParams, outputCookies);

                if (path.equals("/")) {
                    path = "index";
                }
                if (processedAsWorkerAlias(requestContext)) {
                    return;
                }

                String extension = extractExtension(path);
                setMimeType(requestContext, extension);

                Path filePath = Paths.get(documentRoot + path);
                checkFile(filePath);

                if (extension.equals("smscr")) {
                    processAsSmartScript(requestContext, filePath);
                } else {
                    requestContext.write(Files.readAllBytes(filePath));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServerException se) {
                timestampMessage(se.getMessage());
                return;
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Processes requested file as SmartScript, generating dynamical output.
         * @param requestContext    {@link RequestContext} used for communication with client.
         * @param filePath          file containing script.
         * @throws IOException      in case script can not be read.
         */
        private void processAsSmartScript(RequestContext requestContext, Path filePath) throws IOException {
            String documentBody = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            new SmartScriptEngine(
                    new SmartScriptParser(documentBody).getDocumentNode(),
                    requestContext
            ).execute();
        }

        /**
         * Sets MIME type based on extension, using map of supported MIME types.
         * @param requestContext    {@link RequestContext} used for communication with client.
         * @param extension         file extension.
         */
        private void setMimeType(RequestContext requestContext, String extension) {

            String mimeType = mimeTypes.get(extension);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            requestContext.setMimeType(mimeType);
            requestContext.setStatusCode(200);
        }

        /**
         * Checks if file is forbidden and if it exists and sends appropriate errors.
         * @param filePath      path to file to be checked.
         * @throws ServerException  used to notify worker thread and server logs about exception.
         */
        private void checkFile(Path filePath) throws ServerException, IOException {
            if (documentRoot.startsWith(path)) {
                throw new ServerException(403, "Forbidden.");
            }

            if (!filePath.toFile().isFile()) {
                throw new ServerException(404, "Not found.");
            }
        }

        /**
         * Tries processing requested path as alias for calling appropriate worker class.
         * @param requestContext    {@link RequestContext} used for communication with client.
         * @return                  {@code true} if path is successfully processed as alias, {@code false} otherwise.
         * @throws ServerException  in case virtual subdirectory redirecting requests to workers is used and
         *                          no such worker exists.
         */
        private boolean processedAsWorkerAlias(RequestContext requestContext) throws ServerException, IOException {
            if (path.startsWith("/ext/")) {
                String className = "hr.fer.zemris.java.webserver.workers." + path.substring(5);
                try {
                    IWebWorker worker = workerFromClassName(className);
                    worker.processRequest(requestContext);
                } catch (Exception e) {
                    throw new ServerException(404, "Not found.");
                }
            }

            IWebWorker worker = workersMap.get(path);
            if (worker != null) {
                worker.processRequest(requestContext);
                return true;
            }

            try {
                requestContext.setTemporaryParameter("page", path);
                workersMap.get("/get").processRequest(requestContext);
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        /**
         * Checks if current client is cached in session map.
         * @param request   http request header.
         */
        private synchronized void checkSession(List<String> request) {
            String sidCandidate = null;
            for (String line : request) {
                String[] parts = line.split(":");
                String variableName = parts[0].trim();
                if (variableName.toUpperCase().equals("COOKIE")) {
                    String[] variableProperties = parts[1].trim().split(";");
                    String[] nameProperty = variableProperties[0].split("=");
                    String cookieName = nameProperty[0].trim();
                    String cookieValue = nameProperty[1].trim().replace("\"", "");
                    if (cookieName.toUpperCase().equals("SID")) {
                        sidCandidate =  cookieValue;
                    }
                }
            }

            long now = new Date().getTime();

            if (sidCandidate != null
                    && sessions.containsKey(sidCandidate)
                    && sessions.get(sidCandidate).validUntil - now > 0) {
                SessionMapEntry existingEntry = sessions.get(sidCandidate);
                existingEntry.validUntil = now + sessionTimeout * 1000;
                persParams = existingEntry.map;
                return;
            }

            String newSid = "";
            for (int i = 0; i < 20; i++) {
                newSid += (char)(sessionRandom.nextInt(26) + 'A');
            }
            SessionMapEntry newSession = new SessionMapEntry(newSid);
            sessions.put(newSid, newSession);
            outputCookies.add(new RequestContext.RCCookie("sid", newSid, null, address, "/"));
            persParams = newSession.map;
        }

        /**
         * Extracts extension from given filename.
         * @param fileName  filename to extract extension from.
         * @return          file extension.
         */
        private String extractExtension(String fileName) {
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i+1);
            }
            return extension;
        }

        /**
         * Extracts http method, version of protocol and path from first line of http request header.
         * @param firstLine         first line of http request.
         * @throws ServerException  in case of bad request formatting, non-allowed method or unsupported protocol.
         */
        private void extractProperties(String firstLine) throws IOException, ServerException {
            String[] properties = firstLine.split(" ");

            if (properties.length != 3) {
                throw new ServerException(400, "Bad request.");
            }

            method = properties[0].toUpperCase();
            if(!method.equals("GET")) {
                throw new ServerException(400, "Method not allowed.");
            }

            version = properties[2].toUpperCase();
            if(!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
                throw new ServerException(400, "HTTP version not supported.");
            }

            String[] requestedPath = properties[1].split("\\?");
            path = requestedPath[0];

            if (requestedPath.length > 1) {
                String paramString = requestedPath[1];
                parseParameters(paramString);
            }
        }

        /**
         * Parses URL parameters from string and populates appropriate map.
         * @param paramString   string containing URL parameters.
         */
        private void parseParameters(String paramString) {
            String[] entries = paramString.split("&");
            for (String entry : entries) {
                String[] keyAndValue = entry.split("=");
                params.put(keyAndValue[0], keyAndValue[1]);
            }
        }

        /**
         * Sends error to user using SmartScript error template.
         * @param statusCode    error code.
         * @param statusText    error description.
         */
        private void sendError(int statusCode, String statusText) throws IOException {

            RequestContext errorRequest = new RequestContext(outputStream, null, null, null);
            errorRequest.setTemporaryParameter("errorCode", String.valueOf(statusCode));
            errorRequest.setTemporaryParameter("errorMessage", statusText);
            String root = "http://" + address
                    + ":" + port + "/";
            errorRequest.setTemporaryParameter("root", root);
            errorRequest.statusCode = statusCode;
            errorRequest.statusText = statusText;
            errorRequest.setMimeType("smscr");
            String errorTemplate = "templates/error.smscr";


            processAsSmartScript(errorRequest, Paths.get(errorTemplate));
        }

        /**
         * Exception thrown when error is sent to client. Used to interrupt normal transmission to client, and to
         * notify server logs about error.
         */
        private class ServerException extends Exception {
            /**
             * Default constructor specifies exception message and sends error to client
             * using SmartScript error template.
             * @param statusCode    error code.
             * @param statusText    error description.
             * @throws ServerException  exception used to interrupt normal transmission in case of error.
             */
            public ServerException(int statusCode, String statusText) throws IOException {
                super("Error " + statusCode + ": " + statusText);
                sendError(statusCode, statusText);
            }
        }
    }

    /**
     * Reads http request header from input stream.
     * @param inputStream   input stream used for getting data from client.
     * @return              http request header.
     * @throws IOException  in case of error in reading stream.
     */
    private static List<String> readRequest(InputStream inputStream)
            throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int state = 0;
        l:		while(true) {
            int b = inputStream.read();
            if(b==-1) return null;
            if(b!=13) {
                bos.write(b);
            }
            switch(state) {
                case 0:
                    if(b==13) { state=1; } else if(b==10) state=4;
                    break;
                case 1:
                    if(b==10) { state=2; } else state=0;
                    break;
                case 2:
                    if(b==13) { state=3; } else state=0;
                    break;
                case 3:
                    if(b==10) { break l; } else state=0;
                    break;
                case 4:
                    if(b==10) { break l; } else state=0;
                    break;
            }
        }

        return Arrays.asList(bos.toString("ISO_8859_1").split("\n"));
    }

    /**
     * Implements session record holding information about session ID, time of session expiry, and map of
     * persistent parameters.
     */
    private class SessionMapEntry {
        /** Session ID. */
        String sid;
        /** Computer time marking end of validity of cached information. */
        long validUntil;
        /** Map holding persistent parameters. */
        Map<String,String> map;

        /**
         * Constructor initializes new {@link SessionMapEntry} using provided session ID.
         * @param sid   session ID.
         */
        public SessionMapEntry(String sid) {
            this.sid = sid;
            validUntil = new Date().getTime() + sessionTimeout*1000;
            map = Collections.synchronizedMap(new HashMap<>());
        }
    }

    /**
     * Main method for program {@link SmartHttpServer}.
     * @param args command-line arguments. One optional argument containing path to config file is expected.
     */
    public static void main(String[] args) {
        String propertiesFile;
        if (args.length == 1) {
            propertiesFile = args[0];
        } else {
            propertiesFile = "server.properties";
        }
        SmartHttpServer server = new SmartHttpServer(propertiesFile);
        server.start();
        timestampMessage("Smart HTTP Server started. To stop server type 'stop'.");
        promptForStop(server);
    }

    /**
     * Waits until user writes 'stop' on standard input, after which stops the server. Ignores other input.
     * @param server    server to stop.
     */
    private static void promptForStop(SmartHttpServer server) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.next().equals("stop")) {
                try {
                    server.stop();
                    break;
                } catch (IOException e) {
                    timestampMessage(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Prints time stamp at the time of calling along with provided message. Used for logging.
     * @param message   message to display.
     */
    private static void timestampMessage(String message) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(formatter.format(new Date()) + " -- " + message);
    }
}
