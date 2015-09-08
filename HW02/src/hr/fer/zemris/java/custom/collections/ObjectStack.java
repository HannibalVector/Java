package hr.fer.zemris.java.custom.collections;

/**
 * Resizeable stack of objects. Duplicate elements are allowed,
 * {@code null} references are not allowed.
 * The backing array doubles its capacity when pushing is attempted
 * onto a full stack.
 * 
 * @author ilijan
 *
 */
public class ObjectStack {
	
	/** Collection for which class {@code ObjectStack} implements adapter.  */
	private ArrayBackedIndexedCollection collection;
	
	/**
	 * The default constructor creates a stack with capacity for 16 elements.
	 */
	public ObjectStack() {
		collection = new ArrayBackedIndexedCollection();
	}

	/**
	 * Checks if stack is empty.
	 * @return	{@code true} if stack is empty, otherwise {@code false}.
	 */
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	
	/**
	 * Returns the number of currently stored objects in the stack.
	 * @return	The number of currently stored objects in the stack.
	 */
	public int size() {
		return collection.size();
	}
	
	/**
	 * Pushes given {@code value} on the stack.
	 * {@code null} value is not allowed.
	 * @param value		Value to be pushed on the stack.
	 */
	public void push(Object value) {
		collection.add(value);
	}
	
	/**
	 * Removes the last value pushed on stack from stack and returns it.
	 * @return	The last value pushed on stack.
	 */
	public Object pop() {
		Object lastElement = peek();
		
		int indexOfLastElement = collection.size() - 1;
		collection.remove(indexOfLastElement);
		
		return lastElement;
	}
	
	/**
	 * Throws {@code EmptyStackException} if the stack is empty.
	 */
	private void checkIfStackIsEmpty() {
		if (collection.isEmpty()) {
			throw new EmptyStackException("Cannot read from empty stack.");
		}	
	}
	
	/**
	 * Returns the last value pushed on stack.
	 * @return	The last value pushed on stack.
	 */
	public Object peek() {
		checkIfStackIsEmpty();
		int indexOfLastElement = collection.size() - 1;
		Object lastElement = collection.get(indexOfLastElement);
		
		return lastElement;
	}
	
	/**
	 * Removes all elements from the stack.
	 * The allocated array is left at current capacity.
	 */
	public void clear() {
		collection.clear();
	}
}
