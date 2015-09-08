package hr.fer.zemris.java.hw2;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SmartScriptTester {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Invalid number of arguments are passed to program.");
			System.exit(1);
		}
		
		String filePath = args[0];
		String docBody = new String(
				Files.readAllBytes(Paths.get(filePath)),
				StandardCharsets.UTF_8
		);

        String line ="\n------------------------------\n";

        System.out.println(line + "Original document" + line);
        System.out.println(docBody);
		
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();

		String originalDocumentBody = document.getOriginalText();
        System.out.println(line + "Parsed document" + line);
        System.out.println(originalDocumentBody);

        SmartScriptParser reparser = new SmartScriptParser(originalDocumentBody);
        DocumentNode reparsedDocument = reparser.getDocumentNode();

        String reparsedDocumentBody= reparsedDocument.getOriginalText();
        System.out.println(line + "Reparsed document" + line);
        System.out.println(reparsedDocumentBody);

        System.out.println("\nOriginal document and parsed document generate structurally identical trees: "
        + document.equals(reparsedDocument) + ".");
    }
}
