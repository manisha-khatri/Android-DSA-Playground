package com.example.ds.arrays;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {

    static public int subarraySumBrute(int[] nums, int k) {
        int n = nums.length;
        int count = 0;
        int sum;

        for(int i=0; i<n; i++) {
            sum=0;
            for(int j=i; j<n; j++) {
                sum = sum + nums[j];
                if(sum == k) count++;
            }
        }
        return count;
    }

    static int subarraySumOp1(int[] nums, int k) {
        int n = nums.length;
        int count = 0;
        int[] prefixSum = new int[n];

        prefixSum[0] = nums[0];
        for(int i=1; i<n; i++) {
            prefixSum[i] = prefixSum[i-1] + nums[i];
        }

        Map<Integer, Integer> map = new HashMap<>(); // (sum, frequency)
        for(int j=0; j<n; j++) {
            if(prefixSum[j]==k) count++;

            int val = prefixSum[j]-k;
            if(map.containsKey(val)) {
                count += map.get(val);
            }
            map.put(val, map.getOrDefault(val,0)+1);
        }
        return count;
    }

    static int subarraySumOp2(int[] nums, int k) {
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

        System.out.println("Brute Force Approach = " + subarraySumBrute(ar, k));

        System.out.println("Optimized solution-1 = " + subarraySumOp1(ar, k));

        System.out.println("Optimized solution-2 = " + subarraySumOp2(ar, k));
    }
}
