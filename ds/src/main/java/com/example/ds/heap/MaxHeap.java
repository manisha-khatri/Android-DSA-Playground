package com.example.ds.heap;

public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heap = new int[capacity];
    }

    // Get parent and children indices
    private int parent(int i) { return (i - 1) / 2; }
    private int leftChild(int i) { return 2 * i + 1; }
    private int rightChild(int i) { return 2 * i + 2; }

    // Insert a new value into the heap
    public void insert(int value) {
        if (size == capacity) {
            throw new IllegalStateException("Heap is full");
        }
        heap[size] = value;
        size++;
        bubbleUp(size - 1);  // Maintain max-heap property
    }

    // Get max (root)
    public int getMax() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    // Remove max (root)
    public int extractMax() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int max = heap[0];
        heap[0] = heap[size - 1]; // Move the last element to the root
        size--;
        heapify(0); // Restore max-heap property
        return max;
    }

    // Bubble up the element at index i
    private void bubbleUp(int i) {
        while (i > 0 && heap[i] > heap[parent(i)]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    // Heapify (bubble down) at index i
    private void heapify(int i) {
        int largest = i;
        int left = leftChild(i);
        int right = rightChild(i);

        if (left < size && heap[left] > heap[largest]) {
            largest = left;
        }

        if (right < size && heap[right] > heap[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            heapify(largest);
        }
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Print the heap array
    public void printHeap() {
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    // Main method to test
    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap(10);
        maxHeap.insert(10);
        maxHeap.insert(20);
        maxHeap.insert(5);
        maxHeap.insert(30);
        maxHeap.insert(15);

        System.out.print("Max Heap: ");
        maxHeap.printHeap();

        System.out.println("Max Element: " + maxHeap.getMax()); // Returns the max element (root) without removing it.
        System.out.println("Extracted Max: " + maxHeap.extractMax()); // Returns the max element (root) and removes it from the heap.

        System.out.print("Heap after extraction: ");
        maxHeap.printHeap();
    }
}

