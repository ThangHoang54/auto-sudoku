package org.example.groupalgo.DataStructures;

/**
 * @author Group05
 */

/**
 * Represents a custom mapping between string keys and IntSet values.
 * Designed for use with a fixed-size structure such as a 9x9 Sudoku board.
 */
public class DomainMap {
    private String[] keys;    // Array of keys (e.g., board positions)
    private IntSet[] values;  // Corresponding array of IntSet values
    private int size;         // Current number of key-value pairs

    /**
     * Constructs an empty DomainMap with a fixed capacity of 81 entries.
     */
    public DomainMap() {
        keys = new String[81];     // Max capacity for a 9x9 grid
        values = new IntSet[81];
        size = 0;
    }

    /**
     * Associates the specified IntSet with the specified key in the map.
     * If the key already exists, its value is replaced.
     *
     * @param key   The key string to insert or update.
     * @param value The IntSet value to associate with the key.
     */
    public void put(String key, IntSet value) {
        int idx = indexOf(key);
        if (idx != -1) {
            // Key exists; update value
            values[idx] = value;
        } else {
            // New key; add to end
            keys[size] = key;
            values[size] = value;
            size++;
        }
    }

    /**
     * Returns the IntSet associated with the specified key.
     *
     * @param key The key to search for.
     * @return The associated IntSet value, or null if not found.
     */
    public IntSet get(String key) {
        int idx = indexOf(key);
        return idx != -1 ? values[idx] : null;
    }

    /**
     * Returns an array of all keys currently stored in the map.
     *
     * @return An array containing all keys in insertion order.
     */
    public String[] keySet() {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = keys[i];
        }
        return result;
    }

    /**
     * Creates a deep copy of this DomainMap, including all key-value pairs.
     *
     * @return A new DomainMap object with copied keys and cloned IntSets.
     */
    public DomainMap copy() {
        DomainMap copy = new DomainMap();
        for (int i = 0; i < size; i++) {
            copy.put(keys[i], values[i].copy());
        }
        return copy;
    }

    /**
     * Returns the index of the specified key in the map.
     *
     * @param key The key to look for.
     * @return The index if found, or -1 if not present.
     */
    private int indexOf(String key) {
        for (int i = 0; i < size; i++) {
            if (equals(keys[i], key)) return i;
        }
        return -1;
    }

    /**
     * Compares two strings for character-by-character equality.
     * Used instead of String.equals() for manual control or constraint.
     *
     * @param a First string.
     * @param b Second string.
     * @return true if strings are equal, false otherwise.
     */
    private boolean equals(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
    }
}


