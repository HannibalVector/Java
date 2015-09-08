package hr.fer.zemris.java.custom.collections;

/**
 * Resizeable array-backed collection of objects.
 * Duplicate elements are allowed, {@code null} references are not allowed.
 * The backing array doubles its capacity when adding is attempted
 * to a full collection.
 * 
 * @author ilijan
 *
 */
public class ArrayBackedIndexedCollection {
	
	/** Current size of collection (number of elements actually stored).*/
	private int size;
	/** Current capacity of allocated array of object references. */
	private int capacity;
	/** An array of object references. */
	private Object[] elements;
	
	/**
	 * The default constructor creates an instance with capacity for 16 elements.
	 */
	public ArrayBackedIndexedCollection() {
		this(16);
	}

	/**
	 * Creates an instance with desired capacity (positive integer).
	 * @param initialCapacity	Initial capacity of collection to be created.
	 */
	public ArrayBackedIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException(
					"Initial capacity should not be less than 1."
			);
		}
		capacity = initialCapacity;
		elements = new Object[capacity];
	}
	
	/**
	 * Checks if collection is empty.
	 * @return	{@code true} if collection is empty, otherwise {@code false}.
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in collection.
	 * @return	The number of currently stored objects in collection.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Adds given object into the collection, at the first empty space.
	 * Passed object should not be null reference.
	 * If the backing array is full method reallocates array of double capacity.
	 * @param value		Reference to an object to be added to collection.
	 */
	public void add(Object value) {
		checkIfNull(value);
		if (isFull()) {
			doubleCapacityAndCopy();
		}
		
		elements[size] = value;
		size++;
	}
	
	/**
	 * Throws {@code IllegalArgumentException} if passed value is null reference.
	 * @param value		Reference which triggers exception if it equals null.
	 */
	private void checkIfNull(Object value) {
		if (value == null) {
			throw new IllegalArgumentException(
					"Value null can not be added as an element."
			);	
		}
	}
	
	/**
	 * Checks if collection is full.
	 * @return	{@code true} if collection is full, {@code false} otherwise.
	 */
	private boolean isFull() {
		return size == capacity;
	}
	
	/**
	 * Doubles the capacity of collection and copies over elements of the old array into the new one.
	 */
	private void doubleCapacityAndCopy() {
		Object[] oldArray = elements;
		capacity *= 2;
		elements = new Object[capacity];
		
		for(int i = 0; i < oldArray.length; i++) {
			elements[i] = oldArray[i];
		}
	}
	
	/**
	 * Returns the object that is stored in backing array at position {@code index}.
	 * @param index 	The position of the object in the backing array.
	 * @return			Object at the position {@code index} in the backing array.
	 */
	public Object get(int index) {
		checkIndexBounds(index);
		return elements[index];
	}
	
	/**
	 * If {@code index} is out of bounds of backing array throws {@code IndexOutOfBoundsException}.
	 * @param index		Index to be checked.	
	 */
	private void checkIndexBounds(int index) {
		if (index < 0 || index > size-1) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * Removes the object that is stored in backing array at position {@code index}.
	 * @param index		The position of object to be removed.
	 */
	public void remove(int index) {
		checkIndexBounds(index);
		for (int oldPosition = index+1; oldPosition < size; oldPosition++) {
			elements[oldPosition - 1] = elements[oldPosition];
		}
		elements[size-1] = null;
		size--;
	}
	
	/**
	 * Inserts (does not overwrite) the given {@code value} at the given {@code position} in array.
	 * @param value		Value to be added in array.
	 * @param position	Position at which the value will be added.
	 * 					Position should be between 0 and {@link size()}.
	 */
	public void insert(Object value, int position) {
		checkIfNull(value);
		if (position != size) {
			checkIndexBounds(position);
		}
		if (isFull()) {
			doubleCapacityAndCopy();
		}
		for (int oldPosition = size-1; oldPosition >= position; oldPosition--) {
			elements[oldPosition + 1] = elements[oldPosition];
		}
		elements[position] = value;
		size++;
	}
	
	/**
	 * Searches the collection and returns the index of the first occurrence of the given {@code value},
	 * or -1 if {@code value} is not found.
	 * @param value		Value to be searched for in the collection.
	 * @return			index of the first occurrence of the given {@code value},
	 * 					or -1 if {@code value} is not found.
	 */
	public int indexOf(Object value) {
		boolean isValueFound = false;
		int index;
		for(index = 0; index < size; index++) {
			if (elements[index].equals(value)) {
				isValueFound = true;
				break;
			}
		}
		if (isValueFound) {
			return index;
		} else {
			return -1;
		}
	}
	
	/**
	 * Checks if collection contains given value.
	 * @param value		Value to be searched for in the collection.
	 * @return			{@code true} if collection is contains {@code value},
	 * 					otherwise {@code false}.
	 */
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Removes all elements from the collection.
	 * The allocated array is left at current capacity.
	 */
	public void clear() {
		for(int index = 0; index < size; index++) {
			elements[index] = null;
		}
		size = 0;
	}
}
