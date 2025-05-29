package com.example.datastructure.dp;

import java.util.Arrays;

public class LongestIncreasingSubsequence {

    public static void main(String[] args) {
        int[] nums = {10,9,2,5,3,7,101,18};

        System.out.print(longestIncreasingSubsequence(nums));
    }

    static int longestIncreasingSubsequence(int[] nums) {
        int n = nums.length;
        int[][] memo = new int[n][n+1];

        for(int[] row: memo) {
            Arrays.fill(row, -1);
        }

        return calcLIS(nums, 0, -1, memo);
    }

    static int calcLIS(int[] nums, int i, int prev, int[][] memo) {
        //Base
        if(i==nums.length) return 0;

        if (memo[i][prev+1]!=-1) return memo[i][prev+1];

        int notTake = calcLIS(nums, i+1, prev, memo);

        int take = 0;
        if(prev == -1 || nums[prev] < nums[i]) {
            take = 1 + calcLIS(nums, i+1, i, memo);
        }

        return memo[i][prev+1] = Math.max(take, notTake);
    }
}
