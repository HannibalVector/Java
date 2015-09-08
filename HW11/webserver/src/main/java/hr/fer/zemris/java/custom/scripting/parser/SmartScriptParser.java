package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.tokens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * {@code SmartScriptParser} parses structured document and generates tree which represents its structure.
 */
public class SmartScriptParser {

    /** Node that represents the whole document. */
	private DocumentNode documentNode;

    /** Char reader used for scanning string that represents document. */
    private CharReader reader;

    /** String that represents opening of a tag. */
    private final String tagOpener = "{$";

    /** String that represents closing of a tag. */
    private final String tagCloser = "$}";

    /**
     * Returns {@link DocumentNode} which represents a tree of parsed structured document.
     * Tree is generated on demand.
     * @return {@link DocumentNode} which represents a tree of parsed structured document.
     */
    public DocumentNode getDocumentNode() {
		if (documentNode == null) {
            documentNode = new DocumentNode();
            parse();
        }
        return documentNode;
	}

    /**
     * Constructor that takes {@code String} that contains document body.
     * @param documentBody  {@code String} that contains document body.
     */
	public SmartScriptParser(String documentBody) {
		reader = new CharReader(documentBody);
        reader.setEscapedCharacters("\\{");
	}

    /**
     * Method that parses document body and creates a tree that represents its structure.
     * Method uses array of type {@link Node} and creates tree using {@link ObjectStack} stack.
     * {@link SmartScriptParserException} gets thrown if document doesn't have equal number of
     * FOR and END tags.
     */
	private void parse() {
		
		Node[] nodes = nodify();
		
		Stack<Node> nodeStack = new Stack<>();
		nodeStack.push(documentNode);
        int forLoopNodeCounter = 0;
		
		for (Node node : nodes) {	
			if (node instanceof EndNode) {
				forLoopNodeCounter--;
                nodeStack.pop();
			} else {
				nodeStack.peek().addChildNode(node);
			}
			
			if (node instanceof ForLoopNode) {
				forLoopNodeCounter++;
                nodeStack.push(node);
			}
			
			if (nodeStack.isEmpty()) {
				break;
			}
        }

        if (forLoopNodeCounter != 0) {
            throw new SmartScriptParserException("Number of FOR tags and END tags is not equal!");
        }
	}

    /**
     * Method creates array of type {@link Node} in order as they appear in document.
     * @return  Array of nodes.
     */
	private Node[] nodify() {
		List<Node> nodesCollection = new ArrayList<>();
		
		while(!reader.isEndOfString()) {
			nodesCollection.add(nextNode());
		}
		
		int numberOfNodes = nodesCollection.size();
		Node[] nodes = new Node[numberOfNodes];
		for(int i = 0; i < numberOfNodes; i++) {
			nodes[i] = nodesCollection.get(i);
		}
		
		return nodes;
	}

    /**
     * Generates next node in a structured document.
     * @return  Next node.
     */
	private Node nextNode() {

        if (isCurrentTagOpener()) {
            return getNextTagNode();
        }

        return getNextTextNode();
    }

    /**
     * Generates next node of type {@link TextNode}.
     * @return  Next {@link TextNode}.
     */
    private Node getNextTextNode() {
        String string = reader.readUntil(tagOpener);
        return new TextNode(string);
    }

    /**
     * Generates next node which represents tag
     * (node of type {@link EchoNode}, {@link ForLoopNode} or {@link EndNode}).
     * @return  Next node that represents a tag.
     */
    private Node getNextTagNode() {
        reader.skip(tagOpener.length());
        reader.skipSpaces();
        try {
            String string = reader.readCarefullyUntil(tagCloser);
            reader.skip(tagCloser.length());
            return newTagNodeFromString(string);
        } catch (CharReaderException e) {
            throw new TagNotClosedException();
        }
    }

    /**
     * Checks if current position in document contains tag opener.
     * @return  {@code true} if current position points to tag opener, {@code false} otherwise.
     */
	private boolean isCurrentTagOpener() {
		if(!reader.hasNext()) {
            return false;
        }
		return reader.current(tagOpener.length()).equals(tagOpener);
	}

    /**
     * Generates next node which represents tag
     * (node of type {@link EchoNode}, {@link ForLoopNode} or {@link EndNode})
     * from string read by {@link CharReader}.
     * @param string    String read by {@link CharReader}.
     * @return  Next node that represents a tag.
     */
	private Node newTagNodeFromString(String string) {
		
		char firstCharacter = Character.toUpperCase(string.charAt(0));
		
		switch (firstCharacter) {
			case '=':
				return newEchoNodeFromString(string.substring(1));
			case 'F':
				if (string.substring(0, 3).equalsIgnoreCase("FOR")) {
					return newForLoopNodeFromString(string.substring(3));
				}
			case 'E':
				if (string.substring(0, 3).equalsIgnoreCase("END")) {
					return new EndNode();
				}
			default:
                System.out.println(string);
                throw new InvalidTagNameException();
		}
	}

    /**
     * Generates next node of type {@link ForLoopNode}, which represents for-loop construct,
     * from string read by {@link CharReader}.
     * @param string    String read by {@link CharReader}.
     * @return          Next node that represents a for-loop construct.
     */
	private ForLoopNode newForLoopNodeFromString(String string) {
		Tokenizer t = new Tokenizer(string);
		Token[] tokens = t.getTokens();
		if(!(tokens[0] instanceof TokenVariable) || tokens.length > 4 || tokens.length < 3) {
			throw new ForLoopArgumentsException();
		}
		
		if(tokens.length == 3) {
			return new ForLoopNode((TokenVariable)tokens[0], tokens[1], tokens[2]);
		}
		
		if(tokens.length == 4) {
			return new ForLoopNode((TokenVariable)tokens[0], tokens[1], tokens[2], tokens[3]);
		}
		
		return null;
	}

    /**
     * Generates next node of type {@link EchoNode}, which represents
     * a command which generates some textual output dynamically,
     * from string read by {@link CharReader}.
     * @param string    String read by {@link CharReader}.
     * @return          Next node that represents dynamical output.
     */
	private EchoNode newEchoNodeFromString(String string) {
		Tokenizer t = new Tokenizer(string);
		return new EchoNode(t.getTokens());
	}
}
