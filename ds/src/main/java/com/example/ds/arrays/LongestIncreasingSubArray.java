package com.example.ds.arrays;

public class LongestIncreasingSubArray {

    static public int longestIncreasingSubarray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return 1;

        int start=0, end=0;
        int maxStart=0, maxEnd=0, i=1;

        while(i<nums.length) {
            if(nums[i-1]>nums[i]) {
                start = i;
            }
            end = i;
            int sum = end - start;

            if(sum>(maxEnd-maxStart)) {
                maxStart = start;
                maxEnd = end;
            }
            i++;
        }
        return maxEnd-maxStart+1;
    }

    static public int longestIncreasingSubArrayOpt(int[] nums) {
        if(nums == null || nums.length == 0) return 0;
        if(nums.length == 1) return 1;

        int maxLen = 1, start = 0;;

        for(int i=1; i< nums.length; i++) {
            if(nums[i] > nums[i-1]) {
                maxLen = Math.max(maxLen, i-start+1);
            } else {
                start = i;
            }
        }

        return maxLen;
    }


    public static void main(String[] args) {
        int[] arr = {1,2,3,4,9,8,7,11,12,13,14,15};

        System.out.println("Longest = " + longestIncreasingSubArrayOpt(arr));
    }
}
