package com.example.ds.interview.sixt;

/**
 * Given a set of positive integers S, partition the set S into two subsets S1, S2
 * such that the difference between the sum of elements in S1 and the sum of elements in S2 is minimised.
 *
 *     s = {1, 2, 1, 5, 2}
 *        5 2 2 1
 *        1+2+1+5+2 = 10
 *        10%2 !=0 return false
 *
 *        greedy failing
 *        {8,7,6,5,4}.
 *        8+7+6+5+4 = 30
 *        30%2 == 0
 *
 *        8 5 = 13 + 4
 *        7 6 = 13
 *
 *        optimal:
 *        8+7 = 15
 *        6+5+4 = 15
 */
public class SubsetPresent {

    static Boolean[][] memo;
    static boolean subsetPresent(int[] nums) {
        if(nums.length<2) return false;

        int total = 0;
        for(int val: nums) {
            total += val;
        }
        int target = total/2;
        memo = new Boolean[nums.length][target+1];

        return rec(nums, target, 0);
    }

    static boolean rec(int[] nums, int target, int i) {
        if(target == 0) return true;
        if(i==nums.length || target<0) return false;
        if(memo[i][target]!=null) return memo[i][target];

        boolean notTake = rec(nums, target, i+1);
        boolean take = rec(nums, target-nums[i], i+1);

        return memo[i][target] = take || notTake;
    }


    public static void main(String[] args) {
        int[] nums = {8,7,6,5,4};

        System.out.println(subsetPresent(nums));
    }
}













