package hr.fer.zemris.java.tecaj.hw4.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implements simple hash table which enables storing pairs (key, value).
 * Hash collision is handled by storing pairs with same hash in linked list.
 * @author ilijan
 */
public class SimpleHashtable implements Iterable<SimpleHashtable.TableEntry> {

    /** Array for storing slots. */
    private TableEntry[] table;

    /** Number of pairs in table. */
    private int size;

    /** Load factor threshold after which the capacity (number of slots)
     *  is doubled. */
    private static final double LOAD_THRESHOLD = 0.75;

    /** Counter of modifications of table. Used to prevent concurrent
     *  modification of table while iterating over it. */
    private int modificationCount;

    /**
     * Default constructor initializes table with 16 slots.
     */
    public SimpleHashtable() {
        this(16);
    }

    /**
     * Constructor initializes table with the number of slots equal to the
     * closest power of two greater than or equal to the given number.
     * @param initialCapacity   Desired minimal initial capacity of table.
     * @throws IllegalArgumentException if given capacity is not positive integer.
     */
    public SimpleHashtable(int initialCapacity) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Initial capacity must be positive integer.");
        }
        table = new TableEntry[nearestPowerOf2(initialCapacity)];
    }

    /**
     * Calculates nearest power of 2 needed for initialization of table.
     * @param number    number for which we calculate nearest greater power of 2.
     * @return          nearest power of 2.
     */
    private int nearestPowerOf2(int number) {
        int nearestExponent = (int)Math.ceil(Math.log(number)/Math.log(2));
        return (int)Math.pow(2, nearestExponent);
    }

    /**
     * Inner class which acts like a structure holding pair (key, value).
     */
    public static class TableEntry {

        /** Key of the data pair. */
        private Object key;

        /** Value of the data pair. */
        private Object value;

        /** Reference to next data pair in linked list. */
        private TableEntry next;

        /**
         * Constructor initializes {@link TableEntry} to given key, value, and next node reference.
         * @param key   key of the data pair that {@link TableEntry} represents.
         * @param value value of the data pair that {@link TableEntry} represents.
         * @param next  reference to the next node in linked list.
         */
        public TableEntry(Object key, Object value, TableEntry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Getter for the key.
         * @return  key of the data pair.
         */
        public Object getKey() {
            return key;
        }

        /**
         * Getter for the value.
         * @return  value of the data pair.
         */
        public Object getValue() {
            return value;
        }

        /**
         * Setter for the value.
         * @param value value of the data pair to be set.
         */
        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }

    /**
     * Puts new data pair in hash table. If data pair with the same key exists in the
     * table, existing value is overwritten. If load factor of the table exceeds predefined
     * load threshold number of slots is doubled, and data is copied over.
     * @param key   key of the data pair.
     * @param value value of the data pair.
     */
    public void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key must be non-null reference.");
        }
        int slotIndex = getSlotIndex(key);
        table[slotIndex] = putInList(table[slotIndex], key, value);
        modificationCount++;

        if (loadFactor() > LOAD_THRESHOLD) {
            doubleCapacity();
        }
    }

    /**
     * Helping method for putting new data point in linked list, after the index of slot in
     * which data is to be stored is determined. If the slot is empty, new node is created and
     * returned.
     * @param node    reference to starting node in a linked list.
     * @param key           key of data pair to be stored.
     * @param value         value of data pair to be stored.
     * @return              reference to starting node in a slot.
     */
    private TableEntry putInList(TableEntry node, Object key, Object value) {
        if (node == null) {
            size++;
            return new TableEntry(key, value, null);
        } else {
            if (node.key.equals(key)) {
                node.setValue(value);
            } else {
                node.next = putInList(node.next, key, value);
            }
            return node;
        }
    }

    /**
     * Doubles capacity (number of slots) of table if load threshold is exceeded.
     */
    private void doubleCapacity() {
        modificationCount++;
        SimpleHashtable newHashtable = new SimpleHashtable(2*table.length);
        for (TableEntry node : this) {
            newHashtable.put(node.getKey(), node.getValue());
        }
        clear();
        table = newHashtable.table;
    }

    /**
     * Clears table of slots.
     */
    public void clear() {
        for (TableEntry slot : table) {
            slot = null;
        }
        modificationCount++;
    }

    /**
     * Calculates load factor (number of entries / number of slots) of the hash map.
     * @return  load factor of the hash map.
     */
    private double loadFactor() {
        return ((double)size) / table.length;
    }

    /**
     * Calculates the index of a slot for storing data with given key.
     * @param key   key for which the index of a slot needs to be calculated.
     * @return      index of slot in which the pair with given key is to be stored.
     */
    public int getSlotIndex(Object key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * Gets value for the given key. If the table doesn't contain given key,
     * {@code null} value is returned.
     * @param key   key for which the value is returned.
     * @return      the value for the given key.
     */
    public Object get(Object key) {
        if (key == null) {
            return null;
        } else {
            int slotIndex = getSlotIndex(key);
            return getFromList(table[slotIndex], key);
        }
    }

    /**
     * Helping method for getting value for the given key from linked list,
     * after the index of slot in which data is expected to be stored is determined.
     * If the table doesn't contain given key, {@code null} value is returned.
     * @param node  reference to the starting node in a linked list.
     * @param key   key for which the value is returned.
     * @return      the value for the given key.
     */
    private Object getFromList(TableEntry node, Object key) {
        if (node == null) {
            return null;
        } else if (node.key.equals(key)) {
            return node.getValue();
        } else {
            return getFromList(node.next, key);
        }
    }

    /**
     * Getter for the size of the table (number of data pairs).
     * @return  size of hash table.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the table contains given key.
     * @param key   key to be checked.
     * @return      {@code true} if table contains given key, {@code false} otherwise.
     */
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        } else {
            int slotIndex = getSlotIndex(key);
            return getFromList(table[slotIndex], key) != null;
        }
    }

    /**
     * Removes data pair with given key from table.
     * @param key   key of data pair to be removed.
     */
    public void remove(Object key) {
        if (key != null) {
            int slotIndex = getSlotIndex(key);
            table[slotIndex] = removeFromList(table[slotIndex], key);
        }
    }

    /**
     * Helping method for removing data pair with the given key from linked list,
     * after the index of slot in which data is expected to be stored is determined.
     * @param node  reference to the starting node in a linked list.
     * @param key   key for which the pair needs to be removed.
     * @return      starting node of the linked list with removed pair.
     */
    private TableEntry removeFromList(TableEntry node, Object key) {
        if (node == null) {
            return node;
        }
        if (node.key.equals(key)) {
            size--;
            modificationCount++;
            return node.next;
        } else {
            node.next = removeFromList(node.next, key);
            return node;
        }
    }

    /**
     * Checks if the table is empty.
     * @return  {@code true} if the table is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks if the table contains given value.
     * @param value     value to be checked.
     * @return          {@code true} if table contains given value, {@code false} otherwise.
     */
    public boolean containsValue(Object value) {
        for (TableEntry slot : table) {
            if (listContainsValue(slot, value) == true) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helping method for checking if linked list contains given value.
     * @param node      reference to the starting node in a linked list.
     * @param value     value which is looked up in linked list.
     * @return          {@code true} if linked list contains given value,
     *                  {@code false} otherwise.
     */
    private boolean listContainsValue(TableEntry node, Object value) {
        if (node == null) {
            return false;
        }
        if(node.value.equals(value)) {
            return true;
        } else {
            return listContainsValue(node.next, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder hashtableString = new StringBuilder();
        hashtableString.append("[");

        for (TableEntry slot : table) {
            appendListToString(hashtableString, slot);
        }

        int stringLength = hashtableString.length();
        if (!isEmpty()) {
            hashtableString.delete(stringLength - 2, stringLength);
        }
        hashtableString.append("]");

        return hashtableString.toString();
    }

    /**
     * Helping method which appends nodes stored in linked list to hash table
     * {@code String} representation.
     * @param hashtableString   {@link StringBuilder} for storing {@code String} representation.
     * @param node              node in linked list to be added to string,
     *                          including its successors.
     */
    private void appendListToString(StringBuilder hashtableString, TableEntry node) {
        if (node != null) {
            hashtableString.append(node).append(", ");
            appendListToString(hashtableString, node.next);
        }
    }

    @Override
    public Iterator<TableEntry> iterator() {
        return new TableEntryIterator();
    }

    /**
     * Inner class which provides iterator for iterating over data pairs in hash table.
     */
    private class TableEntryIterator implements Iterator<TableEntry> {

        /** Index of current slot. */
        private int currentSlotIndex;

        /** Index of previous slot. Used in method {@link #remove}. */
        private int previousSlotIndex;

        /** Counter of passed entries. */
        private int passedEntriesCount;

        /** Modification count for the table.
         *  Used to prevent concurrent modification of table while iterating. */
        private int initialModificationCount;

        /** Current node in linked list. */
        private TableEntry currentNode;

        /** Reference to previous node in linked list.
         *  Used in method {@link #remove}. */
        private TableEntry previousNode;

        /** Checks if method {@link #next} has been called
         *  before the method {@link #remove}. */
        private boolean isElementRemovable;

        /**
         * Constructor sets initial modification count and reference to current node.
         */
        private TableEntryIterator() {
            initialModificationCount = modificationCount;
            if (hasNext()) {
                currentNode = table[currentSlotIndex];
            }
        }

        @Override
        public boolean hasNext() {
            checkModification();
            if (passedEntriesCount == size) {
                return false;
            }
            return true;
        }

        /**
         * Checks if concurrent modification while iterating
         * has been attempted and throws appropriate exception.
         *
         * @throws ConcurrentModificationException if concurrent modification is attempted.
         */
        private void checkModification() {
            if (modificationCount != initialModificationCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public TableEntry next() {
            checkModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            while(hasNext() && currentNode == null) {
                goToNextSlot();
            }

            previousNode = currentNode;
            passedEntriesCount++;
            isElementRemovable = true;

            if(hasNext() && currentNode.next == null) {
                previousSlotIndex = currentSlotIndex;
                goToNextSlot();
            } else {
                previousSlotIndex = currentSlotIndex;
                currentNode = currentNode.next;
            }

            return previousNode;
        }

        /**
         * Helping method moves to next slot if current slot is empty.
         */
        private void goToNextSlot() {
            currentSlotIndex++;
            currentNode = table[currentSlotIndex];
        }

        @Override
        public void remove() {
            checkModification();
            if (isElementRemovable) {
                table[previousSlotIndex] = removeFromList(table[previousSlotIndex], previousNode.key);
                isElementRemovable = false;
                passedEntriesCount--;
                initialModificationCount++;
            } else {
                throw new IllegalStateException("The next method has not yet been called, or the remove method has already been called after the last call to the next method.");
            }
        }
    }
}
