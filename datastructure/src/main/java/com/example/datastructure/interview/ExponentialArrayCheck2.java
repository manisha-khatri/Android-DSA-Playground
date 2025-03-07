package com.example.datastructure.interview;

class ExponentialArrayCheck2 {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        checkExponential(arr);
    }

    public static void checkExponential(int[] arr) {
        for (int i = 0; i < arr.length - 3; i++) {
            if (arr[i + 1] > arr[i + 2]) {
                int sum = arr[i + 1] + arr[i + 2];
                if (sum > arr[i + 3]) {
                    System.out.println("Exponential pattern found at index: " + i);
                    // You can further process the result if needed
                }
            }
        }
    }
}