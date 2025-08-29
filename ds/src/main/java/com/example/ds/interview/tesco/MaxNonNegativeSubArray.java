package com.example.ds.interview.tesco;

import java.util.*;

public class MaxNonNegativeSubArray {
    /**
     * Given an array of integers, find the contiguous subarray consisting only of non-negative numbers which has the maximum sum.
     * If there are multiple such subarrays with the same sum, return the one with the maximum length.
     * If there is still a tie, return the one which occurs earliest in the array.
     */

    public static int[] maxNonNegativeSubArray(int[] nums) {
        int curSum=0, curStart=0;
        int bestStart=-1, bestEnd=-1, bestSum=-1;

        for(int i=0; i<nums.length; i++) {
            if(nums[i]>=0) {
                curSum+=nums[i];

                if(i==nums.length-1) {
                    if(curSum>bestSum || (curSum==bestSum) && (i-curStart > bestEnd-bestStart)) {
                        bestSum = curSum;
                        bestStart = curStart;
                        bestEnd = i;
                    }
                }
            } else {
                if(curSum>bestSum || (curSum==bestSum) && (i-1-curStart > bestEnd-bestStart)) {
                    bestSum = curSum;
                    bestStart = curStart;
                    bestEnd = i-1;
                }
                curStart = i+1;
                curSum = 0;
            }
        }

        if(bestStart==-1)
            return new int[0];

        int newLen = bestEnd-bestStart+1;
        int[] result = new int[newLen];
        for(int i=0; i<newLen; i++) {
            result[i] = nums[bestStart];
            bestStart++;
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr2 = {1, 2, 3, -1, 6};
        System.out.println("Input: " + Arrays.toString(arr2)); // Expected: [1, 2, 3]
        int[] res = maxNonNegativeSubArray(arr2);
        System.out.println();
        for(int i: res) {
            System.out.print(i+ ",");
        }

    }
}
