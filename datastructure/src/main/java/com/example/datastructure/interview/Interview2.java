package com.example.datastructure.interview;

 class ExponentialTime {

    public static void printSubsets(int[] arr, int index, String currentSubset) {
        if (index == arr.length) {
            System.out.println(currentSubset);
            return;
        }

        printSubsets(arr, index + 1, currentSubset + arr[index] + " ");
        printSubsets(arr, index + 1, currentSubset);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        printSubsets(arr, 0, "");
    }
}