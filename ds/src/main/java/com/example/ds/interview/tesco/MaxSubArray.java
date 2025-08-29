package com.example.ds.interview.tesco;

public class MaxSubArray {

    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int windowSum = 0;

        for(int i=0; i<nums.length; i++) {
            windowSum+=nums[i];
            maxSum = Math.max(windowSum, maxSum);

            if(windowSum<0) {
                windowSum = 0;
            }
        }

        return maxSum;
    }

    int[] maximumNonNegativeSubArray(int[] nums) {
        int maxStart, maxEnd;
        int maxSum = Integer.MIN_VALUE;
        int windowSum = 0;

        for(int i=0; i< nums.length; i++) {
            if(nums[i]<0) {
                windowSum = 0;
            } else {
                windowSum+=nums[i];
            }
            maxSum = Math.max(windowSum, maxSum);
        }

        return null;
    }

    public static void main(String[] args) {

    }
}
