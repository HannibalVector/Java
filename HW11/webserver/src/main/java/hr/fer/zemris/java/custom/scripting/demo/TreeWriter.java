package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.tokens.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Application used for demonstrating use of node visitor to generate original text of document.
 * @author ilijan
 */
public class TreeWriter {
    static class WriterVisitor implements INodeVisitor {
        private StringBuilder originalTextBuilder;

        public WriterVisitor() {
            originalTextBuilder = new StringBuilder();
        }

        @Override
        public void visitTextNode(TextNode node) {
            originalTextBuilder.append(
                    node.getText().replace("\\", "\\\\").replace("{", "\\{"));
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
            originalTextBuilder
                    .append("{$ FOR ")
                    .append(node.getVariable().asText()).append(" ")
                    .append(node.getStartExpression().asText()).append(" ")
                    .append(node.getEndExpression().asText());
            if(node.getStartExpression() != null) {
                originalTextBuilder.append(" ").append(node.getStartExpression().asText());
            }
            originalTextBuilder.append(" $}");
            visitNodesGroup(node);
            originalTextBuilder.append("{$ END $}");
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            originalTextBuilder.append("{$= ");
            for (Token token : node.getTokens()) {
                originalTextBuilder
                        .append(token.asText())
                        .append(" ");
            }
            originalTextBuilder.append("$}");
        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            visitNodesGroup(node);
        }

        public String getDocumentText() {
            return originalTextBuilder.toString();
        }
    }


    public static void main(String[] args) throws IOException {

        String filePath = "fibonacci.smscr";
        String docBody = new String(
                Files.readAllBytes(Paths.get(filePath)),
                StandardCharsets.UTF_8
        );

        String line ="\n------------------------------\n";

        System.out.println(line + "Original document" + line);
        System.out.println(docBody);

        SmartScriptParser parser = new SmartScriptParser(docBody);
        DocumentNode document = parser.getDocumentNode();

        WriterVisitor visitor = new WriterVisitor();
        document.accept(visitor);

        String generatedOriginalDocBody = visitor.getDocumentText();
        System.out.println(line + "Parsed document" + line);
        System.out.println(generatedOriginalDocBody);
    }
}
