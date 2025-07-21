package com.example.ds.heap;

public class MinHeap {
    int capacity;
    int size;
    int[] heap;

    int parent(int i) { return (i-1)/2; }
    int left(int i) { return 2*i + 1; }
    int right(int i) { return 2*i + 2; }

    void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    MinHeap(int capacity) {
        this.capacity = capacity;
        size = 0;
        heap = new int[capacity];
    }

    void insert(int value) {
        heap[size] = value;
        size++;
        bubbleUp(size-1);
    }

    void bubbleUp(int i) {
        while(i>0 && heap[i] < heap[parent(i)]) { //only when inserted element is smaller than the parent then get swap
            swap(i, parent(i));
            i = parent(i);
        }
    }

    int extractMin() {
        int min = heap[0];
        heap[0] = heap[size-1];
        size--;
        heapify(0);
        return min;
    }

    void heapify(int i) {
        int min = i;
        int left = left(i);
        int right = right(i);

        if(left<size && heap[left]<heap[min]) {
            min = left;
        }
        if(right<size && heap[right]<heap[min]) {
            min = right;
        }

        if(min!=i) {
            swap(min, i);
            heapify(min);
        }
    }

    public void printHeap() {
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MinHeap heap = new MinHeap(10);

        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(30);
        heap.insert(15);

        System.out.print("Max Heap: ");
        heap.printHeap();

        System.out.println("Extracted Max: " + heap.extractMin()); // Returns the max element (root) and removes it from the heap.

        System.out.print("Heap after extraction: ");
        heap.printHeap();

    }
}
