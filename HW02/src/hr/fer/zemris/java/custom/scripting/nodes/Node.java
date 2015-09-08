package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayBackedIndexedCollection;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Base class for all graph nodes in the graph representing the structure of structured documents.
 */
public class Node {

    /** Holds references to node's children nodes. */
	private ArrayBackedIndexedCollection childrenCollection;

    /**
     * Adds node to node's children collection.
     * If node does not have any children creates collection for storing children on demand.
     * @param child Parameter of type {@link Node} to be added as node's child.
     */
	public void addChildNode(Node child) {
		if(childrenCollection == null) {
			childrenCollection = new ArrayBackedIndexedCollection();
		}
		childrenCollection.add(child);
	}

    /**
     * Returns number of children of the node.
     * @return  number of children of the node.
     */
	public int numberOfChildren() {
		if(childrenCollection != null) {
			return childrenCollection.size();
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
        if(childrenCollection == null) {
            throw new SmartScriptParserException("Trying to get a child of an empty node.");
        }
		return (Node) childrenCollection.get(index);
	}

    /**
     * Generates {@code String} that represents standardized code which would get parsed to this {@code Node}.
     * Since general node does not hold any additional information apart from collection of children,
     * method calls method {@code getChildrenOriginalText()} which calls {@code getOriginalText()} for every node's child.
     * @return  {@code String} that represents code which would get parsed to this {@code Node}.
     */
    public String getOriginalText() {
        return getChildrenOriginalText();
    }

    /**
     * Calls {@code getOriginalText()} for every node's child and concatenates output to a single string.
     * @return  {@code String} that represents code which would get parsed to the node's children.
     */
    protected String getChildrenOriginalText() {
        StringBuilder originalTextBuilder = new StringBuilder();
        int numberOfChildren = numberOfChildren();
        for (int i = 0; i < numberOfChildren; i++) {
            originalTextBuilder.append(getChild(i).getOriginalText());
        }
        return originalTextBuilder.toString();
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
}