package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Abstract node visitor. Used for execution of commands represented by nodes.
 * @author ilijan
 */
public interface INodeVisitor {
    /**
     * Visitor for node representing text.
     * @param node node representing text.
     */
    void visitTextNode(TextNode node);

    /**
     * Visitor for node representing for loop.
     * @param node node representing for loop.
     */
    void visitForLoopNode(ForLoopNode node);

    /**
     * Visitor for node representing echo command.
     * @param node node representing echo command.
     */
    void visitEchoNode(EchoNode node);

    /**
     * Visitor for node representing whole document.
     * @param node node representing whole document.
     */
    void visitDocumentNode(DocumentNode node);

    /**
     * Visitor for node representing group of other nodes.
     * @param node node representing group of other nodes.
     */
    default void visitNodesGroup(Node node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }
}
