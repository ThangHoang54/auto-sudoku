package org.example.groupalgo.DataStructures;

/**
 * @author Group05
 */

/**
 * A circular queue implementation for storing arrays of integers.
 * Automatically resizes when capacity is exceeded.
 */
public class IntArrayQueue {
    private int[][] data;  // Array of integer arrays representing the queue
    private int front;     // Index of the front element
    private int rear;      // Index of the next insertion point
    private int size;      // Number of elements currently in the queue

    /**
     * Constructs an empty queue with the specified initial capacity.
     *
     * @param capacity The initial capacity of the queue.
     */
    public IntArrayQueue(int capacity) {
        data = new int[capacity][];
        front = 0;
        rear = 0;
        size = 0;
    }

    /**
     * Adds a new integer array to the end of the queue.
     * Automatically resizes the backing array if needed.
     *
     * @param element The integer array to enqueue.
     */
    public void enqueue(int[] element) {
        if (size == data.length) {
            resize();  // Double the capacity if full
        }
        data[rear] = element;
        rear = (rear + 1) % data.length;  // Wrap around if needed
        size++;
    }

    /**
     * Removes and returns the front element of the queue.
     *
     * @return The front integer array, or null if the queue is empty.
     */
    public int[] dequeue() {
        if (isEmpty()) return null;
        int[] result = data[front];
        front = (front + 1) % data.length;  // Move front forward
        size--;
        return result;
    }

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue has no elements, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Doubles the size of the backing array while maintaining element order.
     */
    private void resize() {
        int[][] newData = new int[data.length * 2][];
        for (int i = 0; i < size; i++) {
            // Copy elements in order starting from front
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        rear = size;
    }
}
