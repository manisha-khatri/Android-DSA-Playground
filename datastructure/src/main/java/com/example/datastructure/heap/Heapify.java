package com.example.datastructure.heap;

public class Heapify {

    static void buildMaxHeap(int[] arr) {
        int n = arr.length;
        int nonLeafNode = n/2-1;

        for(int i=nonLeafNode; i>=0; i--) {
            heapify(arr, n, i);
        }
    }

    static void heapify(int ar[], int n, int i) {
        int largest = i;
        int left = 2*i + 1;
        int right = 2*i + 2;

        if(left<n && ar[left]>ar[largest])
            largest = left;
        if(right<n && ar[right]>ar[largest])
            largest = right;

        if (largest != i) {
            swap(ar, largest, i);
            heapify(ar, n, largest);
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {3, 19, 1, 14, 8, 7};
        System.out.println("Original array:");
        printArray(arr);

        buildMaxHeap(arr);
        System.out.println("Max-Heap:");
        printArray(arr);
    }
}
