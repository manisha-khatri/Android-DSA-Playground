package com.example.datastructure.dp;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DecodeWays {
    public static void main(String[] args) {
        System.out.println(decodeWaysBruteForce("123"));
        System.out.println(decodeWaysBruteForce("122016"));

        System.out.println("Optimized -- ");

        System.out.println(decodeWaysOptimized("123"));
        System.out.println(decodeWaysOptimized("122016"));
    }

    static int decodeWaysBruteForce(String s) {
        return decodeWaysBruteForce(0,s);
    }

    /**
     * Brute Force Solution with Recursion
     * TC = O(2^n)
     * SC = O(n) -- Call stack depth due to recursion
     */
    static int decodeWaysBruteForce(int i, String s) {
        if(i == s.length()) {
            return 1;
        }

        if(s.charAt(i) == '0') {
            return 0;
        }

        // One Digit
        int count = decodeWaysBruteForce(i+1, s);

        // Two Digit
        if(i+1 < s.length()) {
            int twoDigit = Integer.parseInt(s.substring(i, i+2));
            if(twoDigit<=26 && twoDigit>=10) {
                count += decodeWaysBruteForce(i+2, s);
            }
        }

        return count;
    }

    static int decodeWaysOptimized(String s) {
        int[] memo = new int[s.length()+1];

        Arrays.fill(memo, -1);

        return decodeWaysOptimized(0,s,memo);
    }

    /**
     * Optimized Top Down, DP, Recursion Solution
     * TC = O(n)
     * SC = O(n) -- Call stack depth due to recursion
     */
    static int decodeWaysOptimized(int i, String s, int[] memo) {
        if(i == s.length())
            return 1;

        if(s.charAt(i) == '0')
            return 0;

        if(memo[i] != -1)
            return memo[i];

        // One Digit
        int count = decodeWaysOptimized(i+1, s, memo);

        if(i+1 < s.length()) {
            int twoDigit = Integer.parseInt(s.substring(i, i+2));
            if(twoDigit>=10 && twoDigit<=26) {
                count += decodeWaysOptimized(i+2, s, memo);
            }
        }

        return memo[i] = count;
    }
}
