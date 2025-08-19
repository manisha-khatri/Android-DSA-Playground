package com.example.ds.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KSum {

    static List<List<Integer>> kSum(int[] nums, int k, int target) {
        Arrays.sort(nums);

        return kSumHelper(nums, target, k, 0);
    }

    static List<List<Integer>> kSumHelper(int[] nums, int target, int k, int start) {
        List<List<Integer>> res = new ArrayList<>();

        if(k==2) {
            int left = start;
            int right = nums.length-1;

            while(left<right) {
                int sum = nums[left] + nums[right];

                if(sum==target) {
                    res.add(Arrays.asList(nums[left], nums[right]));

                    while (left<right && nums[left]==nums[left+1]) left++;
                    while (left<right && nums[right]==nums[right-1]) right--;

                    left++;
                    right--;
                } else if(sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
            return res;
        }

        for(int i=start; i<nums.length-k+1; i++) {
            if(i>start && nums[i] == nums[i-1]) continue;

            List<List<Integer>> subRes = kSumHelper(nums, target-nums[i], k-1, i+1);

            for(List<Integer> subSet: subRes) {
                List<Integer> newList = new ArrayList<>();
                newList.add(nums[i]);
                newList.addAll(subSet);
                res.add(newList);
            }
        }
        return res;
    }


    public static void main(String[] args) {
        int[] nums = {-2, -1, 0, 0, 1, 2};
        int target = 0;
        int k = 4;

        List<List<Integer>> list = kSum(nums, k, target);

        for(List<Integer> row: list) {
            System.out.println(row);
        }
    }
}
