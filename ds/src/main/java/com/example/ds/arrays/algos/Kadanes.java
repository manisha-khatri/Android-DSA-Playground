package com.example.ds.arrays.algos;

/**
 * Kadane's Algo.
 *
 * - Maximum Subarray Sum
 *
 * arr[] = {1,2,3,4,5}
 *  1 2 3 4 5
 *  12 23 34 45
 *  123 234 345
 *  1234 2345
 *  12345
 *
 *  start   end
 *  0       0,1,2,3,4..
 *  1       1,2,3,4..
 *  2       2,3,4..
 *  3       3,4,..
 *
 */
public class Kadanes {
    public static void main(String[] args) {

        int[] arr = {-2,1,-3,4,-1,2,1,-5,4};

        solution1(arr);
        maxSum1(arr);
        kadaneAlgo(arr);
    }

    /**
     * (+ve) + (-ve) = (-ve) --> then reset to 0
     */
    static void kadaneAlgo(int[] arr) {
        int maxSum, curSum=0;
        maxSum = Integer.MIN_VALUE;

        for(int start=0; start<arr.length; start++) {
            curSum += arr[start];
            maxSum = Math.max(curSum, maxSum);

            if(curSum<0) // Reset is happening at the end because if all the elements are negative
                curSum=0;
        }
        System.out.println("Kadane's Max sum = " + maxSum);
    }

    /**
     * Brute Force - O(n^3)
     * Printing all the subarrays
     */
    static void solution1(int[] arr) {
        for(int start=0; start<arr.length; start++) {
            for(int end=start; end<arr.length; end++) {
                for(int i=start; i<=end; i++) {
                    System.out.print(arr[i]);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * Finding the maximum subarray
     * Brute Force - O(n^2)
     */
    static void maxSum1(int[] arr) {
        int currentSum, maxSum = Integer.MIN_VALUE;

        for(int start=0; start<arr.length; start++) {
            currentSum =0;
            for(int end=start; end<arr.length; end++) {
                currentSum += arr[end];
                maxSum = Math.max(currentSum, maxSum);
            }
        }
        System.out.println("Max sum = " + maxSum);
    }

}
