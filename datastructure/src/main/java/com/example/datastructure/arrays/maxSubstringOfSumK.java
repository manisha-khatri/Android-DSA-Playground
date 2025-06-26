package com.example.datastructure.arrays;

import java.util.Arrays;

public class maxSubstringOfSumK {
    public static void main(String[] args) {
        int[] nums = { 3, 1, 2, 5, 1, 1, 2, 3 };
        int k = 6;

        for(int element: maxSubstring(nums,k)) {
            System.out.print(element + " ");
        }

        System.out.println("\n optimized ");

        for(int element: maxSubstringOptimised(nums,k)) {
            System.out.print(element + " ");
        }
    }

    static int[] maxSubstring(int[] nums, int k) {
        if(nums.length==0 || k==-1) return new int[]{};

        int curSum=0, right=-1, maxSum=0;
        int saveStart=0, saveEnd=0;

        for(int left=0; left<nums.length; left++) {

            while(curSum<=k && right<nums.length) {
                if(curSum>maxSum) {
                    maxSum = curSum;
                    saveStart = left;
                    saveEnd = right;
                }
                right++;
                if(right< nums.length) {
                    curSum = curSum + nums[right];
                } else
                    break;
            }
            curSum = curSum - nums[left];
        }

        return Arrays.copyOfRange(nums, saveStart, saveEnd+1);
    }

    static int[] maxSubstringOptimised(int[] nums, int k) {
        int saveStart=0, saveEnd=0;
        int maxSum=0, curSum=0, right=0, left=0;

        while(right < nums.length) {
            curSum = curSum + nums[right];

            //shrink the result if the sum exceed the k
            while(curSum>k && left<=right) {
                curSum = curSum - nums[left];
            }

            if(curSum>maxSum && curSum<=k) {
                maxSum = curSum;
                saveStart = left;
                saveEnd = right;
            }
            right++;
        }

        return Arrays.copyOfRange(nums, saveStart, saveEnd+1);
    }


}
