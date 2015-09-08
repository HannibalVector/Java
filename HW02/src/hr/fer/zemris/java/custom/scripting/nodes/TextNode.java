package hr.fer.zemris.java.custom.scripting.nodes;

/**
 *  A node representing a piece of textual data. It inherits from class {@link Node}.
 */
public class TextNode extends Node {

    /** Variable holds text that the node represents. */
	private String text;

    /**
     * Returns the text that node represents.
     * @return  The text that node represents.
     */
	public String getText() {
		return text;
	}

    /**
     * Constructor initializes node using text to be stored in the node as a single parameter.
     * @param text  Text to be stored in the node.
     */
	public TextNode(String text) {
		this.text = text;
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this {@code TextNode}.
     * Overrides method {@code getOriginalText()} from parent class {@link Node}.
     * The method generates escape characters where needed to ensure that generated text
     * can be again parsed to the semantically equal node.
     * @return  {@code String} that represents code which would get parsed to this {@code TextNode}.
     */
    @Override
    public String getOriginalText() {
        return text.replace("\\", "\\\\").replace("{", "\\{");
    }

    /**
     * Checks if an object is semantically equal to the node (if an object is of type {@code TextNode}
     * and contains the same text as the node).
     * Overrides method {@code equals()} from parent class {@link Node}.
     * @return  {@code true} if an object is semantically equal to the node, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TextNode)) {
            return false;
        }
        TextNode other = (TextNode) object;
        return text.equals(other.getText());
    }
}
