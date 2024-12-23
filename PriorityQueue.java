/*
------------------------
Author: Matvii Repetskyi
------------------------
*/

import java.util.Arrays;

public class PriorityQueue<T extends Comparable<T>> {
    private T[] heap;
    private int currentSize;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 10;

    // Constructor with default capacity
    public PriorityQueue() {
        this.capacity = DEFAULT_CAPACITY;
        this.heap = (T[]) new Comparable[capacity];
        this.currentSize = 0;
    }

    // Constructor for specified capacity
    public PriorityQueue(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity];
        this.currentSize = 0;
    }

    // Constructor that takes an array and heapifies it
    public PriorityQueue(T[] items) {
        this.capacity = items.length;
        this.heap = Arrays.copyOf(items, capacity);
        this.currentSize = items.length;
        heapify();
    }


    public void enqueue(T item) {
        if (currentSize == capacity) {
            expandCapacity();
        }
        heap[currentSize] = item;
        bubbleUp(currentSize);
        currentSize++;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public int size() {
        return currentSize;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        T min = heap[0];
        heap[0] = heap[currentSize - 1];
        currentSize--;
        bubbleDown(0);
        return min;
    }

    private void bubbleUp(int i) {
        int parent = (i - 1) / 2;
        while (i > 0 && heap[i].compareTo(heap[parent]) < 0) {
            swap(i, parent);
            i = parent;
            parent = (i - 1) / 2;
        }
    }

    // bubbles down the elements
    private void bubbleDown(int i) {
        int smallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < currentSize && heap[left].compareTo(heap[smallest]) < 0) {
            smallest = left;
        }

        if (right < currentSize && heap[right].compareTo(heap[smallest]) < 0) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            bubbleDown(smallest);
        }
    }

    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void expandCapacity() {
        capacity = capacity * 2;
        heap = Arrays.copyOf(heap, capacity);
    }

    private void heapify() {
        // start from the last parent node and bubble down each node
        for (int i = (currentSize / 2) - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }
}