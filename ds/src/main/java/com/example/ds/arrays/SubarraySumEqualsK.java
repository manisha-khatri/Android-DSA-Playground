package com.example.ds.arrays;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {

    static int subArraySum(int[] nums, int k) {
        int count=0;

        for(int start=0; start<nums.length; start++) {
            int sum=0;
            for(int j=start; j<nums.length; j++) {
                sum = sum + nums[j];
                if(sum == k)
                    count++;
            }
        }
        return count;
    }

    static int subarraySum(int[] nums, int k) {
        int count = 0, sum = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // prefix sum 0 occurs once

        for (int num : nums) {
            sum += num;
            if (map.containsKey(sum - k)) {
                count += map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }



    public static void main(String[] args) {
        int ar[] = {1,2,3};
        int k = 3;

        System.out.println(subarraySum(ar, k));
    }
}
