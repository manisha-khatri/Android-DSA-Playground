package com.example.ds.recursion;

import java.util.Arrays;

public class ReverseArray {

    static void reverseArray(int[] arr, int l, int r) {
        if (l >= r) return;

        swap(arr, l, r);

        reverseArray(arr, l + 1, r - 1);
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void reverse2(int[] arr, int i) {
        if(i>= arr.length) return;

        swap(arr, i, arr.length-1-i);
        reverse2(arr, i+1);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};
        System.out.println("Original Array: " + Arrays.toString(arr));

        reverseArray(arr, 0, arr.length - 1);

        System.out.println("Reversed Array: " + Arrays.toString(arr));

        reverse2(arr, 0);
        System.out.println("Reversed Array 2: " + Arrays.toString(arr));
    }
}
