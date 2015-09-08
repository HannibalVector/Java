package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Collection similar to {@link Map}, which allows for multiple values
 * for the same key. Collection of values for single key provides a
 * stack-like behaviour. Keys are of type {@code String} and values
 * are of type {@link ValueWrapper}.
 * @author ilijan
 */
public class ObjectMultistack {
    /** Map for storing (key, list of values) pairs. */
    private Map<String, LinkedList<ValueWrapper>> elements;

    /**
     * Constructor creates new hash map for storing (key, list of values) pairs.
     */
    public ObjectMultistack() {
        elements = new HashMap<>();
    }

    /**
     * Pushes given value to a stack mapped to a given name.
     * @param name  name on whose "virtual stack" the value
     *              will be pushed.
     * @param valueWrapper  value to push on {@code Multistack}.
     */
    public void push(String name, ValueWrapper valueWrapper) {
        LinkedList<ValueWrapper> list = elements.get(name);
        if(list == null) {
            list = new LinkedList<>();
            elements.put(name, list);
        }
        list.add(valueWrapper);
    }

    /**
     * Pops value from a stack mapped to a given name.
     * @param name  name on whose "virtual stack" the value
     *              will be popped.
     */
    public ValueWrapper pop(String name) {
        LinkedList<ValueWrapper> list = elements.get(name);
        if(list == null || list.isEmpty()) {
            throw new EmptyStackException();
        }
        return list.removeLast();
    }

    /**
     * Peeks value from a stack mapped to a given name.
     * @param name  name on whose "virtual stack" the value
     *              will be peeked.
     */
    public ValueWrapper peek(String name) {
        LinkedList<ValueWrapper> list = elements.get(name);
        if(list == null || list.isEmpty()) {
            throw new EmptyStackException();
        }
        return list.getLast();
    }

    /**
     * Checks if stack for given name is empty.
     * @param name  name whose stack is checked.
     * @return  {@code true} if checked stack is empty,
     *          {@code false} otherwise.
     */
    public boolean isEmpty(String name) {
        LinkedList<ValueWrapper> list = elements.get(name);
        if(list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }
}
