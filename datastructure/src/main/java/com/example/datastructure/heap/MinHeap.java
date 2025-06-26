package com.example.datastructure.heap;

import java.util.ArrayList;

public class MinHeap {
    private ArrayList<Integer> heap;

    MinHeap() {
        heap = new ArrayList<>();
    }

    void insert(int value) {
        heap.add(value);
        int index = heap.size()-1;
        bubbleUp(index);
    }

    void bubbleUp(int index) {
        int parentIndex = (index-1)/2;

        while(index>0 && heap.get(index) < heap.get(parentIndex)) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index-1)/2;
        }
    }

    void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public void printHeap() {
        System.out.println(heap);
    }

    int delete() {
        if(heap.isEmpty())
            return 0;

        int n = heap.size()-1;
        int root = heap.get(0);

        // Replace root with last element
        heap.set(0, heap.get(n));
        heap.remove(n); // Actually remove the last element

        int i = 0;
        while(2 * i + 1 < heap.size()) { // Ensure left child exists otherwise Index Out of Bounds
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            int smallest = leftChild;

            if(rightChild < heap.size() && heap.get(rightChild) < heap.get(leftChild)) {
                smallest = rightChild;
            }

            if(heap.get(i) <= heap.get(smallest))
                break;

            swap(i, smallest);
            i = smallest;
        }
        return root;
    }

    public static void main(String[] args) {
        MinHeap minHeap = new MinHeap();
        minHeap.insert(10);
        minHeap.insert(20);
        minHeap.insert(15);
        minHeap.insert(30);
        minHeap.insert(40);
        minHeap.insert(5);

        minHeap.printHeap(); // Output: [5, 20, 10, 30, 40, 15]

        System.out.println("deleted: " + minHeap.delete());
        minHeap.printHeap();

        System.out.println("deleted: " + minHeap.delete());
        minHeap.printHeap();
    }
}
