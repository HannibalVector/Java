package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author ilijan
 */
public class PageWorker implements IWebWorker {
    private static final Path ROOT = Paths.get("webroot");
    private static final String PAGES = "pages";
    private static final String TEMPLATE = "templates/page.smscr";

    public PageWorker() {
    }

    @Override
    public void processRequest(RequestContext context) {
        String page = context.getParameter("page");
        if (page == null) {
            page = context.getTemporaryParameter("page");
        }
        File pageFile = new File(PAGES + "/" + page + ".txt");
        if (!pageFile.exists()) {
            throw new IllegalArgumentException("Page doesn't exist.");
        }
        try {
            List<String> lines = Files.readAllLines(pageFile.toPath());
            for (String line : lines) {
                String[] properties = line.split("=");
                if (properties.length < 2) {
                    throw new IllegalArgumentException("Invalid page file format.");
                }
                String key = properties[0].trim();
                String value = line.substring(line.indexOf('=') + 1).trim();
                context.setTemporaryParameter(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String menuDir = context.getTemporaryParameter("menuDir");

        if (menuDir != null) {
            context.setTemporaryParameter("menu",
                    generateMenu(menuDir)
            );
        }

        try {
            String documentBody = new String(Files.readAllBytes(Paths.get(TEMPLATE)), StandardCharsets.UTF_8);
            new SmartScriptEngine(
                    new SmartScriptParser(documentBody).getDocumentNode(),
                    context
            ).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateMenu(String directory){
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            return "empty";
        }
        File[] files = dir.listFiles();
        String menu = "<ul>";
        for (File file : files) {
            menu += "<li><a href=\"" + ROOT.relativize(file.toPath()) +"\">"
                    + file.getName() + "</a></li>";
        }
        menu += "</ul>";
        return menu;
    }
}
