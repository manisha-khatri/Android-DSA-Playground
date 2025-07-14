package com.example.ds.dp;

public class JumpGame {
    public static void main(String[] args) {
        int[] nums = {2,3,1,1,4};
        int[] nums1 = {3,2,1,0,4};

        System.out.println(canJump(nums));
        System.out.println(canJump(nums1));
    }

    static public boolean canJump(int[] nums) {
        int maxLen = nums[0];
        for(int i=1; i< nums.length; i++) {
            if(i>maxLen) return false;
            if(nums.length-1 <= maxLen) return true;

            maxLen = Math.max(maxLen, i+nums[i]);
        }
        return true;
    }
}
