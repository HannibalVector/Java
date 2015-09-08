package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;

/**
 * Echoes parameters given through URL inside HTML table.
 * @author ilijan
 */
public class EchoParams implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) {

        StringBuilder stringBuilder = new StringBuilder(
                "<html>\r\n" +
                        "   <head>\r\n" +
                        "       <title>Parameter Information</title>\r\n"+
                        "   </head>\r\n" +
                        "   <body>\r\n" +
                        "       <h1>Parameter Information</h1>\r\n" +
                        "       <p>You have passed following parameters:</p>\r\n" +
                        "       <table border='1'>\r\n");
        for(String parameter : context.getParameterNames()) {
            stringBuilder.append("      <tr><td>")
                    .append(parameter)
                    .append("</td><td>")
                    .append(context.getParameter(parameter))
                    .append("</td></tr>\r\n");
        }
        stringBuilder.append(
                "    </table>\r\n" +
                        "  </body>\r\n" +
                        "</html>\r\n");

        try {
            context.setMimeType("text/html");
            context.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
