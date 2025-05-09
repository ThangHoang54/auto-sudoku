package org.example.groupalgo.DataStructures;

/**
 * @author Group05
 */

/**
 * A simple generic dynamic array implementation similar to Java's ArrayList.
 *
 * @param <T> The type of elements stored in this list.
 */
public class MyArrayList<T> {
    private Object[] data;  // Internal array to hold list elements
    private int size;       // Number of elements currently in the list

    /**
     * Constructs an empty list with an initial capacity of 10.
     */
    public MyArrayList() {
        data = new Object[10];
        size = 0;
    }

    /**
     * Adds a new element to the end of the list.
     * Resizes the internal array if necessary.
     *
     * @param value The value to be added.
     */
    public void add(T value) {
        if (size == data.length) {
            resize();  // Double the array size if full
        }
        data[size++] = value;
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public T get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException();
        return (T) data[index];  // Type-safe cast (with warning suppressed at runtime)
    }

    /**
     * Returns the current number of elements in the list.
     *
     * @return The size of the list.
     */
    public int getSize() {
        return size;
    }

    /**
     * Removes the last element from the list, if it exists.
     */
    public void removeLast() {
        if (size > 0) {
            data[--size] = null;  // Help garbage collection
        }
    }

    /**
     * Resizes the internal array to double its current capacity.
     */
    private void resize() {
        Object[] newData = new Object[data.length * 2];
        if (size >= 0) {
            System.arraycopy(data, 0, newData, 0, size);
        }
        data = newData;
    }
}


