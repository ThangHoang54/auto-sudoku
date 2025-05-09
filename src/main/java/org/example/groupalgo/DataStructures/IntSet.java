package org.example.groupalgo.DataStructures;

/**
 * @author Group05
 */

/**
 * Represents a set of integers with values ranging from 1 to 9.
 * Supports typical set operations like add, remove, check containment, and copying.
 */
public class IntSet {
    private int[] values;  // Array to store the set values
    private int size;      // Number of current elements in the set

    /**
     * Default constructor.
     * Initializes the set with values 1 through 9.
     */
    public IntSet() {
        values = new int[9];
        size = 9;
        for (int i = 0; i < 9; i++) {
            values[i] = i + 1;
        }
    }

    /**
     * Constructs a set with a single integer value.
     *
     * @param val The initial value to be placed in the set.
     */
    public IntSet(int val) {
        values = new int[1];
        values[0] = val;
        size = 1;
    }

    /**
     * Returns the number of elements currently in the set.
     *
     * @return The size of the set.
     */
    public int getSize() {
        return size;
    }

    /**
     * Adds a value to the set if it is not already present and within bounds (1–9).
     *
     * @param value The value to add.
     */
    public void add(int value) {
        if (!contains(value) && size < 9 && value >= 1 && value <= 9) {
            values[size++] = value;
        }
    }

    /**
     * Removes a value from the set if present.
     * Replaces the removed value with the last element to maintain compactness.
     *
     * @param val The value to remove.
     * @return true if the value was removed, false if it wasn't found.
     */
    public boolean remove(int val) {
        for (int i = 0; i < size; i++) {
            if (values[i] == val) {
                values[i] = values[size - 1]; // Replace with last element
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the set contains a given value.
     *
     * @param value The value to search for.
     * @return true if the value exists in the set, false otherwise.
     */
    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            if (values[i] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the only value in the set if it contains exactly one element.
     *
     * @return The single value in the set, or -1 if the set has more than one element.
     */
    public int getOnlyValue() {
        return size == 1 ? values[0] : -1;
    }

    /**
     * Returns a new array containing the current elements of the set.
     *
     * @return An array of the values in the set.
     */
    public int[] getValues() {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = values[i];
        }
        return result;
    }

    /**
     * Creates a deep copy of the current IntSet.
     *
     * @return A new IntSet object containing the same values.
     */
    public IntSet copy() {
        IntSet copy = new IntSet();
        copy.size = this.size;
        copy.values = new int[9];
        if (size >= 0) {
            System.arraycopy(this.values, 0, copy.values, 0, size);
        }
        return copy;
    }
}

