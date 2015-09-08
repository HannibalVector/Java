package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node that represents an entire document. It inherits from class {@link Node}.
 */
public class DocumentNode extends Node {
    @Override
    public void accept(INodeVisitor visitor) {
        visitor.visitDocumentNode(this);
    }
}
