package com.example.datastructure.dp;

import java.util.ArrayList;
import java.util.List;

public class CombinationSumIV {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        int target = 4;
        List<List<Integer>> ans = new ArrayList<>();

        findComb(target, ans, new ArrayList<>(), nums);

        System.out.print("Ans is = ");

        for(List list: ans) {
            System.out.println(list);
        }
    }

    static void findComb(int target, List<List<Integer>> ans, List<Integer> ds, int[] nums) {
        if(target == 0) {
            ans.add(new ArrayList<>(ds));
            return;
        }

        if(target<0) return;

        for(int i=0; i< nums.length; i++) {
            if(nums[i]<=target) {
                ds.add(nums[i]);
                findComb(target-nums[i], ans, ds, nums);
                ds.remove(ds.size()-1);
            }
        }
    }
}
