package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ilijan
 */
public class EngineDemo4 {

    public static void main(String[] args) {
        String documentBody = readFromDisk("fibonacci.smscr");
        Map<String,String> parameters = new HashMap<String, String>();
        Map<String,String> persistentParameters = new HashMap<String, String>();
        List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

// create engine and execute it
        new SmartScriptEngine(
                new SmartScriptParser(documentBody).getDocumentNode(),
                new RequestContext(System.out, parameters, persistentParameters, cookies)
        ).execute();
    }

    private static String readFromDisk(String filePath) {
        try {
            String docBody = new String(
                    Files.readAllBytes(Paths.get(filePath)),
                    StandardCharsets.UTF_8
            );

            return docBody;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
