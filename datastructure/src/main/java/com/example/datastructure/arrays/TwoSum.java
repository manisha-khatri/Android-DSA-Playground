package com.example.datastructure.arrays;

import java.util.HashMap;

public class TwoSum {
    public static void main(String[] args) {
        int[] nums = {3,2,4};
        int target = 6;

        int pos[] = twoSum(nums, target);
        for(int i: pos) {
            System.out.print(" pos = " + i);
        }

        System.out.println();

        pos = twoSum3(nums, target);
        for(int i: pos) {
            System.out.print(" pos = " + i);
        }
    }

    public static int[] twoSum(int[] nums, int target) {
        for(int i=0; i<nums.length; i++) {
            for(int j=i+1; j<nums.length; j++) {
                if(nums[j] == Math.abs(target - nums[i])) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    //HashMap
    public static int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i=0; i<nums.length; i++) {
            if(map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            int minus = target - nums[i];
            map.put(minus, i);
        }
        return new int[]{};
    }

    public static int[] twoSum3(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i=0; i<nums.length; i++) {
            if(map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            int minus = target - nums[i];
            map.put(minus, i);
        }
        return new int[]{};
    }

}
