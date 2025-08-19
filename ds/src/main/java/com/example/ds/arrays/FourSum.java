package com.example.ds.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FourSum {

    /**
     * Brute Force
     * TC = O(n4)
     * SC = O(n)
     *  O(n2) - mainList but store only 4 elements everytime n*4
     *  O(n) - List always maintain 4 elements (negligible)
     *
     */
    static List<List<Integer>> fourSumBrute(int[] nums, int target) {
        Set<List<Integer>> mainList = new HashSet<>();

        for(int i=0; i<nums.length; i++) {
            for(int j=i+1; j<nums.length; j++) {
                for(int k=j+1; k< nums.length; k++) {
                    for(int l=k+1; l< nums.length; l++) {
                        if(nums[i]+nums[j]+nums[k]+nums[l]== target) {
                            List<Integer> elements = Arrays.asList(nums[i], nums[j], nums[k], nums[l]);
                            Collections.sort(elements);

                            mainList.add(elements);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(mainList);
    }

    /**
     * Optimal
     * tc = O(n3)
     * sc = O(n)
     */
    static List<List<Integer>> fourSumOptimal(int[] nums, int target) {
        List<List<Integer>> mainList = new ArrayList();
        Arrays.sort(nums);

        for(int i=0; i<nums.length-3; i++) {
            if(i>0 && nums[i]==nums[i-1]) continue;

            for(int j=i+1; j<nums.length-2; j++) {
                if(j>i+1 && nums[j]==nums[j-1]) continue;

                int left = i+1;
                int right = nums.length-1;

                while(left<right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];

                    if(sum == target) {
                        List<Integer> list = Arrays.asList(nums[i], nums[j], nums[left], nums[right]);
                        Collections.sort(list);
                        mainList.add(list);

                        while(left<right && nums[left]==nums[left+1]) left++;
                        while(left<right && nums[right]==nums[right-1]) right--;

                        left++;
                        right--;
                    } else if(sum<target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return mainList;
    }


    public static void main(String[] args) {
        int[] nums = {1,0,-1,0,-2,2};
        int target = 0;

        System.out.println("Brute Force Solution:-");
        List<List<Integer>> lists = fourSumBrute(nums, target);
        for(List elements: lists) {
            System.out.println(elements);
        }

        System.out.println("Optimal Solution:-");
        List<List<Integer>> lists2 = fourSumOptimal(nums, target);
        for(List elements: lists2) {
            System.out.println(elements);
        }
    }
}
