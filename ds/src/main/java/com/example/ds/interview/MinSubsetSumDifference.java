package com.example.ds.interview;

/**
 * Given a set of positive integers S, partition the set S into two subsets S1, S2
 * such that the difference between the sum of elements in S1 and the sum of elements in S2 is minimised.
 * S = {10, 20, 15, 5, 25}
 *
 * sum(s1) - sum(s2) is minimized
 */

public class MinSubsetSumDifference {

    static int minDifference(int[] nums) {
        return findMin(nums, 0, 0, 0);
    }

    static int findMin(int[] nums, int index, int s1, int s2) {
        if(index == nums.length) {
            return Math.abs(s1-s2);
        }
        int min1 = findMin(nums, index+1,s1+nums[index], s2);
        int min2 = findMin(nums, index+1,s1, s2+nums[index]);

        return Math.min(min1, min2);
    }

    public static void main(String[] args) {
        int[] nums = {10, 20, 15, 5, 25};
        System.out.println("Minimum difference: " + minDifference(nums));
    }
}
