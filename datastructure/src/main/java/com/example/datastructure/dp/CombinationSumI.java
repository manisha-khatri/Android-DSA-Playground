package com.example.datastructure.dp;

import java.util.ArrayList;
import java.util.List;

public class CombinationSumI {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        int target = 4;
        List<List<Integer>> ans = new ArrayList<>();

        findComb(0, target, nums, ans, new ArrayList<>());

        System.out.print("Ans is = ");

        for(List list: ans) {
            System.out.println(list);
        }
    }

    static void findComb(int i, int target, int[] nums, List<List<Integer>> ans, List<Integer> ds) {
        if(target == 0) {
            ans.add(new ArrayList<>(ds));
            return;
        }

        if(i==nums.length || target<0) {
            return;
        }

        if(nums[i]<=target) {
            ds.add(nums[i]);
            findComb(i, target-nums[i], nums, ans, ds);
            ds.remove(ds.size()-1);
        }

        findComb(i+1, target, nums, ans, ds);
    }
}
