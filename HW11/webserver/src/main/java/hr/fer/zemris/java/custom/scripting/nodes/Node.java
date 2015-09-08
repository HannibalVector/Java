package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for all graph nodes in the graph representing the structure of structured documents.
 */
public abstract class Node {

    /** Holds references to node's children nodes. */
	private List<Node> children;

    /**
     * Adds node to node's children collection.
     * If node does not have any children creates collection for storing children on demand.
     * @param child Parameter of type {@link Node} to be added as node's child.
     */
	public void addChildNode(Node child) {
		if(children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

    /**
     * Returns number of children of the node.
     * @return  number of children of the node.
     */
	public int numberOfChildren() {
		if(children != null) {
			return children.size();
		} else {
			return 0;
		}
	}

    /**
     * Returns child of the node for given index.
     * Throws {@link SmartScriptParserException} if getting a child of an empty node is attempted.
     * @param index Index of the child of the node that method returns.
     * @return      Child of the node with given index.
     */
	public Node getChild(int index) {
        if(children == null) {
            throw new SmartScriptParserException("Trying to get a child of an empty node.");
        }
		return children.get(index);
	}

    /**
     * Returns {@code Iterable<Node>} view of the object {@link Node}.
     * @return  new {@code Iterable<Node>} object.
     */
    public Iterable<Node> getChildren() {
        return () -> children.iterator();
    }

    /**
     * Checks if an object is semantically equal to the node (if an object is of type {@code Node}
     * and contains the same children nodes in the same order as the node).
     * @return  {@code true} if an object is semantically equal to the node, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Node)) {
            return false;
        }
        Node other = (Node) object;
        if(numberOfChildren()==0 && other.numberOfChildren()==0) {
            return true;
        }
        if(numberOfChildren() != other.numberOfChildren()) {
            return false;
        }
        int numberOfChildren = numberOfChildren();
        for (int i = 0; i < numberOfChildren; i++) {
            if(!getChild(i).equals(other.getChild(i))) {
                return false;
            }
        }
        return true;
    }

    public abstract void accept (INodeVisitor visitor);
}