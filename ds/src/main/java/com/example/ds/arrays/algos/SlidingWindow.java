package com.example.ds.arrays.algos;

public class SlidingWindow {
    public static void main(String[] args) {
        // find the max sum of any 3 consecutive elements, sliding window

        int[] arr = {1,2,3,1,1};

        System.out.println(maxSumOfThree(arr));
        System.out.println(maxSumOfThreeOptimized(arr));

        System.out.println(maxSumOfK(arr,4));

    }

    public static int maxSumOfThree(int[] arr) {
        if(arr.length<3) return -1;

        int windowSum = arr[0] + arr[1] + arr[2];
        int maxSum = windowSum;
        int left=0;

        for(int right=3; right<arr.length; right++) {
            if(right-left+1 > 3) {
                windowSum -=arr[left];
                left++;
            }
            windowSum += arr[right];
            maxSum = Math.max(windowSum, maxSum);
        }
        return maxSum;
    }

    public static int maxSumOfThreeOptimized(int[] arr) {
        if(arr.length<3) return -1;

        int windowSum = arr[0] + arr[1] + arr[2];
        int maxSum = windowSum;

        for(int right=3; right<arr.length; right++) {
            windowSum += arr[right] - arr[right-3];
            maxSum = Math.max(windowSum, maxSum);
        }
        return maxSum;
    }

    static int maxSumOfK(int[] arr, int k) {
        if(arr.length<k) return -1;

        int i=0;
        int windowSum=0;
        while(i<k) {
            windowSum+=arr[i];
            i++;
        }
        int maxSum = windowSum;

        for(int right=k; right<arr.length; right++) {
            windowSum += arr[right] - arr[right-k];
            maxSum = Math.max(windowSum, maxSum);
        }
        return maxSum;
    }
}
