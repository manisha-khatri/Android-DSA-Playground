package com.example.ds.graph;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        int longestStreak = 0;
        Set<Integer> numSet = new HashSet<>();

        for(int num: nums)
            numSet.add(num);

        for(int num: numSet) {
            if(!numSet.contains(num-1)) {
                int currentStreak = num;
                int count = 1;

                while(numSet.contains(currentStreak+1)) {
                    count ++;
                    currentStreak++;
                }
                longestStreak = Math.max(longestStreak, count);
            }
        }
        return longestStreak;
    }

    public static void main(String[] args) {
        int nums[] = {1,0,1,2};

        LongestConsecutiveSequence ls = new LongestConsecutiveSequence();

        System.out.println(ls.longestConsecutive(nums));
    }
}
