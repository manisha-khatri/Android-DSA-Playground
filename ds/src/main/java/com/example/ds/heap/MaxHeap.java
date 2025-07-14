package com.example.ds.heap;

import java.util.ArrayList;

public class MaxHeap {
    private ArrayList<Integer> heap;

    public MaxHeap() {
        heap = new ArrayList<>();
    }

    void insert(int value) {
        heap.add(value);
        int index = heap.size()-1;
        bubbleUp(index);
    }

    void bubbleUp(int index) {
        int parentIndex = (index-1)/2;

        while(index>0 && heap.get(index) > heap.get(parentIndex)) {
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

    void heapify(int ar[]) {


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

        /**
         * - Ensuring left child exists otherwise Index Out of Bounds
         * - checking all the children
         */
        while(2 * i + 1 < heap.size()) { //
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            int biggest = leftChild;

            if(rightChild < heap.size() && heap.get(rightChild) > heap.get(leftChild)) {
                biggest = rightChild;
            }

            if(heap.get(i) >= heap.get(biggest))
                break;

            swap(i, biggest);
            i = biggest;
        }
        return root;
    }

    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap();
        maxHeap.insert(10);
        maxHeap.insert(20);
        maxHeap.insert(15);
        maxHeap.insert(30);
        maxHeap.insert(40);
        maxHeap.insert(5);

        maxHeap.printHeap(); // Output: [40, 30, 15, 10, 20, 5]

        maxHeap.insert(90);

        maxHeap.printHeap(); // Output: [40, 30, 15, 10, 20, 5]

        System.out.println("deleted: " + maxHeap.delete());
        maxHeap.printHeap();

        System.out.println("deleted: " + maxHeap.delete());
        maxHeap.printHeap();
    }
}
